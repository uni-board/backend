package auth.presentation

import auth.domain.*
import core.ktor.apiRouting
import core.ktor.configure
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.auth() {
    val db by inject<UsersRepository>()
    val tokenRepo by inject<TokenRepository>()
    tokenRepo.configure()

    apiRouting {
        post("signin") {
            val rawUser = call.receive<RawUser>()
            val user = db.getByName(rawUser.name)
            if (user != null) {
                val token = tokenRepo.generateToken(user.id)
                call.respond(token)
            }
        }
        post("signup") {
            val user = call.receive<RawUser>()
            if (db.getByName(user.name) == null) {
                val id = db.create(user)
                call.respond(HttpStatusCode.Created, id.toString())
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }
        auth {
            get("hello") {
                val id = call.userId
                if (id != null) {
                    val user = db.getById(id)
                    call.respondText("Hello, ${user?.name}!")
                }
            }
        }
    }
}