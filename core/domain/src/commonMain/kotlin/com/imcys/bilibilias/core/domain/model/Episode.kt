package com.imcys.bilibilias.core.domain.model

import com.imcys.bilibilias.core.datasource.CdnResource

data class EpisodeInfo(
    val bvid: String,
    val cid: Long,
    val desc: String,
    val cover: String,
    val title: String,
    val owner: Owner,
    val parts: List<EpisodePartInfo>,
    val video: List<StreamData>,
    val audio: List<StreamData>,
)

/**
 * [codecId] audio always 0, video include 7/avc 12/hevc 13/av1
 */
data class StreamData(
    val id: Int,
    val backupUrl: List<CdnResource>,
    val codecId: Int = 0,
    val description: String = ""
)

/**
 * [index] is the index of the episode in the series
 */
data class EpisodePartInfo(
    val cid: Long,
    val title: String,
    val index: Int
)

data class Owner(
    val id: Long,
    val avatarUrl: String,
    val name: String
)