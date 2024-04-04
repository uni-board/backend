package com.uniboard.storage.domain

interface StorageDB {
    fun add(): String

    fun fileExists(id: String): Boolean

    fun delete(id: String)
}
