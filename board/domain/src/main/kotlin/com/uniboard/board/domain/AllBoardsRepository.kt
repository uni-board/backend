package com.uniboard.board.domain

interface AllBoardsRepository {
    fun add(): String

    fun exists(id: String): Boolean

    fun settings(id: String): String

    fun edit(id: String, settings: String)

    fun delete(id: String)
}