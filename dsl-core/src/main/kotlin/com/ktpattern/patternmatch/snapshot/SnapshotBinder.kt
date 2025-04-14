package com.ktpattern.patternmatch

class SnapshotBinder {
    private val snapshots = mutableMapOf<String, Snapshot>()

    fun saveSnapshot(identifier: String, snapshot: Snapshot) {
        snapshots[identifier] = snapshot
    }

    fun getAll(): Map<String, Snapshot> {
        return snapshots
    }
}
