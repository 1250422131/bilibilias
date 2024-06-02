package com.imcys.bilibilias.feature.settings

data class UserEditableSettings(
    val fileStoragePath: String,
    val folderNameRule: String,
    val autoMerge: Boolean,
    val autoImport: Boolean,
    val command: String,
    val shouldAppcenter: Boolean,
)
