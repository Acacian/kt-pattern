package com.ktpattern.patternmatch

sealed class Animal
data class Dog(val name: String, val age: Int) : Animal()
data class Cat(val name: String) : Animal()
data class Person(val name: String, val age: Int)
data class Box<T>(val value: T)

fun main() {
    val binder = SnapshotBinder()

    val inputs = listOf<Any>(
        "hello",
        123,
        Person("Alice", 30),
        Dog("Bobby", 5),
        Cat("Kitty"),
        Box(42)
    )

    // 커스텀 패턴: "he"로 시작하는 문자열
    val startsWithHe = object : Pattern<Any> {
        override fun match(value: Any): Boolean =
            value is String && value.startsWith("he")
        override fun getType(): Class<*> = String::class.java
    }

    for (input in inputs) {
        val result = match(input, snapshotBinder = binder) {
            // 1. 정확한 값
            whenValue(123) { "🎯 정확히 123입니다" }

            // 2. 타입 기반 매칭
            whenType<String> { "🔤 문자열: $it" }

            // 3. 커스텀 조건 기반
            caseOf<Person>({ it.age > 18 }) { "👩 어른: ${it.name}" }

            caseOf<Box<Int>>({ it.value > 10 }) { "📦 10보다 큰 정수: ${it.value}" }

            caseOf<Dog>({ it.age < 10 }) { "🐶 어린 강아지: ${it.name}" }

            whenType<Animal> { "🦁 동물입니다: $it" }

            // 4. 커스텀 패턴
            case(startsWithHe) { "💡 'he'로 시작하는 문자열: $it" }

            // 5. fallback
            else_ { "❓ 매칭 실패: $it" }
        }

        println("▶ 입력: $input → 결과: $result")
    }

    println("\n📋 Snapshot 로그")
    binder.getAll().forEachIndexed { index, snap ->
        println("[$index] ${snap.status} | ${snap.value} | ${snap.pattern}")
    }
}
