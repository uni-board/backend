package com.uniboard.core.data

import com.uniboard.core.domain.BuildConfig
import com.uniboard.core.domain.Logger
import com.uniboard.core.domain.error
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val coreModule = module {
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { coroutineContext, throwable ->
            get<Logger>().error("Error from CoroutineScope", throwable.message ?: "")
        })
    }
    single<BuildConfig> { EnvBuildConfig() }
    single<Logger> { FileLogger(get(), CoroutineScope(Dispatchers.IO)) }
}