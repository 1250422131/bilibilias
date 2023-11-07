package com.imcys.player

import com.imcys.model.VideoFormatDash
import com.imcys.model.video.Page
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface PlayerUiState {
    data object Loading : PlayerUiState
    data object LoadFailed : PlayerUiState

    // 视频简介
    data class VideoIntro(
        val title: String,
        val pic: String,
        val decs: String,
        val pages: List<Page>,
        val aid: Long,
        val bvid: String,
        val cid: Long
    ) : PlayerUiState

    data class Success(
        val videoFormatDash: VideoFormatDash = VideoFormatDash(),
        val qualityDescription: ImmutableList<Pair<String, Int>> = persistentListOf()
    ) : PlayerUiState
}
