package com.ktpattern.patternmatch

sealed class PatternMatchResult {
    data class Success(val value: Any /* , val snapshot: Snapshot? = null */) : PatternMatchResult()
    data class Failure(val reason: String) : PatternMatchResult()

    fun isSuccess(): Boolean = this is Success
}