package com.uniboard.board.presentation.socket

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketConfig
import com.uniboard.board.presentation.socket.dsl.DSLServer
import com.uniboard.board.presentation.socket.dsl.SocketIO
import com.uniboard.board.presentation.socket.dsl.SocketIODSL
import com.uniboard.board.presentation.socket.dsl.SocketIOServer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private class Listener(
    val event: String,
    val receive: suspend SocketIOServer.(data: String) -> Unit
)

@SocketIODSL
fun CoroutineScope.sockets(port: Int = 8081, configure: SocketIO.() -> Unit) {
    val listeners = mutableListOf<Listener>()
    val exceptionHandlers = mutableListOf<SocketIOServer.(Throwable) -> Unit>()
    object : SocketIO {
        override fun listen(
            event: String,
            receive: suspend SocketIOServer.(data: String) -> Unit
        ) {
            listeners.add(Listener(event, receive))
        }

        override fun exception(receive: SocketIOServer.(throwable: Throwable) -> Unit) {
            exceptionHandlers.add(receive)
        }
    }.configure()
    val mainExceptionHandler: SocketIOServer.(Throwable) -> Unit = { throwable ->
        exceptionHandlers.forEach { handler -> handler(this, throwable) }
    }

    val config = Configuration().apply {
        this.port = port
    }

    val server = com.corundumstudio.socketio.SocketIOServer(config)

    listeners.forEach { listener ->
        server.addEventListener(listener.event, String::class.java) { client, data, _ ->
            val dslServer = DSLServer(server, client)
            launch(CoroutineExceptionHandler { _, throwable ->
                if (exceptionHandlers.isEmpty()) throw throwable
                mainExceptionHandler(dslServer, throwable)
            } + SupervisorJob()) {
                listener.receive(dslServer, data)
            }
        }
    }
    server.start()
}