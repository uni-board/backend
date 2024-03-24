package com.uniboard.board.presentation.socket

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import com.uniboard.board.presentation.socket.dsl.DSLServer
import com.uniboard.board.presentation.socket.dsl.SocketIO
import com.uniboard.board.presentation.socket.dsl.SocketIODSL
import com.uniboard.board.presentation.socket.dsl.sendAndFinish
import kotlinx.coroutines.*

fun main() {
    runBlocking {
        sockets {
            listen("connected") {
                val boardId = it.toLongOrNull() ?: sendAndFinish("ceee", "ID NOT FOUND")
                send("ceee", boardId.toString())
                send("receive", it)
                join(it)
                room(it).send("receive$it", "ROOM DATA")
            }
        }
        delay(1000000000000)
    }
}

private class Listener(
    val event: String,
    val receive: suspend com.uniboard.board.presentation.socket.dsl.SocketIOServer.(data: String) -> Unit
)

@SocketIODSL
fun CoroutineScope.sockets(configure: SocketIO.() -> Unit) {
    val listeners = mutableListOf<Listener>()
    SocketIO { path, receive -> listeners.add(Listener(path, receive)) }.configure()

    val config = Configuration().apply {
        hostname = "localhost"
        port = 8082
    }

    val server = SocketIOServer(config)

    listeners.forEach { listener ->
        server.addEventListener(listener.event, String::class.java) { client, data, _ ->
            launch {
                val dslServer = DSLServer(server, client)
                listener.receive(dslServer, data)
            }
        }
    }
    server.start()
}