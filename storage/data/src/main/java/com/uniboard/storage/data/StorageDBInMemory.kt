package com.uniboard.storage.data

import com.uniboard.storage.domain.StorageDB
import java.util.UUID

class StorageDBInMemory: StorageDB {
    private val files = mutableSetOf<String>()
    override fun add(): String {
        val uuid = UUID.randomUUID().toString()
        files.add(uuid)
        return uuid
    }

    override fun fileExists(id: String): Boolean {
        return id in files
    }

    override fun delete(id: String) {
        files.remove(id)
    }
}