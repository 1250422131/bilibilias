package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val storageFolder: String? = null,
    val fileNamesConvention: String? = null,
    val ffmpegCommand: String? = null,
)
