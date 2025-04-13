package com.ktpattern.patternmatch

class CompositeEvaluator(
    private val delegates: List<PatternEvaluator<Any>>
) : PatternEvaluator<Any> {
    override fun supports(pattern: Pattern<*>): Boolean = true

    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        val evaluator = delegates.firstOrNull { it.supports(pattern) }
            ?: error("❌ No evaluator supports this pattern: $pattern")
        return evaluator.evaluate(pattern, value)
    }
}
