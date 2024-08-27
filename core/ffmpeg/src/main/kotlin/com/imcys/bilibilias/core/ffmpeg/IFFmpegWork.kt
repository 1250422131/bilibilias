package com.imcys.bilibilias.core.ffmpeg

interface IFFmpegWork {
    fun execute(
        template: String,
        outputUri: String,
        vararg contentSourcesUri: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    )
}
