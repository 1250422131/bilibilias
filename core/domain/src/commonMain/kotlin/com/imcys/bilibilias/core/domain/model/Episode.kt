package com.imcys.bilibilias.core.domain.model

import com.imcys.bilibilias.core.datasource.CdnResource

data class EpisodeInfo(
    val bvid: String,
    val cid: Long,
    val desc: String,
    val cover: String,
    val title: String,
    val owner: Owner,
    val video: MediaStreamMetadata,
    val audio: MediaStreamMetadata,
)

/**
 * [codecId] audio always 0, video include 7/avc 12/hevc 13/av1
 */
data class MediaStreamMetadata(
    val id: Int,
    val backupUrl: List<CdnResource>,
    val codecId: Int = 0,
    val description: String = ""
)

data class Owner(
    val id: Long,
    val avatarUrl: String,
    val name: String
)