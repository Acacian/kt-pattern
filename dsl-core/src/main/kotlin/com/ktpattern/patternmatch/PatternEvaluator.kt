package com.ktpattern.patternmatch

interface PatternEvaluator<T> {
    fun evaluate(pattern: Pattern<T>, input: T?): MatchResult
}
