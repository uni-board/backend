package auth.domain

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

@KtorDsl
fun Route.auth(configuration: Route.() -> Unit) {
    authenticate("auth-jwt") {
        configuration()
    }
}

val ApplicationCall.userId: Long?
    get() {
        val repo by inject<TokenRepository>()
        return repo.tokenFromCall(this)
    }