package com.uniboard.storage.data

import com.uniboard.core.domain.buildConfig
import com.uniboard.storage.domain.StorageDB
import com.uniboard.storage.domain.StorageRepository
import org.koin.dsl.module

val storageModule = module {
    single<StorageDB> {
        if (buildConfig.NO_DB) StorageDBInMemory()
        else StorageDBImpl(get())
    }
    single<StorageRepository> { StorageRepositoryImpl(get(), get()) }
}