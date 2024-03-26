package com.uniboard.board.domain

interface BoardRepository {
    fun all(boardId: Long): List<BoardObject>

    fun get(boardId: Long, id: String): String

    fun add(boardId: Long, element: BoardObject)

    fun delete(boardId: Long, id: String)

    fun set(boardId: Long, element: BoardObject)
}