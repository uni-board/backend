package com.uniboard.board.presentation

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*

@KtorDsl
fun Application.board() {
    install(WebSockets)

    routing {
        webSocket {

        }
    }
}