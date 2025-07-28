package com.imcys.bilibilias.core.ffmpeg

import kotlinx.coroutines.flow.StateFlow

interface MediaMultiplexer {
    val isRunning: Boolean
    val progress: StateFlow<Int>
    suspend fun muxMedia(inputPaths: List<String>, outputPath: String)
}

expect fun createMediaMultiplexer(): MediaMultiplexer