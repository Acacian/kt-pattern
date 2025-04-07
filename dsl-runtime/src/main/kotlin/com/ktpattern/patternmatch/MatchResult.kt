package com.ktpattern.patternmatch

sealed class MatchResult {
    data class Success(val value: Any) : MatchResult()
    data class Failure(val reason: String) : MatchResult()

    fun isSuccess(): Boolean = this is Success
}
