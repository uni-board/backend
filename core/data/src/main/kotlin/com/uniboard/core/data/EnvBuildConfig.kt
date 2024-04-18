package com.uniboard.core.data

import com.uniboard.core.domain.BuildConfig
import java.io.File

class EnvBuildConfig : BuildConfig {
    override val WORKDIR = "/app/data"
    override val SOCKETS_ENABLED = booleanEnv("SOCKETS_ENABLED") ?: true
    override val NO_DB = booleanEnv("NO_DB") ?: false
    override val TRACE = booleanEnv("TRACE") ?: false
    override val DB_CONNECT = System.getenv("DB_CONNECT").orEmpty()

    private fun booleanEnv(name: String): Boolean? =
        System.getenv(name)?.toBoolean()
}
