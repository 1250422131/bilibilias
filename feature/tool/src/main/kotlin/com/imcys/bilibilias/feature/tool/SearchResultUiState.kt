package com.imcys.bilibilias.feature.tool

import com.imcys.bilibilias.core.model.bangumi.BangumiStreamUrl
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.model.video.VideoStreamUrl

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState
    data class Success(
        val aid: Long = 0,
        val bvid: String = "",
        val cid: Long = 0,
        val mid: Mid = 0,
        val collection: List<View> = emptyList(),
        val ownerFace: String = "",
        val ownerId: Long = 0,
    ) : SearchResultUiState {
        val isEmpty = aid == 0L && bvid.isEmpty()
    }
}

data class View(val cid: Long, val title: String, val videoStreamDesc: VideoStreamDesc)
data class VideoStreamDesc(val descriptionQuality: List<Description>)

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
