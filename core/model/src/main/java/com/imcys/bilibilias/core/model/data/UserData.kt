package com.imcys.bilibilias.core.model.data

data class UserData(
    val fileStoragePath: String,
    val fileNameRule: String,
    val autoMerge: Boolean,
    val autoImport: Boolean,
    val shouldAppcenter: Boolean,
//    val themeBrand: ThemeBrand,
//    val darkThemeConfig: DarkThemeConfig,
)
