package com.uniboard.board.data

import com.uniboard.board.domain.AllBoardsRepository
import java.util.UUID

class AllBoardsRepositoryInMemory: AllBoardsRepository {
    private val boards = mutableListOf<String>()
    override fun add(): String{
        val uuid = UUID.randomUUID().toString()
        println(uuid)
        boards.add(uuid)
        return uuid
    }

    override fun exists(id: String): Boolean {
        return id in boards
    }


    override fun delete(id: String) {
        boards.remove(id)
    }
}