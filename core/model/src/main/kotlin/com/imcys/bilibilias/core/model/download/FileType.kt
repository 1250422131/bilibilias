package com.imcys.bilibilias.core.model.download

enum class FileType(val priority: Int, val extension: String) {
    VIDEO(99, ".mp4"),
    AUDIO(100, ".aac"),
}
