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