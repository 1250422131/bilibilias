package com.imcys.bilibilias.ui.setting

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imcys.bilibilias.logic.setting.SettingsComponent
import me.zhanghai.compose.preference.ListPreferenceType
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.checkboxPreference
import me.zhanghai.compose.preference.footerPreference
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.multiSelectListPreference
import me.zhanghai.compose.preference.preference
import me.zhanghai.compose.preference.radioButtonPreference
import me.zhanghai.compose.preference.sliderPreference
import me.zhanghai.compose.preference.switchPreference
import me.zhanghai.compose.preference.textFieldPreference
import me.zhanghai.compose.preference.twoTargetIconButtonPreference
import me.zhanghai.compose.preference.twoTargetSwitchPreference
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(component: SettingsComponent) {
    ProvidePreferenceLocals {
        SettingsContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("设置")
                },
                navigationIcon = {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "返回")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            preference(
                key = "preference",
                title = { Text(text = "Preference") },
                summary = { Text(text = "Summary") },
            ) {}
            checkboxPreference(
                key = "checkbox_preference",
                defaultValue = false,
                title = { Text(text = "Checkbox preference") },
                summary = { Text(text = if (it) "On" else "Off") },
            )
            switchPreference(
                key = "switch_preference",
                defaultValue = false,
                title = { Text(text = "Switch preference") },
                summary = { Text(text = if (it) "On" else "Off") },
            )
            twoTargetSwitchPreference(
                key = "two_target_switch_preference",
                defaultValue = false,
                title = { Text(text = "Two target switch preference") },
                summary = { Text(text = if (it) "On" else "Off") },
            ) {}
            twoTargetIconButtonPreference(
                key = "two_target_icon_button_preference",
                title = { Text(text = "Two target icon button preference") },
                summary = { Text(text = "Summary") },
                onClick = {},
                iconButtonIcon = {
                    Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings")
                },
            ) {}
            sliderPreference(
                key = "slider_preference",
                defaultValue = 0f,
                title = { Text(text = "Slider preference") },
                valueRange = 0f..5f,
                valueSteps = 9,
                summary = { Text(text = "Summary") },
                valueText = { Text(text = ((it / 0.5f).roundToInt() * 0.5f).toString()) },
            )
            listPreference(
                key = "list_alert_dialog_preference",
                defaultValue = "Alpha",
                values = listOf("Alpha", "Beta", "Canary"),
                title = { Text(text = "List preference (alert dialog)") },
                summary = { Text(text = it) },
            )
            listPreference(
                key = "list_dropdown_menu_preference",
                defaultValue = "Alpha",
                values = listOf("Alpha", "Beta", "Canary"),
                title = { Text(text = "List preference (dropdown menu)") },
                summary = { Text(text = it) },
                type = ListPreferenceType.DROPDOWN_MENU,
            )
            multiSelectListPreference(
                key = "multi_select_list_preference",
                defaultValue = setOf("Alpha", "Beta"),
                values = listOf("Alpha", "Beta", "Canary"),
                title = { Text(text = "Multi-select list preference") },
                summary = { Text(text = it.sorted().joinToString(", ")) },
            )
            textFieldPreference(
                key = "text_field_preference",
                defaultValue = "Value",
                title = { Text(text = "Text field preference") },
                textToValue = { it },
                summary = { Text(text = it) },
            )
            radioButtonPreference(
                key = "radio_button_preference",
                selected = true,
                title = { Text(text = "Radio button preference") },
                summary = { Text(text = "Summary") },
            ) {}
            footerPreference(
                key = "footer_preference",
                summary = { Text(text = "Footer preference summary") },
            )
        }
    }
}