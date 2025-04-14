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

    for (input in inputs) {
        val result = match<Any, String>(input, snapshotBinder = binder) {
            // ✅ 제네릭 명시 (TSub, T, R)
            whenType<String, Any, String> { "It's a String: $it" }

            whenValue<Int, Any, String>(123) { "Matched exact value: 123" }

            caseOf<Person, Any, String>({ it.age > 18 }) { "Adult person: ${it.name}" }

            caseOf<Box<Int>, Any, String>({ it.value > 10 }) { "Boxed int > 10: ${it.value}" }

            caseOf<Dog, Any, String>({ it.age < 10 }) { "Young dog: ${it.name}" }

            whenType<Animal, Any, String> { "Some kind of animal: $it" }

            // ✅ 커스텀 패턴
            val custom = object : Pattern<Any> {
                override fun match(value: Any): Boolean =
                    value is String && value.startsWith("he")

                override fun getType(): Class<*> = String::class.java
            }
            case<Any>(custom) { value -> "Custom pattern matched: $value" }

            else_ { "No match for: $input" }
        }

        println("Input: $input → Result: $result")
    }

    println("\n🧾 Snapshot Log")
    println("------------------------")
    binder.getAll().forEachIndexed { index, snap ->
        println("[$index] ${snap.status} | ${snap.value} | ${snap.timestamp}")
    }
}
