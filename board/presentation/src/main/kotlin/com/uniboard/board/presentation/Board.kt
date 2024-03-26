package com.uniboard.board.presentation

import com.uniboard.board.domain.AllBoardsRepository
import com.uniboard.board.domain.BoardObject
import com.uniboard.board.domain.BoardRepository
import com.uniboard.board.presentation.socket.dsl.Room
import com.uniboard.board.presentation.socket.dsl.SocketIODSL
import com.uniboard.board.presentation.socket.dsl.requireRoomNotNull
import com.uniboard.board.presentation.socket.dsl.sendAndFinish
import com.uniboard.board.presentation.socket.sockets
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.koin.core.context.GlobalContext
import org.koin.ktor.ext.inject

@KtorDsl
fun Application.board() {
    val allboards by inject<AllBoardsRepository>()
    val boardRepository by inject<BoardRepository>()
    routing {
        post("/createboard") {
            val newID = allboards.add()
            call.respond(mapOf("id" to newID))
        }
        get("/board/{id}/get") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            println(boardRepository.all(id).map { it.state })
            call.respond(boardRepository.all(id).map { it.state }.toString().decoded)
        }
    }
}

fun CoroutineScope.boardSocketServer() = sockets {
    exception {
        it.printStackTrace()
    }
    val boardRepository by GlobalContext.get().inject<BoardRepository>()
    listen("connected") { data ->
        val boardId = data.toLongOrNull() ?: sendAndFinish("error", "Board ID is incorrect")
        join(boardId.toString())
    }
    listen("created") { data ->
        val room = requireRoomNotNull()
        boardRepository.add(room.boardId, BoardObject(data.decoded.jsonObject.id, data))
        room.send("created", data)
    }
    listen("modified") { data ->
        val room = requireRoomNotNull()
        val newData = data.decoded.jsonObject.toMutableMap()
        val id = newData.id
        newData.remove("uniboardData")
        val oldData = boardRepository.get(room.boardId, id).decoded.jsonObject.toMutableMap()
        newData.forEach { (key, value) ->
            oldData[key] = value
        }
        boardRepository.set(room.boardId, BoardObject(id, JsonObject(oldData).encoded))
        room.send("modified", data)
    }
    listen("deleted") { data ->
        val room = requireRoomNotNull()
        boardRepository.delete(room.boardId, data)
        room.send("deleted", data)
    }
}

private val Map<String, JsonElement>.id: String
    get() = get("uniboardData")
        ?.jsonObject
        ?.get("id")
        ?.jsonPrimitive
        ?.content
        .toString()

private val String.boardObject: BoardObject
    get() = Json.decodeFromString(this)

private val BoardObject.json: String
    get() = Json.encodeToString(this)

@SocketIODSL
private val Room.boardId: Long
    get() = id.toLong()

private val String.decoded: JsonElement
    get() = Json.decodeFromString(this)

private val JsonElement.encoded: String
    get() = Json.encodeToString(this)