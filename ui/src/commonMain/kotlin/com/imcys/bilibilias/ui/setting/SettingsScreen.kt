package com.imcys.bilibilias.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imcys.bilibilias.logic.setting.SettingsComponent

@Composable
fun SettingsScreen(component: SettingsComponent) {
    SettingsContent()
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
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) { Text("未实现") }
            }
        }
    }
}