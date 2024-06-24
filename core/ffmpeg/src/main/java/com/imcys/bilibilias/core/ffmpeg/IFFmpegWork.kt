package com.imcys.bilibilias.core.ffmpeg

interface IFFmpegWork {
    fun execute(
        command: Array<String>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )

    fun execute(
        inputVideo: String,
        inputAudio: String,
        output: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )
}
