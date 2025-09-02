package com.imcys.bilibilias.common.data

enum class ASBuildType {
    RELEASE,
    DEBUG,
    BETA,
    ALPHA,
}


fun getASBuildType(buildType: String): ASBuildType {
    return when (buildType) {
        "release" -> ASBuildType.RELEASE
        "debug" -> ASBuildType.DEBUG
        "beta" -> ASBuildType.BETA
        "alpha" -> ASBuildType.ALPHA
        else -> ASBuildType.DEBUG
    }
}