package com.imcys.bilibilias.feature.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun UserRoute(modifier: Modifier) {
    Scaffold { innerPading ->
        Column(
            modifier = Modifier.padding(innerPading),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "当前页面未实现")
        }
    }
}

@Composable
internal fun UserScreen(todo: () -> Unit) {
}
