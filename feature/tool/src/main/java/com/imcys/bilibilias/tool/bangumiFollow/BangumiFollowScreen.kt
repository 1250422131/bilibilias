package com.imcys.bilibilias.tool.bangumiFollow

import android.content.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*

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
                HorizontalDivider()
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
