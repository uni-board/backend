package com.uniboard.board.data

import com.uniboard.board.domain.BoardObject
import com.uniboard.board.domain.BoardRepository

class BoardRepositoryInMemory : BoardRepository {
    private val boards = mutableMapOf<Long, MutableMap<String, BoardObject>>()
    override fun all(boardId: Long): List<BoardObject> {
        return boards[boardId]?.values?.toList() ?: emptyList()
    }

    private fun objects(boardId: Long) = boards.getOrPut(boardId) {
        mutableMapOf()
    }

    override fun get(boardId: Long, id: String): String {
        return boards[boardId]?.get(id)?.state ?: ""
    }

    override fun add(boardId: Long, element: BoardObject) {
        val objects = objects(boardId)
        objects[element.id] = element
    }

    override fun delete(boardId: Long, id: String) {
        objects(boardId).remove(id)
    }

    override fun set(boardId: Long, element: BoardObject) {
        objects(boardId)[element.id] = element
    }
}