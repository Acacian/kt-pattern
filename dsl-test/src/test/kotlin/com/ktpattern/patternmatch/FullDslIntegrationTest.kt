package com.ktpattern.patternmatch

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FullDslIntegrationTest {

    sealed class Animal
    data class Dog(val name: String, val age: Int) : Animal()
    data class Cat(val name: String) : Animal()
    data class Person(val name: String, val age: Int)
    data class Box<T>(val value: T)

    private class RegexPattern : SimplePattern<String>(String::class.java) {
        override fun matches(value: String): Boolean = value.startsWith("hello")
    }

    private class CustomPattern : SimplePattern<String>(String::class.java) {
        override fun matches(value: String): Boolean = value.startsWith("hey")
    }

    private val regexPattern = RegexPattern()
    private val customPattern = CustomPattern()

    private fun evaluate(value: Any, binder: SnapshotBinder? = null, block: MatchBuilder.() -> Unit): String =
        match(value, binder, block)

    @Test
    fun `should match value pattern`() {
        val result = evaluate("hello") {
            whenValue("hello") { "Matched!" }
            else_ { "Nope" }
        }
        assertEquals("Matched!", result)
    }

    @Test
    fun `should match type pattern`() {
        val result = evaluate("some text") {
            whenType<String> { "String matched" }
            else_ { "No match" }
        }
        assertEquals("String matched", result)
    }

    @Test
    fun `should match predicate condition`() {
        val result = evaluate("target") {
            case(PredicateCondition(String::class.java) { it == "target" }) { "Matched predicate" }
            else_ { "No match" }
        }
        assertEquals("Matched predicate", result)
    }

    @Test
    fun `should use DSL caseOf extension`() {
        val result = evaluate("hello") {
            caseOf<String> { "got string" }
            else_ { "nope" }
        }
        assertEquals("got string", result)
    }

    @Test
    fun `should match destructure pattern`() {
        val result = evaluate(Box("match")) {
            case(DestructurePattern(Box::class.java) { (value): Box<*> -> value == "match" }) {
                "Matched destructure"
            }
            else_ { "No match" }
        }
        assertEquals("Matched destructure", result)
    }

    @Test
    fun `should match and pattern`() {
        val result = evaluate("match") {
            case(AndPattern(listOf(
                TypePattern(String::class.java),
                PredicateCondition(String::class.java) { it == "match" }
            ))) { "AND matched" }
            else_ { "no match" }
        }
        assertEquals("AND matched", result)
    }

    @Test
    fun `should match or pattern`() {
        val result = evaluate("B") {
            case(OrPattern(listOf(
                ValuePattern("A", String::class.java),
                ValuePattern("B", String::class.java)
            ))) { "OR matched" }
            else_ { "no match" }
        }
        assertEquals("OR matched", result)
    }

    @Test
    fun `should match Dog using whenType`() {
        val result = evaluate(Dog("멍멍이", 3)) {
            whenType<Dog> { "강아지: ${it.name}" }
            else_ { "다른 동물" }
        }
        assertEquals("강아지: 멍멍이", result)
    }

    @Test
    fun `should match Cat using caseOf`() {
        val result = evaluate(Cat("야옹이")) {
            caseOf<Cat>({ it.name == "야옹이" }) { "고양이: ${it.name}" }
            else_ { "모름" }
        }
        assertEquals("고양이: 야옹이", result)
    }

    @Test
    fun `should match RegexPattern`() {
        val result = evaluate("hello world") {
            case(regexPattern) { "Matched regex" }
            else_ { "No match" }
        }
        assertEquals("Matched regex", result)
    }

    @Test
    fun `should match custom pattern`() {
        val result = evaluate("hey!") {
            case(customPattern) { "Custom match!" }
            else_ { "No match" }
        }
        assertEquals("Custom match!", result)
    }

    @Test
    fun `should fallback to else`() {
        val result = evaluate("bye") {
            case(regexPattern) { "Matched regex" }
            else_ { "No match" }
        }
        assertEquals("No match", result)
    }

    @Test
    fun `should record matched and not matched snapshots`() {
        val binder = SnapshotBinder()
        evaluate("hello world", binder) {
            case(regexPattern) { "Regex" }
            case(customPattern) { "Custom" }
            else_ { "Else" }
        }
        evaluate("hey!", binder) {
            case(regexPattern) { "Regex" }
            case(customPattern) { "Custom" }
            else_ { "Else" }
        }
        evaluate("xyz", binder) {
            case(regexPattern) { "Regex" }
            case(customPattern) { "Custom" }
            else_ { "Else" }
        }

        val logs = binder.getAll()
        val matched = logs.count { it.status is SnapshotStatus.Matched }
        val notMatched = logs.count { it.status is SnapshotStatus.NotMatched }
        assertTrue(matched >= 2)
        assertTrue(notMatched >= 1)
    }

    @Test
    fun `should match sealed class Animal`() {
        val result = evaluate(Dog("멍멍이", 5)) {
            whenType<Animal> { "동물: $it" }
            else_ { "몰라요" }
        }
        assertEquals("동물: Dog(name=멍멍이, age=5)", result)
    }

    @Test
    fun `should match Person with complex composite pattern`() {
        val result = evaluate(Person("홍길동", 35)) {
            case(OrPattern(listOf(
                AndPattern(listOf(
                    TypePattern(Person::class.java),
                    PredicateCondition(Person::class.java) { it.name == "홍길동" },
                    PredicateCondition(Person::class.java) { it.age >= 30 }
                )),
                AndPattern(listOf(
                    TypePattern(Person::class.java),
                    PredicateCondition(Person::class.java) { it.age in 20..39 }
                )),
                AndPattern(listOf(
                    TypePattern(Person::class.java),
                    PredicateCondition(Person::class.java) { it.age >= 60 }
                ))
            ))) {
                val person = it
                when {
                    person.name == "홍길동" && person.age >= 30 -> "프리미엄 사용자"
                    person.age >= 60 -> "시니어 사용자"
                    person.age in 20..39 -> "청년 사용자"
                    else -> "조건 매칭 실패"
                }
            }
            else_ { "기타" }
        }
        assertEquals("프리미엄 사용자", result)
    }
}
