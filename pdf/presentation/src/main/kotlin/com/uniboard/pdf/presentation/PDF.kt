package com.uniboard.pdf.presentation

import com.uniboard.pdf.domain.PdfRepository
import com.uniboard.storage.domain.StorageRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

@KtorDsl
fun Application.pdf() {
    val pdfRepo by inject<PdfRepository>()
    val storageRepo by inject<StorageRepository>()
    routing {
        post("/pdf/split") {
            val id = call.request.queryParameters["id"]
            if (id == null || !storageRepo.fileExists(id)) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (!storageRepo.fileExists(id)) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }
            storageRepo.get(id).use { stream ->
                val ids = pdfRepo.splitToImages(stream) { inputStream ->
                    storageRepo.put(inputStream)
                }
                call.respond(ids.toList())
            }
        }
    }
}