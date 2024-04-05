package com.uniboard.board.domain

interface BoardRepository {
    fun all(boardId: String): List<BoardObject>

    fun get(boardId: String, id: String): String

    fun add(boardId: String, element: BoardObject)

    fun delete(boardId: String, id: String)

    fun set(boardId: String, element: BoardObject)
}