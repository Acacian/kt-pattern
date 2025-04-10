import com.ktpattern.patternmatch.*

sealed class Animal
data class Dog(val name: String, val age: Int) : Animal()
data class Cat(val name: String) : Animal()
object UnknownAnimal : Animal()

data class Person(val name: String, val age: Int)
data class Box<T>(val value: T)

fun main() {
    val inputs = listOf<Any>(
        "hello",
        123,
        Person("Alice", 30),
        Dog("Bobby", 5),
        Cat("Kitty"),
        Box(42)
    )

    for (input in inputs) {
        val result = match<Any, String>(input) {
            // ✅ 타입 기반 매칭
            whenType<String, Any, String> { "It's a String: $input" }

            // ✅ 값 기반 매칭
            whenValue(123) { "Matched exact value: 123" }

            // ✅ 구조 분해 매칭 (Destructuring)
            caseOf<Person, Any, String>({ it.age > 18 }) { "Adult person: ${it.name}" }

            // ✅ 조건부 매칭 (`caseOf` + predicate)
            caseOf<Box<Int>, Any, String>({ it.value > 10 }) { "Boxed int > 10: ${it.value}" }

            // ✅ 스마트 캐스트 연계
            caseOf<Dog, Any, String>({ it.age < 10 }) { "Young dog: ${it.name}" }

            // ✅ Sealed class 매칭
            whenType<Animal, Any, String> { "Some kind of animal: $input" }

            // ✅ 커스텀 패턴 정의
            val custom = object : Pattern<Any> {
                override fun match(value: Any): Boolean =
                    value is String && value.startsWith("he")
                override fun getType(): Class<*> = String::class.java
            }
            case(custom) { "Custom pattern matched: $input" }

            else_ { "No match for: $input" }
        }

        println("Input: $input → Result: $result")
    }
}
