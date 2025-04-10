package com.ktpattern.patternmatch

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleWebApiTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun post(input: Any): String =
        restTemplate.postForObject("http://localhost:$port/api/dsl/evaluate", mapOf("input" to input), String::class.java)

    @Test
    fun `hello는 정확히 매칭됨`() {
        val result = post("hello")
        assertEquals("Matched exact string: hello", result)
    }

    @Test
    fun `정수 123은 정확히 매칭됨`() {
        val result = post(123)
        assertEquals("Matched exact int: 123", result)
    }

    @Test
    fun `커스텀 패턴 - hello로 시작`() {
        val customPattern = object : Pattern<Any> {
            override fun match(value: Any): Boolean =
                value is String && value.startsWith("he")

            override fun getType(): Class<*> = String::class.java
        }
        val result = post("hello123")
        assertEquals("Matched pattern: hello123", result)
    }

    @Test
    fun `일반 문자열은 타입 기반으로 매칭됨`() {
        val result = post("world")
        assertEquals("It's a string: world", result)
    }

    @Test
    fun `Map 타입은 타입 기반으로 매칭됨`() {
        val result = post(mapOf("key" to "value"))
        assertEquals("It's a map: {key=value}", result)
    }

    @Test
    fun `매칭 안 되는 입력은 No match`() {
        val result = post(true)
        assertEquals("No match for: true", result)
    }
}
