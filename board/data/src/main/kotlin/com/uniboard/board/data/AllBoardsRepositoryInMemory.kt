package com.uniboard.board.data

import com.uniboard.board.domain.AllBoardsRepository

class AllBoardsRepositoryInMemory: AllBoardsRepository {
    private val boards = mutableListOf<Long>()
    override fun add(): Long {
        val lastId = boards.lastOrNull() ?: -1
        boards.add(lastId + 1)
        return lastId + 1
    }

    override fun delete(id: Long) {
        boards.remove(id)
    }
}