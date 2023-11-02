package com.imcys.bilibilias.common.base.utils.file

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer

object VideoMergerUtils {

    private const val videoMimeType = MediaFormat.MIMETYPE_VIDEO_AVC
    private const val audioMimeType = MediaFormat.MIMETYPE_AUDIO_AAC
    private const val timeoutUs = 5000000L


    fun merge(videoFilePath: String, audioFilePath: String, outputFilePath: String) {
        val videoExtractor = MediaExtractor()
        val audioExtractor = MediaExtractor()

        videoExtractor.setDataSource(videoFilePath)
        var videoFormat = videoExtractor.getTrackFormat(0)
        audioExtractor.setDataSource(audioFilePath)
        var audioFormat = audioExtractor.getTrackFormat(0)
        val videoDecoder = MediaCodec.createDecoderByType(videoMimeType)
        val audioDecoder = MediaCodec.createDecoderByType(audioMimeType)

        val videoEncoder = MediaCodec.createEncoderByType(videoMimeType)
        val audioEncoder = MediaCodec.createEncoderByType(audioMimeType)

        val muxer = MediaMuxer(outputFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        val videoTrackIndex = muxer.addTrack(videoEncoder.outputFormat)
        val audioTrackIndex = muxer.addTrack(audioEncoder.outputFormat)
        muxer.start()

        // 设置MediaExtractor和MediaCodec
        videoExtractor.setDataSource(videoFilePath)
        audioExtractor.setDataSource(audioFilePath)
        videoDecoder.configure(videoFormat, null, null, 0)
        audioDecoder.configure(audioFormat, null, null, 0)
        videoFormat = videoDecoder.outputFormat
        audioFormat = audioDecoder.outputFormat
        videoEncoder.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        audioEncoder.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        // 开始合并
        videoDecoder.start()
        audioDecoder.start()
        videoEncoder.start()
        audioEncoder.start()

        while (true) {
            // 从视频文件中读取帧数据
            val videoInputBufferIndex = videoDecoder.dequeueInputBuffer(timeoutUs)
            if (videoInputBufferIndex >= 0) {
                val videoInputBuffer = videoDecoder.getInputBuffer(videoInputBufferIndex)
                val size = videoInputBuffer?.let { videoExtractor.readSampleData(it, 0) }
                if (size!! < 0) {
                    videoDecoder.queueInputBuffer(
                        videoInputBufferIndex,
                        0,
                        0,
                        0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    )
                } else {
                    videoDecoder.queueInputBuffer(
                        videoInputBufferIndex,
                        0,
                        size,
                        videoExtractor.sampleTime,
                        0
                    )
                    videoExtractor.advance()
                }
            }

// 从音频文件中读取帧数据
            val audioInputBufferIndex = audioDecoder.dequeueInputBuffer(timeoutUs)
            if (audioInputBufferIndex >= 0) {
                val audioInputBuffer = audioDecoder.getInputBuffer(audioInputBufferIndex)
                val size = audioInputBuffer?.let { audioExtractor.readSampleData(it, 0) }
                if (size!! < 0) {
                    audioDecoder.queueInputBuffer(
                        audioInputBufferIndex,
                        0,
                        0,
                        0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    )
                } else {
                    audioDecoder.queueInputBuffer(
                        audioInputBufferIndex,
                        0,
                        size,
                        audioExtractor.sampleTime,
                        0
                    )
                    audioExtractor.advance()
                }
            }

// 从解码器中读取帧数据
            val videoBufferInfo = MediaCodec.BufferInfo()
            val videoOutputBufferIndex =
                videoDecoder.dequeueOutputBuffer(videoBufferInfo, timeoutUs)
            if (videoOutputBufferIndex >= 0) {
                // 将解码后的帧数据传递给编码器
                val videoOutputBuffer = videoDecoder.getOutputBuffer(videoOutputBufferIndex)
                val videoInputBufferIndex = videoEncoder.dequeueInputBuffer(timeoutUs)
                if (videoInputBufferIndex >= 0) {
                    val videoInputBuffer = videoEncoder.getInputBuffer(videoInputBufferIndex)
                    videoInputBuffer!!.clear()
                    videoInputBuffer.put(videoOutputBuffer)
                    videoEncoder.queueInputBuffer(
                        videoInputBufferIndex,
                        0,
                        videoBufferInfo.size,
                        videoBufferInfo.presentationTimeUs,
                        0
                    )
                }
                videoDecoder.releaseOutputBuffer(videoOutputBufferIndex, false)
            }


            val audioBufferInfo = MediaCodec.BufferInfo()
            val audioOutputBufferIndex =
                audioDecoder.dequeueOutputBuffer(audioBufferInfo, timeoutUs)
            if (audioOutputBufferIndex >= 0) {
                // 将解码后的帧数据传递给编码器
                val audioOutputBuffer = audioDecoder.getOutputBuffer(audioOutputBufferIndex)
                val audioInputBufferIndex = audioEncoder.dequeueInputBuffer(timeoutUs)
                if (audioInputBufferIndex >= 0) {
                    val audioInputBuffer = audioEncoder.getInputBuffer(audioInputBufferIndex)
                    audioInputBuffer!!.clear()
                    audioInputBuffer.put(audioOutputBuffer)
                    audioEncoder.queueInputBuffer(
                        audioInputBufferIndex,
                        0,
                        audioBufferInfo.size,
                        audioBufferInfo.presentationTimeUs,
                        0
                    )
                }
                audioDecoder.releaseOutputBuffer(audioOutputBufferIndex, false)
            }

// 从编码器中读取帧数据
            val videoEncoderBufferInfo = MediaCodec.BufferInfo()
            val videoEncoderOutputBufferIndex =
                videoEncoder.dequeueOutputBuffer(videoEncoderBufferInfo, timeoutUs)
            if (videoEncoderOutputBufferIndex >= 0) {
                // 将编码后的帧数据写入输出文件
                val videoEncoderOutputBuffer =
                    videoEncoder.getOutputBuffer(videoEncoderOutputBufferIndex)
                videoEncoderOutputBuffer?.let {
                    muxer.writeSampleData(
                        videoTrackIndex,
                        it, videoEncoderBufferInfo
                    )
                }
                videoEncoder.releaseOutputBuffer(videoEncoderOutputBufferIndex, false)
            }

// 从编码器中读取帧数据
            val audioEncoderBufferInfo = MediaCodec.BufferInfo()
            val audioEncoderOutputBufferIndex =
                audioEncoder.dequeueOutputBuffer(audioEncoderBufferInfo, timeoutUs)

            if (audioEncoderOutputBufferIndex >= 0) {
                // 将编码后的帧数据写入输出文件
                val audioEncoderOutputBuffer =
                    audioEncoder.getOutputBuffer(audioEncoderOutputBufferIndex)
                audioEncoderOutputBuffer?.let {
                    muxer.writeSampleData(
                        audioTrackIndex,
                        it, audioEncoderBufferInfo
                    )
                }
                audioEncoder.releaseOutputBuffer(audioEncoderOutputBufferIndex, false)
            }

// 判断是否完成合并
            if ((videoBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0 && (audioBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                break
            }


        }
    }
}