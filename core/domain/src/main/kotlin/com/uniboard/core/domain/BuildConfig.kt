package com.uniboard.core.domain

import org.koin.core.scope.Scope

interface BuildConfig {
    val WORKDIR: String
    val SOCKETS_ENABLED: Boolean
    val NO_DB: Boolean
    val TRACE: Boolean
    val DB_CONNECT: String
}

val Scope.buildConfig: BuildConfig
    get() = get()