package com.uniboard.pdf.data

import com.uniboard.pdf.domain.PdfRepository
import org.koin.dsl.module

val pdfModule = module {
    single<PdfRepository> { PdfRepositoryImpl() }
}