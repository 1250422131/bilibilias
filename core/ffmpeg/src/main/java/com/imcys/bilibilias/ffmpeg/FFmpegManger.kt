package com.imcys.bilibilias.ffmpeg

import kotlinx.coroutines.suspendCancellableCoroutine

object FFmpegManger {

    /**
     * A native method that is implemented by the 'ffmpeg' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun getFFmpegVersion(): String

    interface FFmpegMergeListener {
        fun onProgress(progress: Int)
        fun onError(errorMsg: String)
        fun onComplete()
    }

    external fun mergeVideoAndAudio(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: FFmpegMergeListener
    )

    init {
        System.loadLibrary("ffmpeg")
    }

    suspend fun mergeVideoAndAudioSuspend(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: FFmpegMergeListener
    ): Result<String> = suspendCancellableCoroutine { cont ->
        mergeVideoAndAudio(videoPath, audioPath, outputPath, object : FFmpegMergeListener {
            override fun onProgress(progress: Int) {
                listener.onProgress(progress)
            }

            override fun onError(errorMsg: String) {
                if (cont.isActive) cont.resumeWith(Result.failure(Exception(errorMsg)))
            }

            override fun onComplete() {
                if (cont.isActive) cont.resume(Result.success("成功")) { cause, _, _ ->
                    Result.success(
                        cause.message
                    )
                }
            }
        })
    }
}