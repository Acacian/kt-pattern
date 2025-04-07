package com.ktpattern.patternmatch

data class Snapshot(val data: Any)

sealed class MatchResult {
    data class Success(val value: Any, val snapshot: Snapshot? = null) : MatchResult()
    data class Failure(val reason: String, val value: Any? = null) : MatchResult()

    // 성공 여부 확인
    fun isSuccess(): Boolean = this is Success
}