package com.uniboard.board.domain

interface AllBoardsRepository {
    fun add(): String

    fun exists(id: String): Boolean

    fun delete(id: String)
}