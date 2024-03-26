package com.uniboard.board.presentation.socket.dsl

interface RoomServer {
    @SocketIODSL
    suspend fun join(room: String)

    @SocketIODSL
    fun room(name: String): Room

    @SocketIODSL
    val currentRoom: Room?
}

@SocketIODSL
suspend fun <T> T.requireRoomNotNull(
    errorEvent: String = "error",
    message: () -> String = { "Client is not connected to any room" }
): Room where T : RoomServer, T : SendServer {
    return currentRoom ?: sendAndFinish(errorEvent, message())
}