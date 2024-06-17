package com.imcys.bilibilias.core.model.download

enum class FileType(val priority: Int, val filename: String) {
    VIDEO(99, "video.mp4"),
    AUDIO(100, "audio.aac"),
}
