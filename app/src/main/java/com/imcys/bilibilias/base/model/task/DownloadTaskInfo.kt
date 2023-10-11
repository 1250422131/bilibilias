package com.imcys.bilibilias.base.model.task

import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import com.imcys.bilibilias.base.utils.STATE_DOWNLOAD_WAIT
import com.imcys.bilibilias.common.data.download.entity.DownloadTask

// 下载任务类
data class DownloadTaskInfo(
    // 下载地址
    val url: String,
    // 下载文件保存路径
    var savePath: String,
    /**
     * 文件名
     */
    val filename: String = "",
    /**
     * 文件拓展名
     */
    val fileExtensionName: String = "",
    // 文件类型，0为视频，1为音频
    var fileType: Int,
    // 下载任务的其他参撒
    val downloadTaskDataBean: DownloadTaskDataBean,
    // 标识这个任务是否是一组任务的一部分
    var isGroupTask: Boolean = true,
    var payloadsType: Int = 0,
    // 下载状态
    var state: Int = STATE_DOWNLOAD_WAIT,
    // 定义当前任务的下载进度
    var progress: Double = 0.0,
    // 定义当前文件大小
    var fileSize: Double = 0.0,
    // 定义当前已经下载的大小
    var fileDlSize: Double = 0.0,
    // 定义当前任务的下载请求
    var call: DownloadTask? = null,

    val lastModificationTime: Long = 0,
    val cid: Long = 0,
    val pageTitle: String = "",
    val bvid: String = "",
    // 分辨率
    val qn: Int = 0,
)
