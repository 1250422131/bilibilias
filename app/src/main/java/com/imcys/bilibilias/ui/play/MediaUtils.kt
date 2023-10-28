package com.imcys.bilibilias.ui.play

import android.content.Context
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.MimeTypes.getMediaMimeType
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.TransformationRequest
import androidx.media3.transformer.Transformer
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import timber.log.Timber
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.IjkExo2MediaPlayer

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun createMediaSource(
    dataSourceFactory: DataSource.Factory,
    url: String,
) = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
    MediaItem.Builder()
        .setUri(url)
        .build()
)

private const val TAG = "MediaCodecInfo"

/**
 *解码器列表：
 * 软解->OMX.google.aac.decoder
 * 软解->OMX.google.amrnb.decoder
 * 软解->OMX.google.amrwb.decoder
 * 软解->OMX.google.flac.decoder
 * 软解->OMX.google.g711.alaw.decoder
 * 软解->OMX.google.g711.mlaw.decoder
 * 软解->OMX.google.gsm.decoder
 * 软解->OMX.google.mp3.decoder
 * 软解->OMX.google.opus.decoder
 * 软解->OMX.google.raw.decoder
 * 软解->OMX.google.vorbis.decoder
 * 软解->OMX.google.h264.decoder
 * 软解->OMX.google.h263.decoder
 * 软解->OMX.google.hevc.decoder
 * 软解->OMX.google.mpeg4.decoder
 * 软解->OMX.google.vp8.decoder
 * 软解->OMX.google.vp9.decoder
 * 硬解->c2.android.aac.decoder
 * 硬解->c2.android.amrnb.decoder
 * 硬解->c2.android.amrwb.decoder
 * 硬解->c2.android.flac.decoder
 * 硬解->c2.android.g711.alaw.decoder
 * 硬解->c2.android.g711.mlaw.decoder
 * 硬解->c2.android.gsm.decoder
 * 硬解->c2.android.mp3.decoder
 * 硬解->c2.android.opus.decoder
 * 硬解->c2.android.raw.decoder
 * 硬解->c2.android.vorbis.decoder
 * 硬解->OMX.hisi.video.decoder.avc
 * 硬解->OMX.hisi.video.decoder.hevc
 * 硬解->OMX.hisi.video.decoder.mpeg2
 * 硬解->OMX.hisi.video.decoder.mpeg4
 * 硬解->OMX.hisi.video.decoder.vp8
 * 硬解->OMX.hisi.video.decoder.vp9
 * 硬解->c2.android.av1.decoder
 * 硬解->c2.android.avc.decoder
 * 硬解->c2.android.h263.decoder
 * 硬解->c2.android.hevc.decoder
 * 硬解->c2.android.mpeg4.decoder
 * 硬解->c2.android.vp8.decoder
 * 硬解->c2.android.vp9.decoder
 * 编码器列表：
 * 软编->OMX.google.aac.encoder
 * 软编->OMX.google.amrnb.encoder
 * 软编->OMX.google.amrwb.encoder
 * 软编->OMX.google.flac.encoder
 * 软编->OMX.google.h264.encoder
 * 软编->OMX.google.h263.encoder
 * 软编->OMX.google.mpeg4.encoder
 * 软编->OMX.google.vp8.encoder
 * 软编->OMX.google.vp9.encoder
 * 硬编->c2.android.aac.encoder
 * 硬编->c2.android.amrnb.encoder
 * 硬编->c2.android.amrwb.encoder
 * 硬编->c2.android.flac.encoder
 * 硬编->c2.android.opus.encoder
 * 硬编->OMX.hisi.video.encoder.avc
 * 硬编->OMX.hisi.video.encoder.hevc
 * 硬编->c2.android.avc.encoder
 * 硬编->c2.android.h263.encoder
 * 硬编->c2.android.hevc.encoder
 * 硬编->c2.android.mpeg4.encoder
 * 硬编->c2.android.vp8.encoder
 * 硬编->c2.android.vp9.encoder
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

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun ss(context: Context) {
    MediaItem.Builder()
        .setMimeType(MimeTypes.AUDIO_AAC)
        .build()
    val transformerListener: Transformer.Listener =
        object : Transformer.Listener {
            override fun onCompleted(composition: Composition, result: ExportResult) {
            }

            override fun onError(
                composition: Composition,
                result: ExportResult,
                exception: ExportException
            ) {
            }
        }
    val inputMediaItem = MediaItem.fromUri("path_to_input_file")
    val editedMediaItem = EditedMediaItem.Builder(inputMediaItem).build()
    val transformer = Transformer.Builder(context)
        .setTransformationRequest(
            TransformationRequest.Builder().setVideoMimeType(MimeTypes.VIDEO_H265).build()
        )
        .addListener(transformerListener)
        .build()

    transformer.start(editedMediaItem, "")
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

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun getFormat2(gsy: GSYVideoPlayer) {
    if (gsy.gsyVideoManager.player is Exo2PlayerManager) {
        val player = gsy.gsyVideoManager.player.mediaPlayer as? IjkExo2MediaPlayer
        val mappedTrackInfo = player?.trackSelector?.currentMappedTrackInfo ?: return
        Debuger.enable()
        for (i in 0 until mappedTrackInfo.rendererCount) {
            val rendererTrackGroups = mappedTrackInfo.getTrackGroups(i)
            if (C.TRACK_TYPE_AUDIO == mappedTrackInfo.getRendererType(i)) { // 判断是否是音轨
                for (j in 0 until rendererTrackGroups.length) {
                    val trackGroup = rendererTrackGroups[j]
                    // 获取到 Format
                    Debuger.printfError("####### Audio" + trackGroup.getFormat(0).toString() + " #######")
                }
            }
            if (C.TRACK_TYPE_VIDEO == mappedTrackInfo.getRendererType(i)) { // 判断是否是音轨
                for (j in 0 until rendererTrackGroups.length) {
                    val trackGroup = rendererTrackGroups[j]
                    // 获取到 Format
                    Debuger.printfError("####### Video" + trackGroup.getFormat(0).toString() + " #######")
                }
            }
        }
    }
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun getFormat(gsy: GSYVideoPlayer, tree: Timber.Tree) {
    val trackSelector = DefaultTrackSelector(gsy.context.applicationContext)
    val mappedTrackInfo = trackSelector.currentMappedTrackInfo ?: return
    Debuger.enable()
    for (i in 0 until mappedTrackInfo.rendererCount) {
        val rendererTrackGroups = mappedTrackInfo.getTrackGroups(i)
        if (C.TRACK_TYPE_AUDIO == mappedTrackInfo.getRendererType(i)) { // 判断是否是音轨
            for (j in 0 until rendererTrackGroups.length) {
                val trackGroup = rendererTrackGroups[j]
                // /获取到 Format
                tree.d("####### Audio${trackGroup.getFormat(0)}#######")
                Debuger.printfError("####### Audio" + trackGroup.getFormat(0).toString() + " #######")
            }
        }
        if (C.TRACK_TYPE_VIDEO == mappedTrackInfo.getRendererType(i)) { // 判断是否是音轨
            for (j in 0 until rendererTrackGroups.length) {
                val trackGroup = rendererTrackGroups[j]
                // /获取到 Format
                tree.d("####### Video${trackGroup.getFormat(0)}#######")
                Debuger.printfError("####### Video" + trackGroup.getFormat(0).toString() + " #######")
                val mediaMimeType = getMediaMimeType(trackGroup.getFormat(0).toString())
            }
        }
    }
}
