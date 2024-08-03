package com.bilias.feature.download

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.hilt.navigation.compose.*
import com.imcys.designsystem.component.*

@Composable
internal fun DownloadRoute() {
    val viewModel: DownloadViewModel = hiltViewModel()
    val taskState by viewModel.taskState.collectAsState()
    DownloadScreen(taskState)
}

@Composable
internal fun DownloadScreen(taskState: TaskState) {
    var editable by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    AsTextButton(
                        onClick = { editable = !editable },
                        text = { Text("编辑") }
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            taskState.progress.forEach { (k, v) ->
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = k.toString())
                        Text(text = v.toString())
                    }
                }
            }
//            items(taskState.taskList) { task ->
//                var selected by remember { mutableStateOf(false) }
//                ListItem(
//                    leadingContent = {
//                        VideoCover(url = task.cover)
//                    },
//                    headlineContent = {
//                        Text(text = task.collectionName)
//                    },
//                    trailingContent = {
//                        if (editable) {
//                            Checkbox(
//                                checked = selected,
//                                onCheckedChange = {
//                                    selected = it
//                                }
//                            )
//                        }
//                    }
//                )
//            }
        }
    }
}
