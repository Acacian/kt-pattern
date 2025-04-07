package com.ktpattern.patternmatch

interface PatternEvaluator {
    fun evaluate(pattern: Pattern, input: Any?): MatchResult
}
