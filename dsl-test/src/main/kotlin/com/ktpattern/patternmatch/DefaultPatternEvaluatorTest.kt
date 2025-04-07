package com.ktpattern.patternmatch

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DefaultPatternEvaluatorTest {

    private val evaluator = DefaultPatternEvaluator()

    @Test
    fun `test evaluate TypePattern`() {
        val result = evaluator.evaluate(TypePattern(Int::class), 42)
        assertEquals(result, MatchResult.Success(42))
    }

    @Test
    fun `test evaluate ValuePattern`() {
        val result = evaluator.evaluate(ValuePattern(42), 42)
        assertEquals(result, MatchResult.Success(42))
    }

    @Test
    fun `test evaluate PredicateCondition`() {
        val result = evaluator.evaluate(PredicateCondition { it is String && it.length > 3 }, "Hello")
        assertEquals(result, MatchResult.Success("Hello"))
    }

    // 추가적인 테스트 케이스들...
}
