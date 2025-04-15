package com.ktpattern.patternmatch

// ✅ 도메인 모델
sealed class Animal
data class Dog(val name: String, val age: Int) : Animal()
data class Cat(val name: String) : Animal()
data class Person(val name: String, val age: Int)
data class Box<T>(val value: T)

// ✅ Custom Pattern + Evaluator
class RegexPattern(private val regex: Regex) : Pattern<Any> {
    override fun match(value: Any): Boolean = value is String && regex.matches(value)
    override fun getType(): Class<*> = RegexPattern::class.java
}

class CustomPattern : Pattern<Any> {
    override fun match(value: Any): Boolean = value is String && value.startsWith("hey")
    override fun getType(): Class<*> = CustomPattern::class.java
}

class RegexPatternEvaluator : PatternEvaluator<Any> {
    override fun supports(pattern: Pattern<*>): Boolean = pattern is RegexPattern
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        val regex = pattern as RegexPattern
        return if (regex.match(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Regex mismatch")
    }
}

class CustomPatternEvaluator : PatternEvaluator<Any> {
    override fun supports(pattern: Pattern<*>): Boolean = pattern is CustomPattern
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        val custom = pattern as CustomPattern
        return if (custom.match(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Custom mismatch")
    }
}

// ✅ Main 실행
fun main() {
    val binder = SnapshotBinder()

    // ✅ Evaluator에 모두 등록
    val evaluator = CompositeEvaluator(
        listOf(
            DefaultPatternEvaluator(),
            RegexPatternEvaluator(),
            CustomPatternEvaluator()
        )
    )

    val inputs = listOf<Any>(
        Person("홍길동", 35),
        Person("김영희", 28),
        Person("고길동", 65),
        Dog("뽀삐", 3),
        Cat("나비"),
        Box("hello"),
        Box(999),
        "hello world",
        "hey there",
        123
    )

    // ✅ 외부 선언 패턴
    val regexPattern = RegexPattern(Regex("^hello.*"))
    val customPattern = CustomPattern()

    for (input in inputs) {
        val builder = MatchBuilder<Any, String>(evaluator, binder)

        // ✅ 복합 AND 패턴
        builder.case(
            AndPattern(
                listOf(
                    TypePattern(Person::class.java),
                    PredicateCondition(Person::class.java) { it.name == "홍길동" },
                    PredicateCondition(Person::class.java) { it.age >= 30 }
                )
            )
        ) {
            "💎 프리미엄 사용자 (홍길동): $it"
        }

        // ✅ DSL 패턴들
        builder.caseOf<Person, Any, String>({ it.age in 20..39 }) {
            "🧑 청년 사용자: ${it.name}"
        }

        builder.caseOf<Person, Any, String>({ it.age >= 60 }) {
            "👴 시니어 사용자: ${it.name}"
        }

        builder.whenType<Dog, Any, String> {
            "🐶 강아지: ${it.name} (${it.age}살)"
        }

        builder.whenType<Cat, Any, String> {
            "🐱 고양이: ${it.name}"
        }

        builder.whenValue("hello world") {
            "🧾 정확히 'hello world' 문자열입니다"
        }

        builder.caseOf<Box<String>, Any, String>({ it.value.startsWith("he") }) {
            "📦 Boxed String: ${it.value}"
        }

        builder.caseOf<Box<Int>, Any, String>({ it.value > 100 }) {
            "📦 Boxed Int > 100: ${it.value}"
        }

        // ✅ 커스텀 패턴 적용
        builder.case(regexPattern) { "🔍 RegexPattern 매칭: $it" }
        builder.case(customPattern) { "🎯 CustomPattern 매칭: $it" }

        // ✅ fallback
        builder.else_ {
            "🤷‍♀️ 매칭되지 않음: $input"
        }

        // ✅ 실행 및 출력
        val result = builder.evaluate(input)
        println("▶ 입력: $input → 결과: $result")
    }

    println("\n📋 Snapshot 기록")
    binder.getAll().forEachIndexed { idx, snap ->
        println("[$idx] ${snap.status} | ${snap.value} | ${snap.pattern}")
    }
}
