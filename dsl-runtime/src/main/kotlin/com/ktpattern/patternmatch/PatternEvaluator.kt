package com.ktpattern.patternmatch

interface PatternEvaluator<T> {
    fun evaluate(pattern: Pattern<T>, value: T): MatchResult
}
