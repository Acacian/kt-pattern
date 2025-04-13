package com.ktpattern.patternmatch

interface PatternEvaluator<T> {
    fun supports(pattern: Pattern<*>): Boolean
    fun evaluate(pattern: Pattern<T>, value: T): PatternMatchResult
}