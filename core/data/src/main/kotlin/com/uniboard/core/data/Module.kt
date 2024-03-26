package com.uniboard.core.data

import com.uniboard.core.domain.BuildConfig
import org.koin.dsl.module

val coreModule = module {
    single<BuildConfig> { EnvBuildConfig() }
}