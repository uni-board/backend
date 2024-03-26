package com.uniboard.core.domain

interface BuildConfig {
    val WORKDIR: String
    val SOCKETS_ENABLED: Boolean
    val NO_DB: Boolean
    val TRACE: Boolean
}