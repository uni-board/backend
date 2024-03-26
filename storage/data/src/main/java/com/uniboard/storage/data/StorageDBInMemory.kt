package com.uniboard.storage.data

import com.uniboard.storage.domain.StorageDB

class StorageDBInMemory: StorageDB {
    private val files = mutableSetOf<Long>()
    override fun add(): Long {
        val lastId = files.lastOrNull() ?: -1
        files.add(lastId + 1)
        return lastId + 1
    }

    override fun fileExists(id: Long): Boolean {
        return id in files
    }

    override fun delete(id: Long) {
        files.remove(id)
    }
}