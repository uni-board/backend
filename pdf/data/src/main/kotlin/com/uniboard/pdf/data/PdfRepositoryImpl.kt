package com.uniboard.pdf.data

import com.uniboard.pdf.domain.PdfRepository
import org.apache.pdfbox.Loader
import org.apache.pdfbox.rendering.PDFRenderer
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

class PdfRepositoryImpl : PdfRepository {
    override suspend fun splitToImages(pdf: InputStream): List<InputStream> {
        val document = Loader.loadPDF(pdf.readAllBytes())
        val renderer = PDFRenderer(document)
        return document.pages.mapIndexed { index, _ ->
            val image = renderer.renderImageWithDPI(index, 300f)
            val outputStream = ByteArrayOutputStream()
            ImageIO.write(image, "png", outputStream)
            outputStream.close()
            outputStream.toByteArray().inputStream()
        }.also {
            document.close()
        }
    }
}