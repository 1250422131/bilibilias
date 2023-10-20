package com.imcys.bilibilias.ui.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.data.download.entity.DownloadFileType

/**
 * todo bug 这个类在搜索多个视频有几率cid与bvid对应不上
 */
class DownloadOptionsStateHolders {
    /**
     * 视频清晰度描述
     * "真彩 HDR","超清 4K","高清 1080P60","高清 1080P","高清 720P60","清晰 480P","流畅 360P"
     *
     */
    var videoFormatDescription by mutableStateOf("")

    /**
     * 视频清晰度
     * 125,120,116,80,64,32,16
     */
    var videoQuality = 0

    /**
     * 要下载的文件集合
     */
    var subset = mutableStateListOf<VideoDetails.Page>()

    /**
     * 音频质量
     */
    var audioFormatDescription by mutableIntStateOf(0)
    var audioQuality = 0

    var requireDownloadFileType by mutableStateOf(DownloadFileType.VideoAndAudio)

    // 下载工具
    var toolType = DownloadToolType.BUILTIN
    override fun toString(): String {
        return "DownloadOptionsStateHolders(videoFormatDescription='$videoFormatDescription', videoQuality=$videoQuality, subset=$subset, audioFormatDescription=$audioFormatDescription, audioQuality=$audioQuality, requireDownloadFileType=$requireDownloadFileType, toolType=$toolType)"
    }
}

@Composable
fun rememberDownloadOptions(): DownloadOptionsStateHolders {
    return remember {
        DownloadOptionsStateHolders()
    }
}
