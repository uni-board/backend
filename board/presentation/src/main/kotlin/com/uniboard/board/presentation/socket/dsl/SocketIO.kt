package com.uniboard.board.presentation.socket.dsl

interface SocketIO {
    @SocketIODSL
    fun listen(event: String, receive: suspend SocketIOServer.(data: String) -> Unit)

    @SocketIODSL
    fun exception(receive: SocketIOServer.(throwable: Throwable) -> Unit = { throw it })
}