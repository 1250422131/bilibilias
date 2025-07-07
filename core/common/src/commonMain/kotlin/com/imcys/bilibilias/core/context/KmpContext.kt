package com.imcys.bilibilias.core.context

import kotlinx.io.files.Path

expect object KmpContext {
    val isDebug: Boolean
    val dataDir: Path
    val cacheDir: Path
    val logsDir: Path
    val dataStoreDir: Path
    val platform: Platform
}

enum class Platform {
    ANDROID,
    DESKTOP,
}