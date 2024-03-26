package com.uniboard.backend.data

import com.uniboard.backend.domain.StorageDB
import com.uniboard.backend.domain.StorageRepository
import org.koin.dsl.module

val storageModule = module {
    single<StorageDB> { StorageDBInMemory() }
    single<StorageRepository> { StorageRepositoryImpl(get()) }
}