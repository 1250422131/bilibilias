package com.imcys.network.download

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.imcys.bilibilias.common.data.download.entity.DownloadFileType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * todo bug 这个类在搜索多个视频有几率cid与bvid对应不上
 */
@Singleton
class DownloadListHolders @Inject constructor() {

    /**
     * 视频清晰度
     * 125,120,116,80,64,32,16
     */
    var videoQuality = 0

    /**
     * 要下载的文件集合
     */
    var subset = mutableStateListOf<com.imcys.model.VideoDetails.Page>()

    var requireDownloadFileType by mutableStateOf(DownloadFileType.VideoAndAudio)

    // 下载工具
    var toolType = DownloadToolType.BUILTIN
    override fun toString(): String {
        return "DownloadListHolders(videoQuality=$videoQuality, subset=$subset, requireDownloadFileType=$requireDownloadFileType, toolType=$toolType)"
    }
}

