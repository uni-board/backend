package auth.domain

import core.ktor.KtorConfigurable
import io.ktor.server.application.*

interface TokenRepository : KtorConfigurable {
    suspend fun generateToken(userId: Long): String

    fun tokenFromCall(call: ApplicationCall): Long?
}