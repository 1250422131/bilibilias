package com.imcys.bilibilias.tool.util

import com.coder.ffmpeg.annotation.CodecProperty
import com.coder.ffmpeg.annotation.MediaAttribute
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.model.CodecInfo
import com.imcys.network.download.AUDIO_M4S
import com.imcys.network.download.VIDEO_M4S

fun getAudioCodecInfo(path: String): CodecInfo? =
    FFmpegCommand.getCodecInfo(path, CodecProperty.AUDIO)

fun getVideoCodecInfo(path: String): CodecInfo? =
    FFmpegCommand.getCodecInfo(path, CodecProperty.VIDEO)

fun getVideoBitRate(path: String): Int? =
    FFmpegCommand.getMediaInfo(path, MediaAttribute.VIDEO_BIT_RATE)

fun getVideoFPS(path: String): Int? = FFmpegCommand.getMediaInfo(path, MediaAttribute.FPS)
fun getAudioBitRate(path: String): Int? =
    FFmpegCommand.getMediaInfo(path, MediaAttribute.AUDIO_BIT_RATE)

fun getAudioSampleRate(path: String): Int? =
    FFmpegCommand.getMediaInfo(path, MediaAttribute.SAMPLE_RATE)

/**
 * 7	AVC 编码	8K
 * 12	HEVC 编码
 * 13	AV1 编码
 */
fun isVideo(path: String): Boolean {
    return path.contains(VIDEO_M4S)
}

fun isAudio(path: String): Boolean {
    return path.contains(AUDIO_M4S)
}
