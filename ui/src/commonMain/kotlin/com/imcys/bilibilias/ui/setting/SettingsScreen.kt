package com.imcys.bilibilias.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Hd
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsSwitch
import com.imcys.bilibilias.core.datastore.model.Codecs
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import com.imcys.bilibilias.logic.setting.SettingsViewModel
import com.imcys.bilibilias.ui.component.BackButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val state by settingsViewModel.preferences.collectAsState()
    SettingsContent(
        state = state,
        onBack = onBack,
        updateTryLook = settingsViewModel::setTryLook,
        updateDecoderCodec = settingsViewModel::setDecoderCodecPriorityList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    state: UserPreferences,
    onBack: () -> Unit,
    updateTryLook: (Boolean) -> Unit,
    updateDecoderCodec: (List<Codecs>) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("设置")
                },
                navigationIcon = {
                    BackButton(onBack = onBack)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            SortableItem(
                values = { state.codecPriorityList },
                onSort = {
                    updateDecoderCodec(it)
                },
                exposed = { list -> Text(text = list.joinToString()) },
                item = { item ->
                    Text(text = item.toString(), modifier = Modifier.padding(vertical = 16.dp))
                },
                key = { it },
                title = { Text("解码格式") },
                description = { Text("请根据设备支持情况与需求调整") },
                dialogDescription = { Text("长按排序，优先选择顺序较高的项目。") },
                icon = { Icon(Icons.Outlined.Hd, null) },
            )
            SettingsSwitch(
                state = state.enableTryLook,
                icon = {
                    if (state.enableTryLook)
                        Icon(Icons.Outlined.Visibility, null)
                    else
                        Icon(Icons.Outlined.VisibilityOff, null)
                },
                title = {
                    Text("免登录1080P")
                },
                subtitle = {
                    Text("免登录查看1080P视频")
                }
            ) {
                updateTryLook(it)
            }
            ShareLogFile()
        }
    }
}

@Composable
internal expect fun ShareLogFile()