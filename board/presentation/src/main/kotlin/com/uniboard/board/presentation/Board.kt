package com.uniboard.board.presentation

import com.uniboard.board.domain.AllBoardsRepository
import com.uniboard.board.domain.BoardRepository
import com.uniboard.board.presentation.socket.dsl.requireRoomNotNull
import com.uniboard.board.presentation.socket.dsl.sendAndFinish
import com.uniboard.board.presentation.socket.sockets
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
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
            call.respond(boardRepository.all(id).toList())
        }
    }
}

fun CoroutineScope.boardSocketServer() {
    sockets {
        listen("connected") { data ->
            val boardId = data.toLongOrNull() ?: sendAndFinish("error", "Board ID is incorrect")
            join(boardId.toString())
        }
        listen("created") { data ->
            val room = requireRoomNotNull()
            // TODO: Add db handling
            room.send("created", data)
        }
        listen("modified") { data ->
            val room = requireRoomNotNull()
            // TODO: Add db handling
            room.send("modified", data)
        }
        listen("deleted") { data ->
            val room = requireRoomNotNull()
            // TODO: Add db handling
            room.send("deleted", data)
        }
    }
}