package com.ktpattern.patternmatch

@Suppress("unused") // ServiceLoader에서 동적으로 로드됩니다
class PatternEvaluator : Evaluator<Any> {

    override fun supports(pattern: Pattern<*>): Boolean {
        return pattern is TypePattern<*> ||
                pattern is ValuePattern<*> ||
                pattern is DestructurePattern<*> ||
                pattern is PredicateCondition<*> ||
                pattern is AndPattern<*> ||
                pattern is OrPattern<*> ||
                pattern is CustomPattern<*>
    }

    @Suppress("UNCHECKED_CAST")
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        return when (pattern) {
            is CustomPattern<*> -> (pattern as CustomPattern<Any>).evaluate(value)
            is TypePattern<*> -> evaluateType(pattern as TypePattern<Any>, value)
            is ValuePattern<*> -> evaluateValue(pattern as ValuePattern<Any>, value)
            is DestructurePattern<*> -> evaluateDestructure(pattern as DestructurePattern<Any>, value)
            is PredicateCondition<*> -> evaluatePredicate(pattern as PredicateCondition<Any>, value)
            is AndPattern<*> -> evaluateAnd(pattern as AndPattern<Any>, value)
            is OrPattern<*> -> evaluateOr(pattern as OrPattern<Any>, value)
            else -> PatternMatchResult.Failure("❌ Unknown pattern type: ${pattern::class.simpleName}")
        }
    }

    private fun evaluateType(pattern: TypePattern<Any>, value: Any): PatternMatchResult =
        if (pattern.targetType.isInstance(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Type mismatch")

    private fun evaluateValue(pattern: ValuePattern<Any>, value: Any): PatternMatchResult =
        if (pattern.value == value && pattern.predicate(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Value mismatch")

    private fun evaluateDestructure(pattern: DestructurePattern<Any>, value: Any): PatternMatchResult =
        if (pattern.match(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Destructure failed")

    private fun evaluatePredicate(pattern: PredicateCondition<Any>, value: Any): PatternMatchResult =
        if (pattern.predicate(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Predicate condition failed")

    private fun evaluateAnd(pattern: AndPattern<Any>, value: Any): PatternMatchResult =
        if (pattern.patterns.all { evaluate(it, value).isSuccess() }) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("And pattern failed")

    private fun evaluateOr(pattern: OrPattern<Any>, value: Any): PatternMatchResult =
        if (pattern.patterns.any { evaluate(it, value).isSuccess() }) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Or pattern failed")
}
