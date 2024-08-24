package com.imcys.bilibilias.core.download.media

import android.media.MediaCodecList
import android.media.MediaCodecList.ALL_CODECS

object MediaCodecUtil {
    fun isH265HardwareDecoderSupport(): Boolean {
        val codecList = MediaCodecList(ALL_CODECS)
        val codecInfos = codecList.codecInfos
        for (i in codecInfos.indices) {
            val codecInfo = codecInfos[i]
            if (!codecInfo.isEncoder &&
                (
                    codecInfo.name.contains("hevc") &&
                        !isSoftwareCodec(codecInfo.name)
                    )
            ) {
                return true
            }
        }
        return false
    }

    fun isSoftwareCodec(codecName: String): Boolean {
        if (codecName.startsWith("OMX.google.")) {
            return true
        }

        if (codecName.startsWith("OMX.")) {
            return false
        }

        return true
    }
}
