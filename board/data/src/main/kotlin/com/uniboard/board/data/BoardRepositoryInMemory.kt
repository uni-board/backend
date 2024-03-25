package com.uniboard.board.data

import com.uniboard.board.domain.BoardObject
import com.uniboard.board.domain.BoardRepository

class BoardRepositoryInMemory : BoardRepository {
    private val boards = mutableMapOf<Long, MutableList<BoardObject>>()
    override fun all(boardId: Long): List<BoardObject> {
        return boards[boardId] ?: emptyList()
    }

    private fun objects(boardId: Long) = boards.getOrPut(boardId) {
        mutableListOf()
    }

    override fun get(boardId: Long, id: Long): String {
        return boards[boardId]?.find { it.id == id }?.state ?: ""
    }

    override fun add(boardId: Long, data: String): Long {
        val objects = objects(boardId)
        val lastId = objects.lastOrNull()?.id ?: -1
        objects.add(BoardObject(lastId + 1, data))
        return lastId + 1
    }

    override fun delete(boardId: Long, id: Long) {
        objects(boardId).removeAll { it.id == id }
    }

    override fun set(boardId: Long, id: Long, data: String) {
        objects(boardId).replaceAll { element ->
            if (element.id == id) element.copy(state = data)
            else element
        }
    }
}