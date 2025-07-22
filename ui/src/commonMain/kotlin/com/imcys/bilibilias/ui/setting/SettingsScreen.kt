package com.imcys.bilibilias.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imcys.bilibilias.logic.setting.SettingsComponent

@Composable
fun SettingsScreen(component: SettingsComponent) {
    SettingsContent()
}

@Composable
fun SettingsContent() {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Settings")
        }
    }
}