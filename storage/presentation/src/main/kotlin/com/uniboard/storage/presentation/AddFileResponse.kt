package com.uniboard.storage.presentation

import kotlinx.serialization.Serializable

@Serializable
data class AddFileResponse(
    val id: String,
    val url: String
)