package com.imcys.bilibilias

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class AsBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
