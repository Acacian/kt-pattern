package com.koodongha.patternmatch

interface Pattern {
    fun match(value: Any?): MatchResult
}
