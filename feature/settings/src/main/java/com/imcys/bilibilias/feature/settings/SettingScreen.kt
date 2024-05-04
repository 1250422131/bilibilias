package com.imcys.bilibilias.feature.settings

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch

object SettingScreen : Screen {
    @Composable
    override fun Content() {
    }
}

@Composable
fun SettingContent(modifier: Modifier = Modifier) {
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding.calculateTopPadding())
                .scrollable(rememberScrollState(), Orientation.Vertical)
        ) {
            SettingsGroup(title = { Text(text = "通用") }) {
                SettingsMenuLink(
                    title = { Text(text = "文件储存路径") },
                    subtitle = { Text(text = "路径") }
                ) {
                }
                SettingsMenuLink(
                    title = { Text(text = "文件命名规则") },
                    subtitle = { Text(text = "路径") }
                ) {
                }
                SettingsMenuLink(title = { Text(text = "还原文件存储路径") }) {
                }
                SettingsMenuLink(title = { Text(text = "还原文件命名规则") }) {
                }
                SettingsMenuLink(title = { Text(text = "下载完成后自动合并") }) {
                }
                SettingsMenuLink(title = { Text(text = "下载完成自动导入B站") }) {
                }
                SettingsMenuLink(
                    title = { Text(text = "合并时的FFmpeg命令") },
                    subtitle = { Text(text = "ffmpeg -i {VIDEO_PATH} -i {AUDIO_PATH} -c copy {VIDEO_MERGE_PATH}") }
                ) {
                }
            }

            SettingsGroup(title = { Text(text = "主题") }) {
            }
            SettingsGroup(title = { Text(text = "语言") }) {
            }
            SettingsGroup(title = { Text(text = "隐私政策") }) {
                SettingsSwitch(
                    state = false,
                    title = { Text(text = "允许使用 Microsoft AppCenter") },
                ) {
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingContentPreview() {
    SettingContent()
}
