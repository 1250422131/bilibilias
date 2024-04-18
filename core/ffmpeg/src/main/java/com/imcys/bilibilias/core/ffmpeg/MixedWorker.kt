package com.imcys.bilibilias.core.ffmpeg

import com.coder.ffmpeg.call.CommonCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.jni.FFmpegConfig
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class FFmpegWorker @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
) {
    init {
        if (BuildConfig.DEBUG) {
            FFmpegConfig.setDebug(true)
        }
    }

    fun runCommand(command: Array<String?>, task: FFmpegTask) {
        scope.launch {
            FFmpegCommand.runCmd(command, callback(task))
        }
    }

    private fun callback(task: FFmpegTask): CommonCallBack {
        return object : CommonCallBack() {
            override fun onCancel() {
                task.state = FFmpegTask.Cancel
            }

            override fun onComplete() {
                task.state = FFmpegTask.Complete
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                task.state = FFmpegTask.Error
            }

            override fun onProgress(progress: Int, pts: Long) {
                task.progress = progress / 100f
                task.progress2.tryEmit(progress / 100f)
            }

            override fun onStart() {
                task.state = FFmpegTask.Start
            }
        }
    }
}
