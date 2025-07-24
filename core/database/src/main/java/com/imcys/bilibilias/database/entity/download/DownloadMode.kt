package com.imcys.bilibilias.database.entity.download

/**
 * 下载模式
 */
enum class DownloadMode(val title: String) {
    AUDIO_VIDEO("音视频"), // 音视频
    VIDEO_ONLY("仅视频"),  // 仅视频
    AUDIO_ONLY("仅音频")   // 仅音频
}