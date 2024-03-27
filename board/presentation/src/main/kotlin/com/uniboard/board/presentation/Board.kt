package com.uniboard.board.presentation

import com.uniboard.board.domain.AllBoardsRepository
import com.uniboard.board.domain.BoardObject
import com.uniboard.board.domain.BoardRepository
import com.uniboard.board.presentation.socket.dsl.*
import com.uniboard.board.presentation.socket.sockets
import com.uniboard.core.domain.*
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

private object Tags {
    const val BOARD_SOCKETS = "Board Sockets"
    const val BOARD_SOCKETS_CONNECTED = "$BOARD_SOCKETS Connected"
    const val BOARD_SOCKETS_CREATED = "$BOARD_SOCKETS Created"
    const val BOARD_SOCKETS_MODIFIED = "$BOARD_SOCKETS Modified"
    const val BOARD_SOCKETS_DELETED = "$BOARD_SOCKETS Deleted"
    const val BOARD_API = "Board API"
    const val BOARD_API_CREATEDBOARD = "$BOARD_API CreateBoard"
    const val BOARD_API_ALL_OBJECTS = "$BOARD_API All objects"
}

@KtorDsl
fun Application.board() {
    val allboards by inject<AllBoardsRepository>()
    val boardRepository by inject<BoardRepository>()
    val logger by inject<Logger>()
    val buildConfig by inject<BuildConfig>()
    if (buildConfig.SOCKETS_ENABLED) {
        val coroutineScope by inject<CoroutineScope>()
        coroutineScope.boardSocketServer()
    }
    routing {
        post("/createboard") {
            logger.withTag(Tags.BOARD_API_CREATEDBOARD) {
                info("Creating new board")
                val newID = allboards.add()
                info("New board ID: $newID")
                call.respond(mapOf("id" to newID))
                info("Send response")
            }
        }
        get("/board/{id}/get") {
            logger.withTag(Tags.BOARD_API_ALL_OBJECTS) {
                info("Getting all objects")

                val id = call.parameters["id"]?.toLongOrNull()
                info("Requested ID: $id")

                if (id == null || !allboards.exists(id)) {
                    call.respond(HttpStatusCode.BadRequest)
                    warn("Bad Request, ID: $id")

                    if (id != null) {
                       warn("Board exists: ${allboards.exists(id)}")
                    }
                    return@get
                }
                val elements = boardRepository.all(id)
                info("All elements size: ${elements.size}")

                call.respond(elements.map { it.state }.toString().decoded)
                info("Got all objects")
            }
        }
    }
}

private fun CoroutineScope.boardSocketServer() = sockets {
    val boardRepository by inject<BoardRepository>()
    val logger by inject<Logger>()
    exception {
        logger.error(Tags.BOARD_SOCKETS, it)
    }
    logger.withTag(Tags.BOARD_SOCKETS_CONNECTED) {
        listen("connected") { data ->
            info("Connecting with $data")

            val boardId = data.toLongOrNull() ?: sendAndFinish("error", "Board ID is incorrect")
            join(boardId.toString())
            info("Connected with BoardID=$boardId")
        }
    }
    listen("created") { data ->
        logger.withTag(Tags.BOARD_SOCKETS_CREATED + currentRoom?.id) {
            info("Creating object")

            val room = requireRoomNotNull()

            val element = BoardObject(data.decoded.jsonObject.id, data)
            boardRepository.add(room.boardId, element)
            room.send("created", data)
            info("Created object $element")
        }
    }
    listen("modified") { data ->
        logger.withTag(Tags.BOARD_SOCKETS_MODIFIED + currentRoom?.id) {
            info("Modifying object $data")

            val room = requireRoomNotNull()
            val newData = data.decoded.jsonObject.toMutableMap()
            info("New data is $newData")

            val id = newData.id
            info("New data ID: $id")

            newData.remove("uniboardData")
            val oldData = boardRepository.get(room.boardId, id).decoded.jsonObject.toMutableMap()
            info("Old data: $oldData")

            newData.forEach { (key, value) ->
                oldData[key] = value
            }
            info("Modified data is $oldData")

            val element = BoardObject(id, JsonObject(oldData).encoded)
            boardRepository.set(room.boardId, element)
            room.send("modified", data)
            info("Modified object $element")
        }
    }
    listen("deleted") { id ->
        logger.withTag(Tags.BOARD_SOCKETS_DELETED + currentRoom?.id) {
            info("Deleting object with ID: $id")

            val room = requireRoomNotNull()
            boardRepository.delete(room.boardId, id)
            room.send("deleted", id)
            info("Deleted object with ID: $id")
        }
    }
}

private val Map<String, JsonElement>.id: String
    get() = get("uniboardData")
        ?.jsonObject
        ?.get("id")
        ?.jsonPrimitive
        ?.content
        .toString()

@SocketIODSL
private val Room.boardId: Long
    get() = id.toLong()

private val String.decoded: JsonElement
    get() = Json.decodeFromString(this)

private val JsonElement.encoded: String
    get() = Json.encodeToString(this)