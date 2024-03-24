package com.uniboard.board.presentation.socket.dsl

fun interface SocketIO {
    @SocketIODSL
    fun listen(event: String, receive: suspend SocketIOServer.(data: String) -> Unit)
}