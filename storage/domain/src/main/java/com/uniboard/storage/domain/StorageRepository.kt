package com.uniboard.storage.domain

import java.io.InputStream

interface StorageRepository {
    fun put(stream: InputStream): String

    fun get(id: String): InputStream

    fun fileExists(id: String): Boolean
}
