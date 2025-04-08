import com.ktpattern.patternmatch.dsl.match
import com.ktpattern.patternmatch.dsl.whenType
import com.ktpattern.patternmatch.dsl.whenValue
import com.ktpattern.patternmatch.dsl.else_

data class Person(val name: String, val age: Int)

fun main() {
    val result = match<Person, String>(Person("Alice", 30)) {
        whenValue(Person("Bob", 40)) { "It's Bob!" }
        whenType<Person> { "It's a Person!" }
        else_ { "No match." }
    }

    println("Matched result: $result")
}
