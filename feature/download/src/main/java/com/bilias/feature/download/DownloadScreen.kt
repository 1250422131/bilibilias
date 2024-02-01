package com.bilias.feature.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imcys.designsystem.component.AsTextButton
import com.imcys.ui.VideoCover

@Composable
internal fun DownloadRoute() {
    val viewModel: DownloadViewModel = hiltViewModel()
    val taskState by viewModel.taskState.collectAsStateWithLifecycle()
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
            items(taskState.taskList) { task ->
                var selected by remember { mutableStateOf(false) }
                ListItem(
                    leadingContent = {
                        VideoCover(url = task.cover)
                    },
                    headlineContent = {
                        Text(text = task.collectionName)
                    },
                    trailingContent = {
                        if (editable) {
                            Checkbox(
                                checked = selected,
                                onCheckedChange = {
                                    selected = it
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}
