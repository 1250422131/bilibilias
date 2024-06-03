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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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

@Composable
fun SettingContent(component: SettingsComponent) {
    val model by component.models.collectAsStateWithLifecycle()
    SettingContent(model, component::take)
}

@Composable
fun SettingContent(model: UserEditableSettings, onEvent: (UserEditEvent) -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .scrollable(rememberScrollState(), Orientation.Vertical)
        ) {
            SettingsGroup(
                title = { Text(text = "通用") }
            ) {
                val activityResultLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
                        Napier.d { "选择的路径 ${it?.path}" }
                        onEvent(UserEditEvent.onChangeStoragePath(it?.path))
                    }
                SettingsMenuLink(
                    title = { Text(text = "文件储存路径") },
                    subtitle = { Text(text = model.fileStoragePath) }
                ) {
                    activityResultLauncher.launch(null)
                }
                var showEditRuleDialog by remember { mutableStateOf(false) }
                var rule by remember { mutableStateOf<String?>(null) }
                SettingsMenuLink(
                    title = { Text(text = "文件夹命名规则") },
                    subtitle = { Text(text = model.folderNameRule) }
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
                    textFieldHint = rule ?: model.folderNameRule,
                    onDismiss = {
                        showEditRuleDialog = false
                        rule?.let { onEvent(UserEditEvent.onChangeFileNamingRule(it)) }
                    }
                ) {
                    rule = it
                }
                SettingsMenuLink(title = { Text(text = "还原文件存储路径") }) {
                    onEvent(UserEditEvent.onChangeStoragePath(null))
                }
                SettingsMenuLink(title = { Text(text = "还原文件夹命名规则") }) {
                    onEvent(UserEditEvent.onChangeFileNamingRule(null))
                }
                SettingsSwitch(
                    state = model.autoMerge,
                    title = { Text(text = "下载完成后自动合并") }
                ) {
                    onEvent(UserEditEvent.onChangeAutoMerge(it))
                }
                SettingsSwitch(
                    state = model.autoImport,
                    title = { Text(text = "下载完成自动导入B站") }
                ) {
                    onEvent(UserEditEvent.onChangeAutoImport(it))
                }
                var command by remember {
                    mutableStateOf<String?>(null)
                }
                var showEditCommandDialog by remember {
                    mutableStateOf(false)
                }
                SettingsMenuLink(
                    title = { Text(text = "合并时的FFmpeg命令") },
                    subtitle = { Text(text = model.command) }
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
                    textFieldHint = command ?: model.command,
                    onDismiss = {
                        showEditCommandDialog = false
//                        command?.let { onChangeCommand(it) }
                    }
                ) {
                    command = it
                }

                SettingsGroup(title = { Text(text = "主题") }) {
                }
                SettingsGroup(title = { Text(text = "语言") }) {
                }
            }

            SettingsGroup(
                title = { Text(text = "隐私政策") },
                modifier = Modifier
            ) {
                SettingsSwitch(
                    state = model.shouldAppcenter,
                    title = { Text(text = "允许使用 Microsoft AppCenter") },
                ) {
                    onEvent(UserEditEvent.onChangeWill(it))
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
                        modifier = Modifier.padding(8.dp),
                    )
                    OutlinedTextField(
                        value = textFieldHint,
                        onValueChange = onEdit,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
