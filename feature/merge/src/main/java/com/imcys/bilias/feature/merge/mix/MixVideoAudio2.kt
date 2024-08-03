package com.imcys.bilias.feature.merge.mix

import androidx.collection.CircularArray
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.imcys.common.di.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope

class MixVideoAudio2(@AppCoroutineScope private val scope: CoroutineScope) {
    private val readyWorker = CircularArray<MixVideoAndAudio>()
    private var runningWorker: MixVideoAndAudio? = null
    private var onStart: (String) -> Unit = { }
    private var onProgress: (Int) -> Unit = { }
    private var onComplete: (String) -> Unit = { }
    private var onError: (String) -> Unit = { }
    fun enqueue(
        data: MixVideoAndAudio,
        onStart: (String) -> Unit,
        onProgress: (Int) -> Unit,
        onComplete: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        readyWorker.addLast(data)
        this.onStart = onStart
        this.onProgress = onProgress
        this.onComplete = onComplete
        this.onError = onError
        promoteAndExecute()
    }

    private fun promoteAndExecute() {
        if (runningWorker != null) return
        runningWorker = readyWorker.popFirst()
        // 执行命令
        FFmpegCommand.runCmd(
            arrayOf(),
            object : IFFmpegCallBack {
                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onComplete() {
                    onComplete("")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    errorMsg?.let(onError)
                }

                override fun onProgress(progress: Int, pts: Long) {
                    onProgress(progress)
                }

                override fun onStart() {
                    onStart("")
                }
            }
        )
    }
}
