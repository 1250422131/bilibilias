package com.imcys.bilibilias.base.model.user

import com.imcys.bilibilias.home.ui.model.VideoBaseBean

data class DownloadTaskBean(
    val path: String,
    val videoTasks: MutableList<VideoTask> = mutableListOf(),
) {

    data class VideoDownloadProgressData(
        var fileSize: Double = 0.0,
        var fileDlSize: Double = 0.0,
        var progress: Int = 0,
    )

    data class VideoTask(
        val cid: String,
        val pageTitle: String,
        //分辨率
        val qn: String,
        //视频获取方式选择
        val fnval: String,
        //
        val platform: String,
        val videoBaseBean: VideoBaseBean,
        val downloadProgressData: VideoDownloadProgressData,
    ) {

    }
}
