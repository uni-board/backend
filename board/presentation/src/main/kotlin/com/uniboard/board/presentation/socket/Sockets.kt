package com.uniboard.board.presentation.socket

import io.socket.engineio.server.EngineIoServer
import io.socket.engineio.server.EngineIoServerOptions
import io.socket.socketio.server.SocketIoServer
import io.socket.socketio.server.SocketIoSocket
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        sockets()
        delay(1000000000000)
    }
}

fun sockets() {
    val options = EngineIoServerOptions.newFromDefault()
        .apply {

        }
    EngineIoServer(options)
    val server = SocketIoServer(EngineIoServer(options).apply {
    })
    val namespace = server.namespace("/board")
    namespace.on("created") { arr ->
        val socket = arr.first() as SocketIoSocket
        socket.joinRoom("1")
        namespace.emit("hello")
    }
}