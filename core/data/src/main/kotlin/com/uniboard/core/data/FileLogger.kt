package com.uniboard.core.data

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.uniboard.core.domain.BuildConfig
import com.uniboard.core.domain.LogLevel
import com.uniboard.core.domain.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class FileLogger(config: BuildConfig, private val scope: CoroutineScope) : Logger {
    private val writer = csvWriter()
    private val file by lazy {
        File("${config.WORKDIR}/log.csv").also { file ->
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
                writer.open(file) {
                    writeRow("TIMESTAMP", "LEVEL", "TAG", "MESSAGE")
                }
            }
        }
    }

    private fun write(level: LogLevel, channel: String, message: String) {
        scope.launch {
            writer.open(file, append = true) {
                writeRow(listOf(System.currentTimeMillis(), level, channel, message))
            }
        }
    }

    override fun log(level: LogLevel, tag: String, message: String) {
        write(level, tag, message)
    }
}