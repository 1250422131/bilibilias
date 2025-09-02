package com.imcys.bilibilias.buildlogic

enum class BILIBILIASBuildType(
    val applicationIdSuffix: String? = null,
    val versionNameSuffix: String? = null
) {
    DEBUG,
    RELEASE,
    BETA(null, "-beta"),
    ALPHA(".alpha", "-alpha")

}