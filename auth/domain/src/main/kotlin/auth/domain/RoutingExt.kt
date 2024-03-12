package auth.domain

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.util.*

@KtorDsl
fun Route.auth(configuration: Route.() -> Unit) {
    authenticate("auth-jwt") {
        configuration()
    }
}
