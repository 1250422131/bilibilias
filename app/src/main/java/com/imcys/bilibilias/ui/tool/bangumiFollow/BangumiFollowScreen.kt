package com.imcys.bilibilias.ui.tool.bangumiFollow

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BangumiFollowScreen(
    selectableHeaders: List<Pair<String, BangumiFollowHeaders>>,
    selectedHeaders: List<Pair<String, BangumiFollowHeaders>>,
    removeSelected: (Pair<String, BangumiFollowHeaders>) -> Unit,
    addToSelected: (Pair<String, BangumiFollowHeaders>) -> Unit,
    submit: (Context) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var index by remember { mutableIntStateOf(0) }
    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                selectedHeaders.forEach { pair ->
                    OutlinedButton(onClick = { removeSelected(pair) }) {
                        Text(text = pair.first)
                    }
                }
                Divider()
                FlowRow(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    selectableHeaders.forEach { pair ->
                        OutlinedButton(onClick = { addToSelected(pair) }) {
                            Text(text = pair.first)
                        }
                    }
                }
            }
            val context = LocalContext.current
            Button(onClick = { submit(context) }) {
                Text(text = "完成")
            }
        }
    }
}
