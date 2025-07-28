package com.imcys.bilibilias.core.startup

fun interface Startup {
    suspend fun initialize()
}