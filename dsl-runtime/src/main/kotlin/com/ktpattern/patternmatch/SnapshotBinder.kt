package com.ktpattern.patternmatch

class SnapshotBinder<T> {
    private val snapshots = mutableMapOf<String, Snapshot>()

    fun saveSnapshot(identifier: String, snapshot: Snapshot) {
        snapshots[identifier] = snapshot
    }

    fun getSnapshot(identifier: String): Snapshot? {
        return snapshots[identifier]
    }
}
