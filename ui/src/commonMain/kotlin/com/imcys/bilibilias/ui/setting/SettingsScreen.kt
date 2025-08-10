package com.imcys.bilibilias.ui.setting

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alorma.compose.settings.ui.SettingsGroup
import com.imcys.bilibilias.logic.setting.SettingsComponent
import com.imcys.bilibilias.ui.BackButton

@Composable
fun SettingsScreen(component: SettingsComponent, onBack: () -> Unit) {
    SettingsContent(
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(onBack: () -> Unit) {
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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                SettingsGroup(title = { Text("其他") }) {
                    ShareLogFile()
                }
            }
        }
    }
}

@Composable
internal expect fun ShareLogFile()