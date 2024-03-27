package com.uniboard.core.domain

interface Logger {
    fun log(level: LogLevel, tag: String, message: String)
}

fun Logger.info(tag: String, message: String) =
    log(LogLevel.Info, tag, message)

fun Logger.debug(tag: String, message: String) =
    log(LogLevel.Debug, tag, message)

fun Logger.warn(tag: String, message: String) =
    log(LogLevel.Warn, tag, message)

fun Logger.error(tag: String, message: String) =
    log(LogLevel.Error, tag, message)

fun Logger.error(tag: String, throwable: Throwable) =
    error(tag, throwable.message ?: "")

inline fun Logger.withTag(tag: String, block: context(String, Logger) () -> Unit) {
    block(tag, this)
}

context(String)
fun Logger.info(message: String) = info(this@String, message)

context(String)
fun Logger.debug(message: String) = debug(this@String, message)

context(String)
fun Logger.warn(message: String) = warn(this@String, message)

context(String)
fun Logger.error(message: String) = error(this@String, message)

context(String)
fun Logger.error(throwable: Throwable) = error(this@String, throwable)