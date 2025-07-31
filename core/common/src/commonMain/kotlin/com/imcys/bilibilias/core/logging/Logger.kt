package com.imcys.bilibilias.core.logging

import co.touchlab.kermit.Logger

inline fun <reified T : Any> logger(): Logger {
    val tag = T::class.simpleName ?: "Anonymous"
    return Logger.withTag(tag)
}