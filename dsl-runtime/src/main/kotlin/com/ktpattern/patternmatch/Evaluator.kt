package com.ktpattern.patternmatch

interface Evaluator<T> {
    fun supports(pattern: Pattern<*>): Boolean
    fun evaluate(pattern: Pattern<T>, value: T): PatternMatchResult
}