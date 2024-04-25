package com.imcys.bilibilias.core.ffmpeg

import com.coder.ffmpeg.call.IFFmpegCallBack
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

    @Suppress("LongParameterList")
    fun runCommand(
        command: Array<String?>,
        onStart: () -> Unit = {},
        onProgress: (Float) -> Unit = {},
        onError: (String) -> Unit = {},
        onCancel: () -> Unit = {},
        onComplete: () -> Unit = {},
    ) {
        scope.launch {
            FFmpegCommand.runCmd(
                command,
                callback(onStart, onProgress, onError, onCancel, onComplete)
            )
        }
    }

    private fun callback(
        onStart: () -> Unit,
        onProgress: (Float) -> Unit,
        onError: (String) -> Unit,
        onCancel: () -> Unit,
        onComplete: () -> Unit
    ): IFFmpegCallBack {
        return object : IFFmpegCallBack {
            override fun onStart() {
                onStart()
            }

            override fun onProgress(progress: Int, pts: Long) {
                onProgress(progress / 100f)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                onError(errorMsg ?: "未知错误")
            }

            override fun onCancel() {
                onCancel()
            }

            override fun onComplete() {
                onComplete()
            }
        }
    }
}
