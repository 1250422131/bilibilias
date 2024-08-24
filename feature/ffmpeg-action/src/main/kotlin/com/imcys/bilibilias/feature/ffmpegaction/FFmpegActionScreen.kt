package com.imcys.bilibilias.feature.ffmpegaction

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.feature.ffmpegaction.Action.CreateNewFile
import com.imcys.bilibilias.feature.ffmpegaction.Action.ExecuteCommand
import com.imcys.bilibilias.feature.ffmpegaction.Action.UpdateAudioResource
import com.imcys.bilibilias.feature.ffmpegaction.Action.UpdateVideoResource
import io.github.aakira.napier.Napier
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType

@Composable
fun FfmpegActionScreen(component: FfmpegActionComponent) {
    val model by component.models.collectAsState()
    FfmpegActionContent(model)
}

@Composable
internal fun FfmpegActionContent(
    model: State,
    modifier: Modifier = Modifier,
) {
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
                value = model.command,
                onValueChange = { model.action(Action.UpdateCommand(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(32.dp),
            )
            SectionResourceCard(
                resourcePath = model.videoName,
                buttonText = R.string.feature_ffmpeg_action_video_resources,
                allowExtensions = "mp4",
            ) {
                model.action(UpdateVideoResource(it))
            }
            SectionResourceCard(
                resourcePath = model.audioName,
                buttonText = R.string.feature_ffmpeg_action_audio_resources,
                allowExtensions = "aac",
            ) {
                model.action(UpdateAudioResource(it))
            }
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("*/*")) { uri ->
                    if (uri != null) {
                        val newFile = uri.toString()
                        model.action(CreateNewFile(newFile))
                        Napier.d(tag = "createFile") { newFile }
                    }
                }
            AsButton(
                onClick = {
                    launcher.launch((System.currentTimeMillis() / 1000).toString() + ".mp4")
                },
            ) {
                Text("输出文件")
            }
            AsButton(onClick = { model.action(ExecuteCommand) }) {
                Text("执行命令")
            }
        }
    }
}

@Composable
internal fun SectionResourceCard(
    resourcePath: String,
    allowExtensions: String,
    buttonText: Int,
    result: (ResourceFile) -> Unit,
) {
    val launcher = rememberFilePickerLauncher(
        type = PickerType.File(listOf(allowExtensions)),
    ) {
        if (it != null) {
            result(ResourceFile(it.name, it.uri))
            Napier.d(tag = "filename") { it.name }
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            resourcePath,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AsButton(
            onClick = launcher::launch,
        ) {
            Text(stringResource(buttonText))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSectionResourceCard() {
    SectionResourceCard("", "", R.string.feature_ffmpeg_action_audio_resources, {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewFfmpegActionScreen() {
    FfmpegActionScreen(PreviewFfmpegActionComponent())
}
