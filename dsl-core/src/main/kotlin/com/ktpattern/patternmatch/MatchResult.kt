package com.ktpattern.patternmatch

sealed class MatchResult {
    data class Success(val bindings: Map<String, Any?> = emptyMap()) : MatchResult()
    object Failure : MatchResult()

    fun isSuccess() = this is Success
    fun isFailure() = this is Failure
}