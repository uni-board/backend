package com.uniboard.pdf.data

import com.uniboard.pdf.domain.PdfRepository
import org.apache.pdfbox.Loader
import org.apache.pdfbox.rendering.PDFRenderer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

class PdfRepositoryImpl : PdfRepository {
    override suspend fun <T> splitToImages(pdf: InputStream, process: suspend (InputStream) -> T): List<T> =
        Loader.loadPDF(pdf.readAllBytes()).use { document ->
            val renderer = PDFRenderer(document)
            (0 until document.numberOfPages).map { index ->
                val image = renderer.renderImageWithDPI(index, 300f)
                ByteArrayOutputStream().use { outputStream ->
                    ImageIO.write(image, "png", outputStream)
                    image.flush()
                    process(outputStream.toByteArray().inputStream())
                }
            }
        }.also { System.gc() }
}