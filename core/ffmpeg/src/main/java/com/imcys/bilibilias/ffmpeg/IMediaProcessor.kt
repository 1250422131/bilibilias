package com.imcys.bilibilias.ffmpeg

interface IMediaProcessor {
    interface ProcessorListener {
        fun onProgress(progress: Int)
        fun onError(errorMsg: String)
        fun onComplete()
    }

    suspend fun mergeVideoAndAudioSuspend(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: ProcessorListener
    ): Result<String>
}
