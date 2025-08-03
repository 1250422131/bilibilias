package com.imcys.bilibilias.ffmpeg

import kotlinx.coroutines.suspendCancellableCoroutine


class FFmpegMediaProcessor : IMediaProcessor {
    override suspend fun mergeVideoAndAudioSuspend(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: IMediaProcessor.ProcessorListener
    ): Result<String> = suspendCancellableCoroutine { cont ->
        FFmpegManger.mergeVideoAndAudio(
            videoPath,
            audioPath,
            outputPath,
            object : FFmpegManger.FFmpegMergeListener {
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
