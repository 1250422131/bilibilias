
import com.imcys.bilibilias.core.model.bangumi.BangumiStreamUrl
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
data class VideoStreamDesc(
    val descriptionQuality: List<Description>,
    val supportCodecs: List<Codecs>
)

data class Description(val desc: String, val quality: Int)
data class Codecs(val quality: Int, val useAV1: Boolean, val useH264: Boolean, val useH265: Boolean)

fun VideoStreamUrl.mapToVideoStreamDesc(): VideoStreamDesc {
    val selectableDescription = acceptDescription.zip(acceptQuality).map {
        Description(it.first, it.second)
    }
    val supportCodecs = supportFormats.map { supportFormat ->
        val av01 = supportFormat.codecs.any { it.startsWith("av01") }
        val h264 = supportFormat.codecs.any { it.startsWith("avc1") }
        val h265 = supportFormat.codecs.any { it.startsWith("hev1") }
        Codecs(supportFormat.quality, av01, h264, h265)
    }
    return VideoStreamDesc(selectableDescription, supportCodecs)
}

fun BangumiStreamUrl.mapToVideoStreamDesc(): VideoStreamDesc {
    val selectableDescription = videoInfo.acceptDescription.zip(videoInfo.acceptQuality).map {
        Description(it.first, it.second)
    }
    val supportCodecs = videoInfo.supportFormats.map { supportFormat ->
        val av01 = supportFormat.codecs.any { it.startsWith("av01") }
        val h264 = supportFormat.codecs.any { it.startsWith("avc1") }
        val h265 = supportFormat.codecs.any { it.startsWith("hev1") }
        Codecs(supportFormat.quality, av01, h264, h265)
    }
    return VideoStreamDesc(selectableDescription, supportCodecs)
}

data class DownloadFileRequest(val aid: Long, val bvid: String, val cid: Long, val quality: Int)
