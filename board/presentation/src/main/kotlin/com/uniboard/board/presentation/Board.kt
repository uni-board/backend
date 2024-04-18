package com.uniboard.board.presentation

import com.uniboard.board.domain.AllBoardsRepository
import com.uniboard.board.domain.BoardObject
import com.uniboard.board.domain.BoardRepository
import com.uniboard.board.presentation.socket.dsl.*
import com.uniboard.board.presentation.socket.sockets
import com.uniboard.core.domain.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
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
    const val BOARD_API_GET_SETTINGS = "$BOARD_API Get Settings"
    const val BOARD_API_EDIT_SETTINGS = "$BOARD_API Edit Settings"
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
                debug("New board ID: $newID")
                call.respond(mapOf("id" to newID))
                info("Send response")
            }
        }
        get("/board/{id}/get") {
            logger.withTag(Tags.BOARD_API_ALL_OBJECTS) {
                info("Getting all objects")

                val id = call.parameters["id"]
                debug("Requested ID: $id")

                if (id == null || !allboards.exists(id)) {
                    call.respond(HttpStatusCode.BadRequest)
                    warn("Bad Request, ID: $id")

                    if (id != null) {
                       warn("Board exists: ${allboards.exists(id)}")
                    }
                    return@get
                }
                val elements = boardRepository.all(id)
                debug("All elements size: ${elements.size}")

                call.respond(elements.map { it.state }.toString().decoded)
                info("Sent all objects")
            }
        }
        get("/board/{id}/settings") {
            logger.withTag(Tags.BOARD_API_GET_SETTINGS) {
                info("Getting settings")

                val id = call.parameters["id"]
                debug("Requested ID: $id")

                if (id == null || !allboards.exists(id)) {
                    call.respond(HttpStatusCode.BadRequest)
                    warn("Bad Request, ID: $id")

                    if (id != null) {
                        warn("Board exists: ${allboards.exists(id)}")
                    }
                    return@get
                }
                val settings = allboards.settings(id)
                debug("Settings: $settings")

                call.respond(settings)
                info("Sent settings")
            }
        }
        put("/board/{id}/settings/edit") {
            logger.withTag(Tags.BOARD_API_EDIT_SETTINGS) {
                info("Editing settings")

                val id = call.parameters["id"]
                debug("Requested ID: $id")

                if (id == null || !allboards.exists(id)) {
                    call.respond(HttpStatusCode.BadRequest)
                    warn("Bad Request, ID: $id")

                    if (id != null) {
                        warn("Board exists: ${allboards.exists(id)}")
                    }
                    return@put
                }
                val body = call.receiveText()
                allboards.edit(id, body)

                call.respond(HttpStatusCode.OK)
                info("Edited settings")
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

            join(data)
            info("Connected with BoardID=$data")
        }
    }
    listen("created") { data ->
        logger.withTag(Tags.BOARD_SOCKETS_CREATED + currentRoom?.id) {
            info("Creating object")

            val room = requireRoomNotNull()
            room.send("created", data)

            val element = BoardObject(data.decoded.jsonObject.id, data)
            boardRepository.add(room.boardId, element)
            debug("Created object $element")
            info("Created object")
        }
    }
    listen("modified") { data ->
        logger.withTag(Tags.BOARD_SOCKETS_MODIFIED + currentRoom?.id) {
            info("Modifying object")

            val room = requireRoomNotNull()
            room.send("modified", data)

            val newData = data.decoded.jsonObject.toMutableMap()
            debug("New data is $newData")

            val id = newData.id
            debug("New data ID: $id")

            newData.remove("uniboardData")
            val oldData = boardRepository.get(room.boardId, id).decoded.jsonObject.toMutableMap()
            debug("Old data: $oldData")

            newData.forEach { (key, value) ->
                oldData[key] = value
            }
            debug("Modified data is $oldData")

            val element = BoardObject(id, JsonObject(oldData).encoded)
            boardRepository.set(room.boardId, element)
            debug("Modified object $element")
            info("Modified object")
        }
    }
    listen("deleted") { id ->
        logger.withTag(Tags.BOARD_SOCKETS_DELETED + currentRoom?.id) {
            info("Deleting object with ID")

            val room = requireRoomNotNull()
            room.send("deleted", id)
            boardRepository.delete(room.boardId, id)
            debug("Deleted object $id")
            info("Deleted object with ID")
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
private val Room.boardId: String
    get() = id

private val String.decoded: JsonElement
    get() = Json.decodeFromString(this)

private val JsonElement.encoded: String
    get() = Json.encodeToString(this)