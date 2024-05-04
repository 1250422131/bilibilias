package com.imcys.bilibilias.feature.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.imcys.bilibilias.core.common.download.DefaultConfig

object SettingScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: SettingsViewModel = getViewModel()
        val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
        SettingContent(
            settingsUiState = settingsUiState,
            onChangeStoragePath = viewModel::updateFileStoragePath,
            onChangeNameRule = viewModel::updateFileNameRule,
            onChangeAutoMerge = viewModel::updateAutoMerge,
            onChangeAutoImport = viewModel::updateAutoImportToBilibili,
            onChangeWill = viewModel::updateShouldAppcenter
        )
    }
}

@Composable
fun SettingContent(
    settingsUiState: SettingsUiState,
    onChangeStoragePath: (path: String) -> Unit,
    onChangeNameRule: (rule: String) -> Unit,
    onChangeAutoMerge: (enable: Boolean) -> Unit,
    onChangeAutoImport: (enable: Boolean) -> Unit,
    onChangeWill: (will: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        when (settingsUiState) {
            SettingsUiState.Loading -> Unit
            is SettingsUiState.Success -> Column(
                modifier = modifier
                    .padding(innerPadding.calculateTopPadding())
                    .scrollable(rememberScrollState(), Orientation.Vertical)
            ) {
                SettingsGroup(title = { Text(text = "通用") }) {
                    val activityResultLauncher =
                        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
                            onChangeStoragePath(it?.path ?: DefaultConfig.defaultStorePath)
                        }
                    SettingsMenuLink(
                        title = { Text(text = "文件储存路径") },
                        subtitle = { Text(text = settingsUiState.settings.fileStoragePath) }
                    ) {
                        activityResultLauncher.launch(null)
                    }

                    SettingsMenuLink(
                        title = { Text(text = "文件命名规则") },
                        subtitle = { Text(text = "路径") }
                    ) {
                    }
                    SettingsMenuLink(title = { Text(text = "还原文件存储路径") }) {
                        onChangeStoragePath(DefaultConfig.defaultStorePath)
                    }
                    SettingsMenuLink(title = { Text(text = "还原文件命名规则") }) {
                        onChangeNameRule(DefaultConfig.defaultNameRule)
                    }
                    SettingsMenuLink(title = { Text(text = "下载完成后自动合并") }) {
                        onChangeAutoMerge(!settingsUiState.settings.autoMerge)
                    }
                    SettingsMenuLink(title = { Text(text = "下载完成自动导入B站") }) {
                        onChangeAutoImport(!settingsUiState.settings.autoImport)
                    }
                    SettingsMenuLink(
                        title = { Text(text = "合并时的FFmpeg命令") },
                        subtitle = { Text(text = "ffmpeg -i {VIDEO_PATH} -i {AUDIO_PATH} -c copy {VIDEO_MERGE_PATH}") }
                    ) {
                    }

                    SettingsGroup(title = { Text(text = "主题") }) {
                    }
                    SettingsGroup(title = { Text(text = "语言") }) {
                    }
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
}
