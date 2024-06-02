package com.imcys.bilibilias.core.model.data

data class UserData(
    val fileStoragePath: String?,
    val fileNamingRule: String?,
    val autoMerge: Boolean,
    val autoImport: Boolean,
    val shouldAppcenter: Boolean,
    val command: String,
//    val themeBrand: ThemeBrand,
//    val darkThemeConfig: DarkThemeConfig,
)
