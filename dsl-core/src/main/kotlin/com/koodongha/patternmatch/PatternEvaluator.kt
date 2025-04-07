package com.koodongha.patternmatch

interface PatternEvaluator {
    fun evaluate(pattern: Pattern, input: Any?): MatchResult
}
