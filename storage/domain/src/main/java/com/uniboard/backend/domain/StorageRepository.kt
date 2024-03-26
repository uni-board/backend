package com.uniboard.backend.domain

import java.io.InputStream

interface StorageRepository {
    fun put(boardId: Long, stream: InputStream): Long

    fun get(boardId: Long, id: Long): InputStream
}
