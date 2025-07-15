package com.imcys.bilibilias.core.context

import kotlinx.io.files.Path

expect object KmpContext {
    val isDebug: Boolean

    val cacheDir: Path
    val dataDir: Path
    val dataStoreDir: Path
    val logsDir: Path

    val platform: Platform
}

enum class Platform {
    ANDROID,
    DESKTOP;
}