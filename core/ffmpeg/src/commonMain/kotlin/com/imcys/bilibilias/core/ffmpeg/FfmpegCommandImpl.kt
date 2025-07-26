package com.imcys.bilibilias.core.ffmpeg

@Suppress("FunctionName")
fun FfmpegCommandExecutor(): FfmpegCommand {
    return FfmpegCommandImpl()
}

internal expect class FfmpegCommandImpl() : FfmpegCommand {
    override suspend fun execute(command: String)
    override suspend fun execute(command: List<String>)
}