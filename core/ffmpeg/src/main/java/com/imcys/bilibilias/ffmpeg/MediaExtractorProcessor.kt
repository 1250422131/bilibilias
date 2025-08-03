package com.imcys.bilibilias.ffmpeg

class MediaExtractorProcessor : IMediaProcessor {
    override suspend fun mergeVideoAndAudioSuspend(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: IMediaProcessor.ProcessorListener
    ): Result<String> = MediaMergeManager.mergeVideoAndAudioSuspend(
        videoPath = videoPath,
        audioPath = audioPath,
        outputPath = outputPath,
        listener = object : MediaMergeManager.MediaMergeListener {
            override fun onProgress(progress: Int) {
                listener.onProgress(progress)
            }

            override fun onError(errorMsg: String) {
                listener.onError(errorMsg)
            }

            override fun onComplete() {
                listener.onComplete()
            }
        }
    )
}