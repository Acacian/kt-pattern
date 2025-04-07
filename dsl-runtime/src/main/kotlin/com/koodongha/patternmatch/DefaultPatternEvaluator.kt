package com.koodongha.patternmatch

class DefaultPatternEvaluator : PatternEvaluator {
    override fun evaluate(pattern: Pattern, input: Any?): MatchResult {
        return pattern.match(input)
    }
}
