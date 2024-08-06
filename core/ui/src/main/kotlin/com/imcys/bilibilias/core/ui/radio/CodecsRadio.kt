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
            enable = true,
        )
        AsRadioButton(
            selected = codecsState.current == Codecs.H265,
            onClick = { codecsState.current = Codecs.H265 },
            text = { Text(text = "H265") },
            enable = true,
        )
        AsRadioButton(
            selected = codecsState.current == Codecs.H264,
            onClick = { codecsState.current = Codecs.H264 },
            text = { Text(text = "H264") },
            enable = true,
        )
    }
}

@Composable
fun rememberCodecsState(): CodecsState = remember { CodecsState() }

@Stable
class CodecsState {
    var current by mutableStateOf(Codecs.AV1)
        internal set
}

enum class Codecs(val codeid: Int) {
    AV1(13),
    H265(12), // hevc
    H264(7),
}
