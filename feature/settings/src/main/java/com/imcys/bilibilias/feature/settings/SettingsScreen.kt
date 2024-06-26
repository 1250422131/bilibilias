package com.imcys.bilibilias.feature.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.imcys.bilibilias.core.common.utils.getActivity
import com.imcys.bilibilias.feature.settings.component.SettingsComponent
import io.github.aakira.napier.Napier
// todo 打开 dialog 的状态放在 viewmodel
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
            val context = LocalContext.current
            SettingsGroup(
                title = { Text(text = "通用") }
            ) {
                FileStorageMenuLink(model.storagePath, onEvent)

                NamingRuleMenuLink(model.namingRule, onEvent)

                SettingsMenuLink(title = { Text(text = "还原文件存储路径") }) {
                    onEvent(UserEditEvent.onSelectedStoragePath(null))
                }
                SettingsMenuLink(title = { Text(text = "还原文件命名规则") }) {
                    onEvent(UserEditEvent.onEditNamingRule(null))
                }

                FFmpegCommandMenuLink(model.command, onEvent)

                SettingsMenuLink(title = { Text(text = "退出登录") }) {
                    onEvent(UserEditEvent.onLogout)
                    context.getActivity().finish()
                }
                ShareLogMenuLink(onEvent)
            }
            SettingsGroup(title = { Text(text = "主题") }) {
            }
            SettingsGroup(title = { Text(text = "语言") }) {
            }
//            SettingsGroup(
//                title = { Text(text = "隐私政策") },
//                modifier = Modifier
//            ) {
//                SettingsSwitch(
//                    state = model.shouldAppcenter,
//                    title = { Text(text = "允许使用 Microsoft AppCenter") },
//                ) {
//                    onEvent(UserEditEvent.onChangeWill(it))
//                }
//            }
        }
    }
}

@Composable
fun ShareLogMenuLink(onEvent: (UserEditEvent) -> Unit) {
    var open by remember { mutableStateOf(false) }
    SettingsMenuLink(title = { Text(text = "分享日志") }) {
        open = true
    }
    if (open) {
        Dialog(
            onDismissRequest = { open = false },
            properties = DialogProperties()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "log.txt",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onEvent(UserEditEvent.ShareLog.NewLog) }
                    )
                    Text(
                        text = "old_log.txt",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onEvent(UserEditEvent.ShareLog.OldLog) }
                    )
                }
            }
        }
    }
}

@Composable
fun FileStorageMenuLink(path: String, onEvent: (UserEditEvent) -> Unit) {
    val context = LocalContext.current
    val activityResultLauncher =
        rememberLauncherForActivityResult(AsOpenDocumentTree()) {
            Napier.d { "选择的路径 ${it?.toString()}" }
            if (it != null) {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }

            onEvent(UserEditEvent.onSelectedStoragePath(it?.toString()))
        }
    SettingsMenuLink(
        title = { Text(text = "文件储存路径") },
        subtitle = { Text(text = path) }
    ) {
        activityResultLauncher.launch(null)
    }
}

@Composable
private fun NamingRuleMenuLink(rule: String, onEvent: (UserEditEvent) -> Unit) {
    var showEditRuleDialog by remember { mutableStateOf(false) }
    var newRule by remember { mutableStateOf<String?>(null) }
    SettingsMenuLink(
        title = { Text(text = "文件命名规则") },
        subtitle = { Text(text = rule) }
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
        inputText = rule,
        onDismiss = {
            showEditRuleDialog = false
            newRule?.let { onEvent(UserEditEvent.onEditNamingRule(it)) }
        }
    ) {
        newRule = it
    }
}

@Composable
private fun FFmpegCommandMenuLink(command: String, onEvent: (UserEditEvent) -> Unit) {
    var newCommand by remember { mutableStateOf<String?>(null) }
    var showEditCommandDialog by remember { mutableStateOf(false) }
    SettingsMenuLink(
        title = { Text(text = "FFmpeg命令") },
        subtitle = { Text(text = command) }
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
        inputText = newCommand ?: command,
        onDismiss = {
            showEditCommandDialog = false
            newCommand?.let { onEvent(UserEditEvent.onEditCommand(it)) }
        }
    ) {
        newCommand = it
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDialog(
    show: Boolean,
    helpText: String,
    inputText: String,
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
                        value = inputText,
                        onValueChange = onEdit,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
