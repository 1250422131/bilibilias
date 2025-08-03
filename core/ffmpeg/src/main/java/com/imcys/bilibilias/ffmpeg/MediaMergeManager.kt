package com.imcys.bilibilias.ffmpeg

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File

object MediaMergeManager {
    interface MediaMergeListener {
        fun onProgress(progress: Int)
        fun onError(errorMsg: String)
        fun onComplete()
    }

    suspend fun mergeVideoAndAudioSuspend(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: MediaMergeListener
    ): Result<String> = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            mergeVideoAndAudioInternal(videoPath, audioPath, outputPath, listener)
            Result.success("成功")
        } catch (e: Exception) {
            listener.onError(e.message ?: "合并失败")
            Result.failure(e)
        }
    }

    private fun mergeVideoAndAudioInternal(
        videoPath: String,
        audioPath: String,
        outputPath: String,
        listener: MediaMergeListener
    ) {
        try {
            val videoExtractor = MediaExtractor()
            val audioExtractor = MediaExtractor()
            videoExtractor.setDataSource(videoPath)
            audioExtractor.setDataSource(audioPath)

            val muxer = MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            var videoTrackIndex = -1
            var audioTrackIndex = -1

            // 选取视频轨道
            for (i in 0 until videoExtractor.trackCount) {
                val format = videoExtractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                if (mime != null && mime.startsWith("video/")) {
                    videoExtractor.selectTrack(i)
                    videoTrackIndex = muxer.addTrack(format)
                    break
                }
            }
            // 选取音频轨道
            for (i in 0 until audioExtractor.trackCount) {
                val format = audioExtractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                if (mime != null && mime.startsWith("audio/")) {
                    audioExtractor.selectTrack(i)
                    audioTrackIndex = muxer.addTrack(format)
                    break
                }
            }
            if (videoTrackIndex == -1 || audioTrackIndex == -1) {
                throw Exception("找不到音视频轨道")
            }
            muxer.start()

            val bufferSize = 1024 * 1024
            val buffer = ByteArray(bufferSize)
            val bufferInfo = android.media.MediaCodec.BufferInfo()

            // 获取总时长用于进度计算
            fun findTrackIndex(extractor: MediaExtractor, mimePrefix: String): Int {
                for (i in 0 until extractor.trackCount) {
                    val mime = extractor.getTrackFormat(i).getString(MediaFormat.KEY_MIME)
                    if (mime != null && mime.startsWith(mimePrefix)) return i
                }
                return -1
            }

            val videoTrackIdx = findTrackIndex(videoExtractor, "video/")
            val audioTrackIdx = findTrackIndex(audioExtractor, "audio/")
            val videoDuration =
                if (videoTrackIdx != -1) videoExtractor.getTrackFormat(videoTrackIdx)
                    .getLong(MediaFormat.KEY_DURATION) else 0L
            val audioDuration =
                if (audioTrackIdx != -1) audioExtractor.getTrackFormat(audioTrackIdx)
                    .getLong(MediaFormat.KEY_DURATION) else 0L
            val totalDuration = maxOf(videoDuration, audioDuration)
            var lastProgress = -1
            // 写入视频
            var videoDone = false
            while (!videoDone) {
                bufferInfo.offset = 0
                bufferInfo.size = videoExtractor.readSampleData(java.nio.ByteBuffer.wrap(buffer), 0)
                if (bufferInfo.size < 0) {
                    videoDone = true
                    bufferInfo.size = 0
                } else {
                    bufferInfo.presentationTimeUs = videoExtractor.sampleTime
                    // 只保留关键帧标志
                    bufferInfo.flags =
                        if ((videoExtractor.sampleFlags and android.media.MediaExtractor.SAMPLE_FLAG_SYNC) != 0) {
                            android.media.MediaCodec.BUFFER_FLAG_KEY_FRAME
                        } else 0
                    muxer.writeSampleData(
                        videoTrackIndex,
                        java.nio.ByteBuffer.wrap(buffer, 0, bufferInfo.size),
                        bufferInfo
                    )
                    videoExtractor.advance()
                    // 进度回调
                    if (totalDuration > 0) {
                        val progress = (bufferInfo.presentationTimeUs * 100 / totalDuration).toInt()
                            .coerceIn(0, 99)
                        if (progress != lastProgress) {
                            listener.onProgress(progress)
                            lastProgress = progress
                        }
                    }
                }
            }
            // 写入音频
            var audioDone = false
            while (!audioDone) {
                bufferInfo.offset = 0
                bufferInfo.size = audioExtractor.readSampleData(java.nio.ByteBuffer.wrap(buffer), 0)
                if (bufferInfo.size < 0) {
                    audioDone = true
                    bufferInfo.size = 0
                } else {
                    bufferInfo.presentationTimeUs = audioExtractor.sampleTime
                    bufferInfo.flags = 0 // 音频一般不需要关键帧标志
                    muxer.writeSampleData(
                        audioTrackIndex,
                        java.nio.ByteBuffer.wrap(buffer, 0, bufferInfo.size),
                        bufferInfo
                    )
                    audioExtractor.advance()
                    // 进度回调
                    if (totalDuration > 0) {
                        val progress = (bufferInfo.presentationTimeUs * 100 / totalDuration).toInt()
                            .coerceIn(0, 99)
                        if (progress != lastProgress) {
                            listener.onProgress(progress)
                            lastProgress = progress
                        }
                    }
                }
            }
            // 结束时100%
            listener.onProgress(100)
            muxer.stop()
            muxer.release()
            videoExtractor.release()
            audioExtractor.release()
            listener.onComplete()
        } catch (e: Exception) {
            throw e
        }
    }
}


object MediaProcessorFactory {
    enum class ProcessorType { FFMPEG, EXTRACTOR }

    fun create(type: ProcessorType): IMediaProcessor = when (type) {
        ProcessorType.FFMPEG -> FFmpegMediaProcessor()
        ProcessorType.EXTRACTOR -> MediaExtractorProcessor()
    }
}
