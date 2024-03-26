package com.uniboard.board.domain

interface AllBoardsRepository {
    fun add(): Long

    fun delete(id: Long)
}