package com.imcys.bilibilias.core.context

import kotlinx.io.files.Path

expect object KmpContext {
    val isDebug: Boolean
    val platform: Platform
}

expect val KmpContext.cacheDir: Path
expect val KmpContext.dataDir: Path
expect val KmpContext.dataStoreDir: Path
expect val KmpContext.logsDir: Path

enum class Platform {
    ANDROID,
    DESKTOP,
}