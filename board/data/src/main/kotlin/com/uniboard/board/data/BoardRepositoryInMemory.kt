package com.uniboard.board.data

import com.uniboard.board.domain.BoardObject
import com.uniboard.board.domain.BoardRepository

class BoardRepositoryInMemory : BoardRepository {
    private val boards = mutableMapOf<String, MutableMap<String, BoardObject>>()
    override fun all(boardId: String): List<BoardObject> {
        return boards[boardId]?.values?.toList() ?: emptyList()
    }

    private fun objects(boardId: String) = boards.getOrPut(boardId) {
        mutableMapOf()
    }

    override fun get(boardId: String, id: String): String {
        return boards[boardId]?.get(id)?.state ?: ""
    }

    override fun add(boardId: String, element: BoardObject) {
        val objects = objects(boardId)
        objects[element.id] = element
    }

    override fun delete(boardId: String, id: String) {
        objects(boardId).remove(id)
    }

    override fun set(boardId: String, element: BoardObject) {
        objects(boardId)[element.id] = element
    }
}