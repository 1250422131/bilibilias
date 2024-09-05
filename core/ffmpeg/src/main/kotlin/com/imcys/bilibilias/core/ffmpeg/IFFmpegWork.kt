package com.imcys.bilibilias.core.ffmpeg

interface IFFmpegWork {
    fun execute(
        template: String,
        outputUri: String,
        contentSourcesUri: Array<String>,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    )
}
