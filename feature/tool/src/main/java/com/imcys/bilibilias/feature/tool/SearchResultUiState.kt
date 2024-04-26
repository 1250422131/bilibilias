package com.imcys.bilibilias.feature.tool

import com.imcys.bilibilias.core.model.bangumi.BangumiStreamUrl
import com.imcys.bilibilias.core.model.video.SupportFormat
import com.imcys.bilibilias.core.model.video.VideoStreamUrl

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState
    data class Success(
        val aid: Long,
        val bvid: String,
        val cid: Long,
        val collection: List<View>,
    ) : SearchResultUiState
}

data class View(val cid: Long, val title: String, val videoStreamDesc: VideoStreamDesc)
data class VideoStreamDesc(val descriptionQuality: List<Description>, )

data class Description(val desc: String, val quality: Int)
data class Codecs(val quality: Int, val useAV1: Boolean, val useH264: Boolean, val useH265: Boolean)

fun VideoStreamUrl.mapToVideoStreamDesc(): VideoStreamDesc {
    val selectableDescription = acceptDescription.zip(acceptQuality).map {
        Description(it.first, it.second)
    }
    return VideoStreamDesc(selectableDescription)
}

fun BangumiStreamUrl.mapToVideoStreamDesc(): VideoStreamDesc {
    val selectableDescription = videoInfo.acceptDescription.zip(videoInfo.acceptQuality).map {
        Description(it.first, it.second)
    }
    return VideoStreamDesc(selectableDescription)
}
