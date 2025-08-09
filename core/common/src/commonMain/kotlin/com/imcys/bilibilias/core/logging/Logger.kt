package com.imcys.bilibilias.core.logging

import io.github.smyrgeorge.log4k.Logger

inline fun <reified T : Any> logger(): Logger {
    return Logger.of(T::class)
}