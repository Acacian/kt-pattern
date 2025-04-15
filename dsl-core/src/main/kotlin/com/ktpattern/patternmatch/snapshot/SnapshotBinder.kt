package com.ktpattern.patternmatch

class SnapshotBinder {
    private val snapshots = mutableListOf<Snapshot>()

    fun saveSnapshot(snapshot: Snapshot) {
        snapshots.add(snapshot)
    }

    fun getAll(): List<Snapshot> = snapshots
}
