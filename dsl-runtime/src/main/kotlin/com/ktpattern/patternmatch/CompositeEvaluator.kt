package com.ktpattern.patternmatch

class CompositeEvaluator<T>(
    private val delegates: List<PatternEvaluator<T>>
) : PatternEvaluator<T> {

    override fun supports(pattern: Pattern<*>): Boolean = true

    override fun evaluate(pattern: Pattern<T>, value: T): PatternMatchResult {
        val evaluator = delegates.firstOrNull { it.supports(pattern) }
            ?: error("❌ No evaluator supports this pattern: $pattern")

        return evaluator.evaluate(pattern, value)
    }
}
