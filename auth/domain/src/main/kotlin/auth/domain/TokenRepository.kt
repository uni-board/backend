package auth.domain

import core.ktor.KtorConfigurable

interface TokenRepository : KtorConfigurable {
    suspend fun generateToken(userId: Long): String
}