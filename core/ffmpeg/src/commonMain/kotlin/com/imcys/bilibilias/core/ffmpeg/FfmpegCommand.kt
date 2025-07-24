package com.imcys.bilibilias.core.ffmpeg

interface FfmpegCommand {
    suspend fun execute(command: String)
    suspend fun execute(command: List<String>)
}

