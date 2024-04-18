package com.imcys.bilibilias.core.ffmpeg

import kotlinx.coroutines.flow.MutableStateFlow

data class FFmpegTask(val audio: String, val video: String, val outputPath: String) {
    var state = Idle
    var progress = 0f
    var progress2 = MutableStateFlow(0f)

    companion object {
        const val Idle = 1
        const val Start = 2
        const val Cancel = 3
        const val Error = 4
        const val Complete = 5
    }
}
