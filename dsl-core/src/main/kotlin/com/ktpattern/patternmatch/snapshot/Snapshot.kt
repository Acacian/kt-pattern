package com.ktpattern.patternmatch

sealed class SnapshotStatus {
    object Matched : SnapshotStatus()
    object NotMatched : SnapshotStatus()
    object Error : SnapshotStatus()
}

data class Snapshot(
    val value: Any,
    val pattern: String,
    val status: SnapshotStatus = SnapshotStatus.Matched,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, Any> = emptyMap()
)
