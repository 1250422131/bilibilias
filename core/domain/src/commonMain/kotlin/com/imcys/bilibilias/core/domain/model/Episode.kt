package com.imcys.bilibilias.core.domain.model

import com.imcys.bilibilias.core.datasource.CdnResource

data class EpisodeInfo(
    val bvid: String,
    val cid: Long,
    val title: String,
    val urls: List<MediaStreamMetadata>,
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