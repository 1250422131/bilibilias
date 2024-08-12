package com.imcys.bilibilias.feature.ffmpegaction

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.download.DefaultConfig
import com.imcys.bilibilias.core.logcat.logcat
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType

@Composable
fun FfmpegActionScreen(component: FfmpegActionComponent) {
    FfmpegActionContent()
}

@Composable
internal fun FfmpegActionContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "请务必选择对应的视频或者音频，需要注意BILIBILIAS并不能保证目前的储存位置有储存权限，因此，被合并的视频和音频应该尽可能在公共文件夹。",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
            )
            TextField(
                value = DefaultConfig.DEFAULT_COMMAND,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            )
            SectionResourceCard()
        }
    }
}

@Composable
internal fun SectionResourceCard() {
    var uri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberFilePickerLauncher(
        type = PickerType.File(listOf("mp4", "acc")),
        title = "hello",
    ) {
        uri = it?.uri
        logcat("file") { it?.name.toString() }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        uri?.let {
            Text(
                it.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        AsButton(
            onClick = { launcher.launch() },
        ) {
            Text("选择视频资源")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSectionResourceCard() {
    SectionResourceCard()
}

@Preview(showBackground = true)
@Composable
private fun PreviewFfmpegActionScreen() {
    FfmpegActionScreen(PreviewFfmpegActionComponent())
}
