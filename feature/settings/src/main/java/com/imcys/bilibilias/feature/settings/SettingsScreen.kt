package com.imcys.bilibilias.feature.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.imcys.bilibilias.core.common.download.DefaultConfig
import io.github.aakira.napier.Napier

object SettingScreen  {
    @Composable
    fun Content() {
//        val viewModel: SettingsViewModel = getViewModel()
//        val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
//        SettingContent(
//            settingsUiState = settingsUiState,
//            onChangeStoragePath = viewModel::updateFileStoragePath,
//            onChangeNameRule = viewModel::updateFileNameRule,
//            onChangeAutoMerge = viewModel::updateAutoMerge,
//            onChangeAutoImport = viewModel::updateAutoImportToBilibili,
//            onChangeWill = viewModel::updateShouldAppcenter,
//            onChangeCommand = viewModel::updateMergeCommand
//        )
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
    onChangeCommand: (command: String) -> Unit,
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
                            Napier.d { "选择的路径 ${it?.path}" }
                            onChangeStoragePath(it?.path ?: DefaultConfig.defaultStorePath)
                        }
                    SettingsMenuLink(
                        title = { Text(text = "文件储存路径") },
                        subtitle = { Text(text = settingsUiState.settings.fileStoragePath) }
                    ) {
                        activityResultLauncher.launch(null)
                    }
                    var showEditRuleDialog by remember { mutableStateOf(false) }
                    var rule by remember { mutableStateOf<String?>(null) }
                    SettingsMenuLink(
                        title = { Text(text = "文件夹命名规则") },
                        subtitle = { Text(text = settingsUiState.settings.folderNameRule) }
                    ) {
                        showEditRuleDialog = true
                    }
                    SimpleDialog(
                        show = showEditRuleDialog,
                        helpText = "AV号: {AV}\n" +
                                "BV号: {BV}\n" +
                                "CID号: {CID}\n" +
                                "视频标题: {TITLE}\n" +
                                "分P标题: {P_TITLE}\n",
                        textFieldHint = rule ?: settingsUiState.settings.folderNameRule,
                        onDismiss = {
                            showEditRuleDialog = false
                            rule?.let { onChangeNameRule(it) }
                        }
                    ) {
                        rule = it
                    }
                    SettingsMenuLink(title = { Text(text = "还原文件存储路径") }) {
                        onChangeStoragePath(DefaultConfig.defaultStorePath)
                    }
                    SettingsMenuLink(title = { Text(text = "还原文件夹命名规则") }) {
                        onChangeNameRule(DefaultConfig.defaultNameRule)
                    }
                    SettingsSwitch(
                        state = settingsUiState.settings.autoMerge,
                        title = { Text(text = "下载完成后自动合并") }
                    ) {
                        onChangeAutoMerge(it)
                    }
                    SettingsSwitch(
                        state = settingsUiState.settings.autoImport,
                        title = { Text(text = "下载完成自动导入B站") }
                    ) {
                        onChangeAutoImport(it)
                    }
                    var command by remember {
                        mutableStateOf<String?>(null)
                    }
                    var showEditCommandDialog by remember {
                        mutableStateOf(false)
                    }
                    SettingsMenuLink(
                        title = { Text(text = "合并时的FFmpeg命令") },
                        subtitle = { Text(text = settingsUiState.settings.command) }
                    ) {
                        showEditCommandDialog = true
                    }
                    SimpleDialog(
                        show = showEditCommandDialog,
                        helpText = "如果你不清楚FFmpeg命令，请不要修改\n" +
                                "BILIBILIAS为大家提供了简单的命令参数\n" +
                                "{VIDEO_PATH} 视频下载路径\n" +
                                "{AUDIO_PATH} 音频储存路径\n" +
                                "{VIDEO_MERGE_PATH} 合并后储存路径",
                        textFieldHint = command ?: settingsUiState.settings.command,
                        onDismiss = {
                            showEditCommandDialog = false
                            command?.let { onChangeCommand(it) }
                        }
                    ) {
                        command = it
                    }

                    SettingsGroup(title = { Text(text = "主题") }) {
                    }
                    SettingsGroup(title = { Text(text = "语言") }) {
                    }
                }

                SettingsGroup(title = { Text(text = "隐私政策") }) {
                    SettingsSwitch(
                        state = settingsUiState.settings.shouldAppcenter,
                        title = { Text(text = "允许使用 Microsoft AppCenter") },
                    ) {
                        onChangeWill(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDialog(
    show: Boolean,
    helpText: String,
    textFieldHint: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onEdit: (String) -> Unit
) {
    if (show) {
        BasicAlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card {
                    Text(
                        text = helpText,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp),
                    )
                    TextField(
                        value = textFieldHint,
                        onValueChange = onEdit
                    )
                }
            }
        }
    }
}
