package com.imcys.bilibilias.core.ffmpeg

interface IFFmpegWork {
    fun execute(
        command: Array<String>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )
}
