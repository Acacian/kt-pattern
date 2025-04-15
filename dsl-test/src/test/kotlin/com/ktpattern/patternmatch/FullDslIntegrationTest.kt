package com.ktpattern.patternmatch

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

private class RegexPattern(private val regex: Regex) : Pattern<Any> {
    override fun match(value: Any) = value is String && regex.matches(value)
    override fun getType(): Class<*> = RegexPattern::class.java
}

private class CustomPattern : Pattern<Any> {
    override fun match(value: Any) = value is String && value.startsWith("hey")
    override fun getType(): Class<*> = CustomPattern::class.java
}

private class RegexPatternEvaluator : PatternEvaluator<Any> {
    override fun supports(pattern: Pattern<*>) = pattern is RegexPattern
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        val regex = pattern as RegexPattern
        val result = regex.match(value)
        println("🔍 Regex evaluate: $value → $result")
        return if (result) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Regex mismatch")
    }
}

private class CustomPatternEvaluator : PatternEvaluator<Any> {
    override fun supports(pattern: Pattern<*>) = pattern is CustomPattern
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        val custom = pattern as CustomPattern
        return if (custom.match(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Custom pattern mismatch")
    }
}

class FullDslIntegrationTest {

    // 도메인 모델
    sealed class Animal
    data class Dog(val name: String, val age: Int) : Animal()
    data class Cat(val name: String) : Animal()
    data class Person(val name: String, val age: Int)
    data class Box<T>(val value: T)

    private val regexPattern = RegexPattern(Regex("^hello.*"))
    private val customPattern = CustomPattern()

    private fun buildEvaluator(): CompositeEvaluator<Any> {
        return CompositeEvaluator(listOf(
            RegexPatternEvaluator(),
            CustomPatternEvaluator()
        ))
    }

    @Test
    fun `should match value pattern`() {
        val builder = MatchBuilder<Any, String>(
            CompositeEvaluator(listOf(DefaultPatternEvaluator())),
            SnapshotBinder()
        )
        builder.case(ValuePattern("hello", String::class.java)) { "Matched!" }
        builder.else_ { "Nope" }

        val result = builder.evaluate("hello")
        assertEquals("Matched!", result)
    }

    @Test
    fun `should match type pattern`() {
        val builder = MatchBuilder<Any, String>(
            CompositeEvaluator(listOf(DefaultPatternEvaluator())),
            SnapshotBinder()
        )
        builder.case(TypePattern(String::class.java)) { "String matched" }
        builder.else_ { "No match" }

        val result = builder.evaluate("some text")
        assertEquals("String matched", result)
    }

    @Test
    fun `should call TypePattern-match directly`() {
        val pattern = TypePattern(String::class.java) { it.startsWith("x") }
        assertTrue(pattern.match("xyz"))
        assertFalse(pattern.match("hello"))
    }

    @Test
    fun `should match predicate condition`() {
        val builder = MatchBuilder<Any, String>(
            CompositeEvaluator(listOf(DefaultPatternEvaluator())),
            SnapshotBinder()
        )
        builder.case(PredicateCondition(String::class.java) { it == "target" }) {
            "Matched predicate"
        }
        builder.else_ { "No match" }

        assertEquals("Matched predicate", builder.evaluate("target"))
    }

    @Test
    fun `should use DSL caseOf extension`() {
        val result = match<Any, String>("hello") {
            caseOf<String, Any, String> { "got string" }
            else_ { "nope" }
        }
        assertEquals("got string", result)
    }

    @Test
    fun `should match destructure pattern`() {
        val builder = MatchBuilder<Any, String>(
            CompositeEvaluator(listOf(DefaultPatternEvaluator())),
            SnapshotBinder()
        )
        builder.case(DestructurePattern(Box::class.java) { (value): Box<*> -> value == "match" }) {
            "Matched destructure"
        }
        builder.else_ { "No match" }

        assertEquals("Matched destructure", builder.evaluate(Box("match")))
    }

    @Test
    fun `should match and pattern`() {
        val builder = MatchBuilder<Any, String>(
            CompositeEvaluator(listOf(DefaultPatternEvaluator())),
            SnapshotBinder()
        )
        val andPattern = AndPattern(listOf(
            TypePattern(String::class.java),
            PredicateCondition(String::class.java) { it == "match" }
        ))
        builder.case(andPattern) { "AND matched" }
        builder.else_ { "no match" }
        assertEquals("AND matched", builder.evaluate("match"))
    }

    @Test
    fun `should match or pattern`() {
        val builder = MatchBuilder<Any, String>(
            CompositeEvaluator(listOf(DefaultPatternEvaluator())),
            SnapshotBinder()
        )
        val orPattern = OrPattern(listOf(
            ValuePattern("A", String::class.java),
            ValuePattern("B", String::class.java)
        ))
        builder.case(orPattern) { "OR matched" }
        builder.else_ { "no match" }
        assertEquals("OR matched", builder.evaluate("B"))
    }


    @Test
    fun `should match using whenType DSL`() {
        val result = match<Any, String>("hello") {
            whenType<String, Any, String> { "yes" }
            else_ { "no" }
        }
        assertEquals("yes", result)
    }

    @Test
    fun `should match Dog using whenType`() {
        val result = match<Animal, String>(Dog("멍멍이", 3)) {
            whenType<Dog, Animal, String> { dog -> "강아지: ${dog.name}" }
            else_ { "다른 동물" }
        }
        assertEquals("강아지: 멍멍이", result)
    }

    @Test
    fun `should match Cat using caseOf`() {
        val result = match<Animal, String>(Cat("야옹이")) {
            caseOf<Cat, Animal, String>({ it.name == "야옹이" }) { "고양이: ${it.name}" }
            else_ { "모름" }
        }
        assertEquals("고양이: 야옹이", result)
    }

    @Test
    fun `should match using whenValue DSL`() {
        val result = match("target") {
            whenValue("target") { "value ok" }
            else_ { "fail" }
        }
        assertEquals("value ok", result)
    }

    @Test
    fun `should match RegexPattern`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(regexPattern) { "Matched regex" }
        builder.else_ { "No match" }

        val result = builder.evaluate("hello world")
        assertEquals("Matched regex", result)
    }

    @Test
    fun `should match custom pattern`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(customPattern) { "Custom match!" }
        builder.else_ { "No match" }

        val result = builder.evaluate("hey!")
        assertEquals("Custom match!", result)
    }

    @Test
    fun `should fallback to else`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(regexPattern) { "Matched regex" }
        builder.else_ { "No match" }

        val result = builder.evaluate("bye")
        assertEquals("No match", result)
    }

    @Test
    fun `should record matched and not matched snapshots`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(regexPattern) { "Regex" }
        builder.case(customPattern) { "Custom" }
        builder.else_ { "Else" }

        builder.evaluate("hello world")
        builder.evaluate("hey!")
        builder.evaluate("xyz")

        val logs = binder.getAll()
        logs.forEach { println("→ ${it.status} | ${it.value} | ${it.pattern}") }

        val matched = logs.count { it.status is SnapshotStatus.Matched }
        val notMatched = logs.count { it.status is SnapshotStatus.NotMatched }

        println("Matched: $matched / NotMatched: $notMatched")
        assertTrue(matched >= 2)
        assertTrue(notMatched >= 1)
    }

    @Test
    fun `should match Person with complex composite pattern`() {
        val builder = MatchBuilder<Any, String>(
            CompositeEvaluator(listOf(DefaultPatternEvaluator())),
            SnapshotBinder()
        )

        val premiumPattern = AndPattern(
            listOf(
                TypePattern(Person::class.java),
                PredicateCondition(Person::class.java) { it.name == "홍길동" },
                PredicateCondition(Person::class.java) { it.age >= 30 }
            )
        )

        val youngAdultPattern = AndPattern(
            listOf(
                TypePattern(Person::class.java),
                PredicateCondition(Person::class.java) { it.age in 20..39 }
            )
        )

        val seniorPattern = AndPattern(
            listOf(
                TypePattern(Person::class.java),
                PredicateCondition(Person::class.java) { it.age >= 60 }
            )
        )

        val orPattern = OrPattern(
            listOf(
                premiumPattern,
                youngAdultPattern,
                seniorPattern
            )
        )

        builder.case(orPattern) { value ->
            val person = value
            when {
                person.name == "홍길동" && person.age >= 30 -> "프리미엄 사용자"
                person.age >= 60 -> "시니어 사용자"
                person.age in 20..39 -> "청년 사용자"
                else -> "조건 매칭 실패"
            }
        }

        builder.else_ { "기타" }

        assertEquals("프리미엄 사용자", builder.evaluate(Person("홍길동", 35)))
        assertEquals("청년 사용자", builder.evaluate(Person("김영희", 28)))
        assertEquals("시니어 사용자", builder.evaluate(Person("고길동", 65)))
        assertEquals("기타", builder.evaluate("Just a String"))
    }
}
