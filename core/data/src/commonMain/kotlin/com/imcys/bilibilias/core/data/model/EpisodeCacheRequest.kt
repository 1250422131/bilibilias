package com.imcys.bilibilias.core.data.model

@Deprecated("Use EpisodeCacheState")
data class EpisodeCacheRequest(
    val episodeId: String,
    val episodeSubId: Long,
    @Deprecated("未来在设置中替换")
    val videoResolution: Int,
    @Deprecated("未来在设置中替换")
    val audioResolution: Int,
)