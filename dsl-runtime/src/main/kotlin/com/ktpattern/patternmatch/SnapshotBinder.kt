package com.ktpattern.patternmatch

class SnapshotBinder<T> {
    private val snapshots = mutableMapOf<String, T>()

    fun saveSnapshot(identifier: String, value: T) {
        snapshots[identifier] = value
    }

    fun getSnapshot(identifier: String): T? {
        return snapshots[identifier]
    }
}
