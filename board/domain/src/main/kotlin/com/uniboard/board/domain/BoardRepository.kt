package com.uniboard.board.domain

interface BoardRepository {
    suspend fun all(boardId: Long): List<String>

    suspend fun get(boardId: Long, id: Long): String

    suspend fun add(boardId: Long, data: String): Long

    suspend fun delete(boardId: Long, id: Long)

    suspend fun set(boardId: Long, id: Long, data: String)
}