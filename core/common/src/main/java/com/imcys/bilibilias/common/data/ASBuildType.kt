package com.imcys.bilibilias.common.data

enum class ASBuildType {
    OFFICIAL,
    BETA,
    ALPHA,
}


fun getASBuildType(buildType: String): ASBuildType {
    return when (buildType) {
        "official" -> ASBuildType.OFFICIAL
        "beta" -> ASBuildType.BETA
        "alpha" -> ASBuildType.ALPHA
        else -> ASBuildType.OFFICIAL
    }
}