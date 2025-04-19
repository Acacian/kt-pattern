package com.ktpattern.patternmatch

interface CustomPattern<T> : Pattern<T> {
    fun evaluate(value: T): PatternMatchResult
}