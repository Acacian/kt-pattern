package com.koodongha.patternmatch

data class PredicateCondition(
    val predicate: (Any?) -> Boolean
) : Pattern {
    override fun match(value: Any?): MatchResult {
        return if (predicate(value)) {
            MatchResult.Success()
        } else {
            MatchResult.Failure
        }
    }
}
