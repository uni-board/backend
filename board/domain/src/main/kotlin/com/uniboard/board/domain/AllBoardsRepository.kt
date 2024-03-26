package com.uniboard.board.domain

interface AllBoardsRepository {
    fun add(): Long

    fun exists(id: Long): Boolean

    fun delete(id: Long)
}