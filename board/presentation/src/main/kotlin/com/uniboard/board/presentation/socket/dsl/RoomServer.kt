package com.uniboard.board.presentation.socket.dsl

interface RoomServer {
    @SocketIODSL
    suspend fun join(room: String)

    @SocketIODSL
    fun room(name: String): SendServer

    @SocketIODSL
    val currentRoom: SendServer?
}

@SocketIODSL
suspend fun <T> T.requireRoomNotNull(
    errorEvent: String = "error",
    message: () -> String = { "Client is not connected to any room" }
): SendServer where T : RoomServer, T : SendServer {
    return currentRoom ?: sendAndFinish(errorEvent, message())
}