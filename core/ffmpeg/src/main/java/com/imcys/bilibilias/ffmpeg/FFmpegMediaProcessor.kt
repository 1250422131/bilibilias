package com.imcys.bilibilias.ffmpeg

import kotlinx.coroutines.suspendCancellableCoroutine


class FFmpegMediaProcessor : IMediaProcessor {
    override suspend fun mergeVideoAndAudioSuspend(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: IMediaProcessor.ProcessorListener
    ): Result<String> = suspendCancellableCoroutine { cont -> }
}
