package com.ktpattern.patternmatch

class DefaultPatternEvaluator : PatternEvaluator<Any> {
    override fun evaluate(pattern: Pattern<Any>, value: Any): MatchResult {
        return when (pattern) {
            is TypePattern -> evaluateType(pattern, value)
            is ValuePattern -> evaluateValue(pattern, value)
            is DestructurePattern -> evaluateDestructure(pattern, value)
            is PredicateCondition -> evaluatePredicate(pattern, value)
            else -> MatchResult.Failure("Unknown pattern type")
        }
    }

    private fun evaluateType(pattern: TypePattern, value: Any): MatchResult {
        return if (pattern.type.isInstance(value)) {
            MatchResult.Success(value)
        } else {
            MatchResult.Failure("Type mismatch")
        }
    }

    private fun evaluateValue(pattern: ValuePattern, value: Any): MatchResult {
        return if (pattern.value == value) {
            MatchResult.Success(value)
        } else {
            MatchResult.Failure("Value mismatch")
        }
    }

    private fun evaluateDestructure(pattern: DestructurePattern, value: Any): MatchResult {
        // Destructure matching logic
        // Assuming value is a destructurable type, like a tuple or a data class
        return MatchResult.Success(value)
    }

    private fun evaluatePredicate(pattern: PredicateCondition, value: Any): MatchResult {
        return if (pattern.predicate(value)) {
            MatchResult.Success(value)
        } else {
            MatchResult.Failure("Predicate condition failed")
        }
    }
}
