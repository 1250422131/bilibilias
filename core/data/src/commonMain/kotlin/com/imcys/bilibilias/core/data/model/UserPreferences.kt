package com.imcys.bilibilias.core.data.model

data class UserPreferences(
    val videoResolverSettings: VideoResolverSettings

)

data class VideoResolverSettings(
    val name: String,
)