package com.imcys.bilibilias.okdownloader.internal.util

inline fun requireNotNullOrEmpty(value: String?, lazyMessage: () -> Any): String {
    if (value.isNullOrEmpty()) {
        val message = lazyMessage()
        throw IllegalArgumentException(message.toString())
    } else {
        return value
    }
}

