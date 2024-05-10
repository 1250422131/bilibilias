package com.imcys.bilibilias.feature.download

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.imcys.bilibilias.core.common.utils.DataSize.Companion.bytes
import com.imcys.bilibilias.core.common.utils.DataUnit
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.designsystem.component.AsCard
import com.imcys.bilibilias.core.designsystem.component.AsTextButton
import com.imcys.bilibilias.core.designsystem.icon.AsIcons
import com.imcys.bilibilias.core.model.video.Cid
import io.github.aakira.napier.Napier

object DownloadRoute1 : Screen {
    @Composable
    override fun Content() {
        val viewModel: DownloadViewModel = getViewModel()
        Napier.d { System.identityHashCode(viewModel).toString() + "haha" }
        val taskQueue by viewModel.taskFlow.collectAsState()
        BottomSheetNavigator {
            DownloadScreen(taskQueue)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DownloadScreen(
    uiState: Map<Cid, List<DownloadTask>>,
) {
    var edit by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                actions = {
                    EditButton(edit, editable = { edit = true }, cancleSelection = { edit = false })
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(4.dp)
        ) {
            uiState.forEach { (_, v) ->
                items(v) { item ->
                    DownloadTaskItem(task = item)
                }
                item {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteTaskDialog(
    isShow: Boolean,
    onconfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isShow) {
        AlertDialog(onDismissRequest = onDismiss, confirmButton = {
            AsButton(onClick = onconfirm) {
                Text("确定")
            }
        }, modifier = modifier, dismissButton = {
            AsButton(onClick = onDismiss) {
                Text("取消")
            }
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadTaskItem(task: DownloadTask) {
    val sheetNavigator = LocalBottomSheetNavigator.current
    ListItem(
        modifier = Modifier.combinedClickable {
            val info = task.viewInfo
            sheetNavigator.show(
                ChoicesScreen(info.aid, info.bvid, info.cid, task.fileType)
            )
        },
        leadingContent = {
            AsCard(modifier = Modifier.size(80.dp)) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = task.fileType.toString(),
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        },
        headlineContent = {
            Text(
                text = task.subTitle,
                modifier = Modifier,
                maxLines = 2,
            )
        },
        supportingContent = {
            Text("${task.state}·${task.uri.toFile().length().bytes.toLong(DataUnit.MEGABYTES)}MB")
        },
        trailingContent = {
            Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
        },
    )
}

@Composable
fun EditButton(
    isEdit: Boolean,
    editable: () -> Unit,
    cancleSelection: () -> Unit
) {
    if (!isEdit) {
        IconButton(onClick = editable) {
            Icon(imageVector = AsIcons.EditNote, contentDescription = "编辑")
        }
    } else {
        AsTextButton(onClick = cancleSelection, text = { Text(text = "取消") })
    }
}
