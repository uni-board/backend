package com.uniboard.storage.presentation

import com.uniboard.storage.domain.StorageRepository
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.utils.io.jvm.javaio.*
import org.koin.ktor.ext.inject

@KtorDsl
fun Application.storage() {
    val storageRepository by inject<StorageRepository>()
    routing {
        route("/storage") {
            post("/add") {
                val data = call.receiveMultipart()
                data.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val id = storageRepository.put(part.streamProvider())
                        call.respond(AddFileResponse(id))
                    }
                }
            }
            get("/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                if (!storageRepository.fileExists(id)) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                val stream = storageRepository.get(id)
                call.respondBytesWriter {
                    stream.copyTo(this)
                }
            }
        }
    }
}