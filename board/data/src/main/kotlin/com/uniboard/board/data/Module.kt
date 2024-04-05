package com.uniboard.board.data

import com.uniboard.board.domain.AllBoardsRepository
import com.uniboard.board.domain.BoardRepository
import com.uniboard.core.domain.buildConfig
import org.koin.dsl.module

val boardModule = module {
    single<BoardRepository> {
        if (buildConfig.NO_DB) BoardRepositoryInMemory()
        else BoardRepositoryImpl(get())
    }
    single<AllBoardsRepository> {
        if (buildConfig.NO_DB) AllBoardsRepositoryInMemory()
        else AllBoardsRepositoryImpl(get())
    }
}