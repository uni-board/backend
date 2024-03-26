package com.uniboard.backend.data

import com.uniboard.backend.domain.StorageDB

class StorageDBInMemory: StorageDB {
    private val files = mutableMapOf<Long, MutableSet<Long>>()
    private fun board(id: Long): MutableSet<Long> {
        return files.getOrPut(id) { mutableSetOf() }
    }
    override fun add(boardId: Long): Long {
        val lastId = board(boardId).lastOrNull() ?: -1
        board(boardId).add(lastId + 1)
        return lastId + 1
    }

    override fun fileExists(boardId: Long, id: Long): Boolean {
        return id in board(boardId)
    }

    override fun delete(boardId: Long, id: Long) {
        board(boardId).remove(id)
    }
}