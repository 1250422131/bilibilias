package com.imcys.player.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

typealias Quality = Int

internal sealed interface PlayerUiState {
    data object Loading : PlayerUiState
    data object LoadFailed : PlayerUiState
    data class Success(
        val video: ImmutableMap<Quality, ImmutableList<PlayLinkInfo>>,
        val audio: ImmutableMap<Quality, ImmutableList<PlayLinkInfo>>,
        val duration: Int,
        val descriptionWithQuality: ImmutableMap<Quality, String>,
        val supportFormats: ImmutableMap<Quality, SupportFormats>,
        val defaultQuality: Int
    ) : PlayerUiState
}

internal data class SupportFormats(
    val quality: Quality,
    val description: String,
    val supportAV1: Boolean,
    val supportAVC1: Boolean,
    val supportHEV1: Boolean
)

internal data class PlayLinkInfo(
    val id: Int,
    val baseUrl: String,
    val backupUrl: ImmutableList<String>,
    val bandwidth: Int = 0,
    val mimeType: String = "mp4",
    val codecs: String = "",
    val codecId: Int = 0,
    val isAVC: Boolean,
    val isHEVC: Boolean,
    val isAV1: Boolean,
    val width: Int = 1920,
    val height: Int = 1080,
    val type: String = "video"
)
