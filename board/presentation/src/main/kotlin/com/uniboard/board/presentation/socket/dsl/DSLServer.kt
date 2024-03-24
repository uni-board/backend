package com.uniboard.board.presentation.socket.dsl

import com.corundumstudio.socketio.BroadcastAckCallback
import com.corundumstudio.socketio.BroadcastOperations
import com.corundumstudio.socketio.SocketIOClient

internal class DSLServer(
    private val server: com.corundumstudio.socketio.SocketIOServer,
    private val client: SocketIOClient
) : SocketIOServer {
    override suspend fun send(event: String, data: String) {
        client.sendEvent(event, data)
    }

    override suspend fun join(room: String) {
        client.joinRoom(room)
    }

    override fun room(name: String): SendServer {
        val operations = server.getRoomOperations(name)
        return operations.sendServer(client)
    }

    override val currentRoom: SendServer?
        get() {
            val roomName = client.allRooms?.singleOrNull() ?: return null
            return room(roomName)
        }
}
@SocketIODSL
private fun BroadcastOperations.sendServer(client: SocketIOClient) = object : SendServer {
    override suspend fun send(event: String, data: String) {
        sendEvent(event, data, client, BroadcastAckCallback(String::class.java))
    }
}