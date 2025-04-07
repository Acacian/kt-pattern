package com.ktpattern.patternmatch

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DslIntegrationTest {

    @Test
    fun `test match with multiple cases`() {
        val result = match(42) {
            case<TypePattern> { it > 40 } -> "Greater than 40"
            case<ValuePattern> { it == 42 } -> "The Answer"
            else -> "Unknown"
        }
        assertEquals("The Answer", result)
    }
}
