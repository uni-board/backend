package com.uniboard.storage.domain

import java.io.InputStream

interface StorageRepository {
    fun put(stream: InputStream): Long

    fun get(id: Long): InputStream

    fun fileExists(id: Long): Boolean
}
