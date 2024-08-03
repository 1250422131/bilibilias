package com.imcys.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.alorma.compose.settings.storage.base.getValue
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.storage.base.rememberStringSettingState
import com.alorma.compose.settings.storage.base.setValue
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsList
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.imcys.common.utils.getActivity
import com.imcys.datastore.mmkv.SettingsRepository
import me.rosuh.filepicker.config.FilePickerManager
import timber.log.Timber

@Composable
fun SettingsScreen() {
    LazyColumn {
        item {
            SettingsGroup(title = {
                Text(stringResource(R.string.app_root_preferences_title_download_conf))
            }) {
                /**
                 * 保存文件路径
                 */
                SaveVideoPath()

                /**
                 * 用存储卡作为下载路径
                 */
                // 还原下载路径 app:title="@string/rename_user_download_save_path_title"
                SettingsSwitch(
                    title = { Text(stringResource(R.string.user_download_save_sd_path_switch_title)) },
                    subtitle = { Text(stringResource(R.string.user_download_save_path_title)) },
                    enabled = false
                ) {}
                /**
                 * 命名规则
                 */
                EditVideoNamingConvention()
            }
        }
        item { AutomaticMerge() }
        /**
         * 下载完成后自动导入B站
         */
        item { AutomaticImportBilibili() }
        item { Theme() }

        item { Language() }

        /**
         * 谷歌广告
         */
        item { GoogleAD() }
        /**
         * 数据统计
         */
        item { Statistics() }
    }
}

@Composable
private fun Language() {
    SettingsGroup(title = { Text(stringResource(R.string.app_root_preferences_title_language)) }) {
        SettingsList(
            items = (1..5).map { it.toString() },
            title = { Text(stringResource(R.string.app_root_preferences_long_title_language)) },
            subtitle = { Text(stringResource(R.string.app_root_preferences_long_title_language_tip)) },
            onItemSelected = { index, item ->
            }
        )
    }
}

@Composable
private fun Theme() {
    SettingsGroup(title = { Text(stringResource(R.string.app_root_preferences_title_theme)) }) {
        SettingsList(
            items = (1..5).map { it.toString() },
            title = { Text(stringResource(R.string.app_root_preferences_long_title_theme)) },
            subtitle = { Text(stringResource(R.string.app_root_preferences_long_title_theme_tip)) },
            onItemSelected = { index, item ->
            }
        )
    }
}

@Composable
fun AutomaticMerge() {
    val autoImport by rememberBooleanSettingState(SettingsRepository.autoImportToBilibili)
    SettingsSwitch(
        title = { Text(stringResource(R.string.user_dl_finish_automatic_merge_switch_title)) },
        subtitle = {
            if (autoImport) {
                Text(stringResource(R.string.user_dl_finish_automatic_merge_switch_on))
            } else {
                Text(stringResource(R.string.user_dl_finish_automatic_merge_switch_off))
            }
        },
        icon = {
            Image(painter = painterResource(R.drawable.ic_setting_bilibili), contentDescription = "bilibili_icon")
        }
    ) {
        SettingsRepository.autoImportToBilibili = it
    }
}

@Composable
private fun GoogleAD() {
    val googleAD = rememberBooleanSettingState(SettingsRepository.googleAD)
    SettingsSwitch(state = googleAD, title = {
        Text("展示谷歌广告")
    }, subtitle = {
        if (googleAD.value) {
            Text("我们很感谢你可以打开它，这个谷歌广告不会阻碍你使用任何功能。")
        } else {
            Text("很抱歉广告打扰了你，我们利用广告展示和点击获取一些收入。")
        }
    }) {
        SettingsRepository.googleAD = it
    }
}

@Composable
private fun Statistics() {
    SettingsGroup(title = {
        Text(stringResource(R.string.app_root_preferences_title_privacy_policy))
    }) {
        val microsoft = rememberBooleanSettingState(SettingsRepository.microsoftStatistics)
        SettingsSwitch(
            state = microsoft,
            icon = {
                Image(
                    painter = painterResource(R.drawable.ic_setting_vscodeignore),
                    contentDescription = "微软统计"
                )
            },
            title = { Text(stringResource(R.string.microsoft_app_center_type_title)) },
            subtitle = { Text(stringResource(R.string.microsoft_app_center_type_summary)) }
        ) {
            SettingsRepository.microsoftStatistics = it
        }
        val baiDu = rememberBooleanSettingState(SettingsRepository.baiduStatistics)
        SettingsSwitch(
            state = baiDu,
            icon = {
                Image(painter = painterResource(R.drawable.baidu_logo), contentDescription = "百度统计")
            },
            title = { Text(stringResource(R.string.baidu_statistics_type_title)) },
            subtitle = { Text(stringResource(R.string.baidu_statistics_type_summary)) }
        ) {
            SettingsRepository.baiduStatistics = it
        }
    }
}

@Composable
private fun AutomaticImportBilibili() {
    val autoImport by rememberBooleanSettingState(SettingsRepository.autoImportToBilibili)
    SettingsSwitch(
        title = { Text(stringResource(R.string.user_dl_finish_automatic_import_switch_title)) },
        subtitle = {
            if (autoImport) {
                Text(stringResource(R.string.user_dl_finish_automatic_import_switch_on))
            } else {
                Text(stringResource(R.string.user_dl_finish_automatic_import_switch_off))
            }
        },
        icon = {
            Image(painter = painterResource(R.drawable.ic_setting_bilibili), contentDescription = "bilibili_icon")
        }
    ) {
        SettingsRepository.autoImportToBilibili = it
    }
}

@Composable
private fun EditVideoNamingConvention() {
    // 还原命名规则 app:title="@string/rename_user_download_file_name_editText_title"
    // 编辑视频命名规则，还原命名规则
    rememberBooleanSettingState()
    var show by rememberBooleanSettingState(false)
    var rules by rememberStringSettingState(SettingsRepository.videoNameRule)
    SettingsMenuLink(
        title = { Text(stringResource(R.string.user_download_file_name)) },
        subtitle = { Text(rules ?: SettingsRepository.DefaultVideoNameRule) },
        onClick = { show = true },
    )
    if (show) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(decorFitsSystemWindows = false)
        ) {
            Surface(
                Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxHeight()
            ) {
                Card(Modifier) {
                    Column(
                        Modifier
                            .padding(8.dp)
                            .scrollable(rememberScrollState(), Orientation.Vertical)
                    ) {
                        Text(stringResource(R.string.dl_file_rename_rules))
                        OutlinedTextField(
                            value = rules ?: SettingsRepository.DefaultVideoNameRule,
                            onValueChange = { rules = it }
                        )
                        Row {
                            TextButton(onClick = { show = false }) {
                                Text("取消")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = { show = false;SettingsRepository.resetVideoNamingRule() }
                            ) {
                                Text(stringResource(R.string.rename_user_download_file_name_editText_title))
                            }
                            TextButton(
                                onClick = {
                                    show = false
                                    rules?.let {
                                        SettingsRepository.saveVideoNamingRule(it)
                                    }
                                }
                            ) {
                                Text(stringResource(R.string.app_dialog_tone_quality_finish))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 保存文件路径
 */
@Composable
private fun SaveVideoPath() {
    val context = LocalContext.current
    var path by rememberStringSettingState(SettingsRepository.saveFilePath)
    SettingsMenuLink(
        title = { Text(stringResource(R.string.user_download_save_path_title)) },
        subtitle = { Text(path ?: TODO()) },
        onClick = { requestFileSavePath(context.getActivity()) },
    )
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        FilePickerManager.obtainData().filterNot { it.isBlank() }.forEach {
            Timber.tag("saveVideoUri").d(it)
            SettingsRepository.saveFilePath = it
            path = it
        }
    }
}
