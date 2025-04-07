package com.ktpattern.patternmatch

interface Pattern {
    fun match(value: Any?): MatchResult
}
