package auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class RawUser(
    val name: String,
    val password: String
)
