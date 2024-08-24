package com.imcys.bilibilias.feature.tool

import com.imcys.bilibilias.core.model.bangumi.BangumiStreamUrl
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.model.video.Sources
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
    ) : SearchResultUiState
}

data class Format(
    val formatId: Int,
    val url: String,
    val codecId: Int,
    val codecs: String,

    val mimeType: String,
    val bandwidth: Int,
    val width: Int = 0,
    val height: Int = 0,

    val sar: String = "",
    val fps: String = "",
)

data class View(val cid: Long, val title: String, val videoStreamDesc: VideoStreamDesc)
data class VideoStreamDesc(
    val descriptionQuality: List<Description>,
    val videos: List<Format> = emptyList(),
    val audios: List<Format> = emptyList(),
)

data class Description(val desc: String, val quality: Int)

fun VideoStreamUrl.mapToVideoStreamDesc(): VideoStreamDesc {
    val selectableDescription = acceptDescription.zip(acceptQuality).map {
        Description(it.first, it.second)
    }

    val vFormat = dash.video.mapToFormat()
    val aFormat = dash.audio.mapToFormat()

    return VideoStreamDesc(selectableDescription, vFormat, aFormat)
}

fun BangumiStreamUrl.mapToVideoStreamDesc(): VideoStreamDesc {
    val selectableDescription = videoInfo.acceptDescription.zip(videoInfo.acceptQuality).map {
        Description(it.first, it.second)
    }

    val vFormat = videoInfo.dash.video.mapToFormat()
    val aFormat = videoInfo.dash.audio.mapToFormat()
    return VideoStreamDesc(selectableDescription, vFormat, aFormat)
}

fun List<Sources>.mapToFormat() = map {
    Format(
        formatId = it.id,
        url = it.baseUrl,
        codecId = it.codecid,
        codecs = it.codecs,
        mimeType = it.mimeType,
        bandwidth = it.bandwidth,
        width = it.width,
        height = it.height,
        sar = it.sar,
        fps = it.frameRate,
    )
}
