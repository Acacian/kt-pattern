package com.ktpattern.patternmatch

import com.ktpattern.patternmatch.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class ExampleWebApplication

fun main(args: Array<String>) {
    runApplication<ExampleWebApplication>(*args)
}

@RestController
@RequestMapping("/api/dsl")
class ExampleWebController(
    private val dslService: ExampleWebService
) {
    @PostMapping("/evaluate")
    fun evaluate(@RequestBody request: MatchRequest): ResponseEntity<String> {
        val result = dslService.evaluate(request.input)
        return ResponseEntity.ok(result)
    }
}

data class MatchRequest(val input: Any)

@Service
class ExampleWebService {
    fun evaluate(input: Any): String {
        return match<Any, String>(input) {
            whenValue("hello") { "Matched exact string: hello" }
            whenValue(123) { "Matched exact int: 123" }
            whenType<String, Any, String> { "It's a string: $it" }
            whenType<Map<*, *>, Any, String> { "It's a map: $it" }

            val customPattern = object : Pattern<Any> {
                override fun match(value: Any): Boolean =
                    value is String && value.lowercase(Locale.getDefault()).startsWith("he")
                override fun getType(): Class<*> = String::class.java
            }
            case(customPattern) { "Custom pattern matched: $it" }

            else_ { "No match for: $input" }
        }
    }
}