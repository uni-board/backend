package com.uniboard.storage.domain

interface StorageDB {
    fun add(): Long

    fun fileExists(id: Long): Boolean

    fun delete(id: Long)
}
