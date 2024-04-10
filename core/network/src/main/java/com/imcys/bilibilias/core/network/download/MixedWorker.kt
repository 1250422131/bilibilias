package com.imcys.bilibilias.core.network.download

import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.jni.FFmpegConfig
import com.coder.ffmpeg.utils.FFmpegUtils
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.network.BuildConfig
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MixedWorker @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
) {
    init {
        if (BuildConfig.DEBUG){
            FFmpegConfig.setDebug(true)
        }
//        FFmpegCommand.runCmd()
//        FFmpegUtils.mixAudioVideo()
    }
    fun mix(it: Task, task: Task) {

    }


}

data class MixTask(val audio: String, val video: String, val outputPath: String)