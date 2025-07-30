package com.imcys.bilibilias.core.ffmpeg

actual fun createMediaMultiplexer(): MediaMultiplexer {
    return FfmpegMediaMultiplexer()
}