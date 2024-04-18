package com.uniboard.board.data

import com.uniboard.board.domain.AllBoardsRepository
import java.util.UUID

class AllBoardsRepositoryInMemory: AllBoardsRepository {
    private val boards = mutableListOf<Pair<String, String>>()
    override fun add(): String{
        val uuid = UUID.randomUUID().toString()
        println(uuid)
        boards.add(uuid to "")
        return uuid
    }

    override fun exists(id: String): Boolean {
        return boards.any { it.first == id }
    }

    override fun settings(id: String): String {
        return boards.first { it.first == id }.second
    }

    override fun edit(id: String, settings: String) {
        boards.replaceAll { if (it.first == id) it.copy(second = settings) else it }
    }


    override fun delete(id: String) {
        boards.removeAll { it.first == id }
    }
}