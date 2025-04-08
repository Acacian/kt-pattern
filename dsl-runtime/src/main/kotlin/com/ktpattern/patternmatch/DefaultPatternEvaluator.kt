package com.ktpattern.patternmatch

internal class DefaultPatternEvaluator : PatternEvaluator<Any> {
    @Suppress("UNCHECKED_CAST")
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        return when (pattern) {
            is TypePattern<*> -> evaluateType(pattern as TypePattern<Any>, value)
            is ValuePattern<*> -> evaluateValue(pattern as ValuePattern<Any>, value)
            is DestructurePattern<*> -> evaluateDestructure(pattern as DestructurePattern<Any>, value)
            is PredicateCondition<*> -> evaluatePredicate(pattern as PredicateCondition<Any>, value)
            else -> PatternMatchResult.Failure("Unknown pattern type")
        }
    }

    private fun evaluateType(pattern: TypePattern<Any>, value: Any): PatternMatchResult {
        return if (pattern.targetType.isInstance(value)) {
            PatternMatchResult.Success(value)
        } else {
            PatternMatchResult.Failure("Type mismatch")
        }
    }

    private fun evaluateValue(pattern: ValuePattern<Any>, value: Any): PatternMatchResult {
        return if (pattern.value == value && pattern.predicate(value)) {
            PatternMatchResult.Success(value)
        } else {
            PatternMatchResult.Failure("Value mismatch")
        }
    }

    private fun evaluateDestructure(pattern: DestructurePattern<Any>, value: Any): PatternMatchResult {
        return if (pattern.match(value)) {
            PatternMatchResult.Success(value)
        } else {
            PatternMatchResult.Failure("Destructure failed")
        }
    }

    private fun evaluatePredicate(pattern: PredicateCondition<Any>, value: Any): PatternMatchResult {
        return if (pattern.predicate(value)) {
            PatternMatchResult.Success(value)
        } else {
            PatternMatchResult.Failure("Predicate condition failed")
        }
    }
}
