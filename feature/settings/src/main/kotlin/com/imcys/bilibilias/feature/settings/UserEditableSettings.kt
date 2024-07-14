package com.imcys.bilibilias.feature.settings

data class UserEditableSettings(
    val storagePath: String,
    val namingRule: String,
    val autoMerge: Boolean,
    val autoImport: Boolean,
    val command: String,
    val shouldAppcenter: Boolean,
)
