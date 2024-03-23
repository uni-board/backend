package com.uniboard.board.domain

import kotlinx.serialization.Serializable

@Serializable
data class BoardObject(
    val id: Long,
    val state: String
)
