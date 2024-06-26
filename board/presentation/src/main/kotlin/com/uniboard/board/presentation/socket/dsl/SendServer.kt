package com.uniboard.board.presentation.socket.dsl

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.awaitCancellation

interface SendServer {
    @SocketIODSL
    suspend fun send(event: String, data: String)
}

@SocketIODSL
suspend fun SendServer.sendAndFinish(event: String, data: String): Nothing {
    send(event, data)
    throw CancellationException()
}