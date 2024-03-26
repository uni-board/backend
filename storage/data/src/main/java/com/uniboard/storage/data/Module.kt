package com.uniboard.storage.data

import com.uniboard.storage.domain.StorageDB
import com.uniboard.storage.domain.StorageRepository
import org.koin.dsl.module

val storageModule = module {
    single<StorageDB> { StorageDBInMemory() }
    single<StorageRepository> { StorageRepositoryImpl(get(), get()) }
}