package com.uniboard.pdf.domain

import java.io.InputStream

interface PdfRepository {
    suspend fun splitToImages(pdf: InputStream): List<InputStream>
}