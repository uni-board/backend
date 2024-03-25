package com.uniboard.board.data

import com.uniboard.board.domain.AllBoardsRepository
import com.uniboard.board.domain.BoardRepository
import org.koin.dsl.module

val boardModule = module {
    single<BoardRepository> { BoardRepositoryInMemory() }
    single<AllBoardsRepository> { AllBoardsRepositoryInMemory() }
}