package com.ktpattern.patternmatch

data class ValuePattern(
    val expected: Any?
) : Pattern {
    override fun match(value: Any?): MatchResult {
        return if (value == expected) {
            MatchResult.Success()
        } else {
            MatchResult.Failure
        }
    }
}
