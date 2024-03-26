package com.uniboard.board.domain

interface BoardRepository {
    fun all(boardId: Long): List<BoardObject>

    fun get(boardId: Long, id: Long): String

    fun add(boardId: Long, data: String): Long

    fun delete(boardId: Long, id: Long)

    fun set(boardId: Long, id: Long, data: String)
}