package core.ktor

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.*

@KtorDsl
fun Application.apiRouting(configuration: Route.() -> Unit) {
    routing {
        route("/api") {
            configuration()
        }
    }
}

val ApplicationCall.userId: Long?
    get() {
        val principal = principal<JWTPrincipal>()
        return principal?.payload?.getClaim("userid")?.asString()?.toLong()
    }