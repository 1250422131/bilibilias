package com.imcys.common.utils

import android.media.MediaCodecInfo
import android.media.MediaCodecList
import androidx.annotation.OptIn
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import timber.log.Timber

/**
 * 解码器列表： 软解->OMX.google.aac.decoder
 * 软解->OMX.google.amrnb.decoder 软解->OMX.google.amrwb.decoder
 * 软解->OMX.google.flac.decoder 软解->OMX.google.g711.alaw.decoder
 * 软解->OMX.google.g711.mlaw.decoder 软解->OMX.google.gsm.decoder
 * 软解->OMX.google.mp3.decoder 软解->OMX.google.opus.decoder
 * 软解->OMX.google.raw.decoder 软解->OMX.google.vorbis.decoder
 * 软解->OMX.google.h264.decoder 软解->OMX.google.h263.decoder
 * 软解->OMX.google.hevc.decoder 软解->OMX.google.mpeg4.decoder
 * 软解->OMX.google.vp8.decoder 软解->OMX.google.vp9.decoder
 * 硬解->c2.android.aac.decoder 硬解->c2.android.amrnb.decoder
 * 硬解->c2.android.amrwb.decoder 硬解->c2.android.flac.decoder
 * 硬解->c2.android.g711.alaw.decoder 硬解->c2.android.g711.mlaw.decoder
 * 硬解->c2.android.gsm.decoder 硬解->c2.android.mp3.decoder
 * 硬解->c2.android.opus.decoder 硬解->c2.android.raw.decoder
 * 硬解->c2.android.vorbis.decoder 硬解->OMX.hisi.video.decoder.avc
 * 硬解->OMX.hisi.video.decoder.hevc 硬解->OMX.hisi.video.decoder.mpeg2
 * 硬解->OMX.hisi.video.decoder.mpeg4 硬解->OMX.hisi.video.decoder.vp8
 * 硬解->OMX.hisi.video.decoder.vp9 硬解->c2.android.av1.decoder
 * 硬解->c2.android.avc.decoder 硬解->c2.android.h263.decoder
 * 硬解->c2.android.hevc.decoder 硬解->c2.android.mpeg4.decoder
 * 硬解->c2.android.vp8.decoder 硬解->c2.android.vp9.decoder 编码器列表：
 * 软编->OMX.google.aac.encoder 软编->OMX.google.amrnb.encoder
 * 软编->OMX.google.amrwb.encoder 软编->OMX.google.flac.encoder
 * 软编->OMX.google.h264.encoder 软编->OMX.google.h263.encoder
 * 软编->OMX.google.mpeg4.encoder 软编->OMX.google.vp8.encoder
 * 软编->OMX.google.vp9.encoder 硬编->c2.android.aac.encoder
 * 硬编->c2.android.amrnb.encoder 硬编->c2.android.amrwb.encoder
 * 硬编->c2.android.flac.encoder 硬编->c2.android.opus.encoder
 * 硬编->OMX.hisi.video.encoder.avc 硬编->OMX.hisi.video.encoder.hevc
 * 硬编->c2.android.avc.encoder 硬编->c2.android.h263.encoder
 * 硬编->c2.android.hevc.encoder 硬编->c2.android.mpeg4.encoder
 * 硬编->c2.android.vp8.encoder 硬编->c2.android.vp9.encoder
 */
fun getMediaCodecInfo() {
    val list = MediaCodecList(MediaCodecList.REGULAR_CODECS)
    val supportCodes = list.codecInfos
    Timber.i("解码器列表：")
    for (codec in supportCodes) {
        if (!codec.isEncoder) {
            val name = codec.name
            if (name.startsWith("OMX.google")) {
                Timber.i("软解->$name")
            }
        }
    }
    for (codec in supportCodes) {
        if (!codec.isEncoder) {
            val name = codec.name
            if (!name.startsWith("OMX.google")) {
                Timber.i("硬解->$name")
            }
        }
    }
    Timber.i("编码器列表：")
    for (codec in supportCodes) {
        if (codec.isEncoder) {
            val name = codec.name
            if (name.startsWith("OMX.google")) {
                Timber.i("软编->$name")
            }
        }
    }
    for (codec in supportCodes) {
        if (codec.isEncoder) {
            val name = codec.name
            if (!name.startsWith("OMX.google")) {
                Timber.i("硬编->$name")
            }
        }
    }
}

fun isH265HardwareDecoderSupport(): Boolean {
    val codecList = MediaCodecList(0)
    val codecInfos: Array<MediaCodecInfo> = codecList.codecInfos
    for (i in codecInfos.indices) {
        val codecInfo: MediaCodecInfo = codecInfos[i]
        if (!codecInfo.isEncoder && (codecInfo.name.contains("hevc") && !isSoftwareCodec(codecInfo.name))) {
            return true
        }
    }
    return false
}

fun isSoftwareCodec(codecName: String): Boolean {
    if (codecName.startsWith("OMX.google.")) {
        return true
    }
    return !codecName.startsWith("OMX.")
}

object MediaUtils {
    @OptIn(UnstableApi::class)
    fun getMimeType(codec: String) = MimeTypes.getMediaMimeType(codec)
}
