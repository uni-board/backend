package com.uniboard.pdf.domain

import java.io.InputStream

interface PdfRepository {
    suspend fun <T> splitToImages(pdf: InputStream, process: suspend (InputStream) -> T): List<T>
}