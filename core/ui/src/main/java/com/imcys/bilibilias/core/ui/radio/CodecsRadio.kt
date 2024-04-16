package com.imcys.bilibilias.core.ui.radio

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.imcys.bilibilias.core.designsystem.component.AsRadioButton

@Composable
fun CodecsRadioGroup(codecsState: CodecsState) {
    Row {
        AsRadioButton(
            selected = codecsState.current == Codecs.AV1,
            onClick = { codecsState.current = Codecs.AV1 },
            text = { Text(text = "AV1") },
            enable = codecsState.enableAV1
        )
        AsRadioButton(
            selected = codecsState.current == Codecs.H265,
            onClick = { codecsState.current = Codecs.H265 },
            text = { Text(text = "H265") },
            enable = codecsState.enableH265
        )
        AsRadioButton(
            selected = codecsState.current == Codecs.H264,
            onClick = { codecsState.current = Codecs.H264 },
            text = { Text(text = "H264") },
            enable = codecsState.enableH264
        )
    }
}

@Composable
fun rememberCodecsState(
    enableAV1: Boolean,
    enableH265: Boolean,
    enableH264: Boolean,
): CodecsState {
    return remember(enableAV1, enableH265, enableH264) {
        CodecsState(enableAV1, enableH265, enableH264)
    }
}

@Stable
class CodecsState(
    val enableAV1: Boolean,
    val enableH265: Boolean,
    val enableH264: Boolean
) {
    var current by mutableStateOf(
        if (enableAV1) {
            Codecs.AV1
        } else if (enableH265) {
            Codecs.H265
        } else {
            Codecs.H264
        }
    )
        internal set
}

enum class Codecs(val codeid: Int) {
    AV1(13),
    H265(12), // hevc
    H264(7),
}
