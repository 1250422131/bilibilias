package com.imcys.bilibilias.feature.download

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.designsystem.component.AsTextButton
import com.imcys.bilibilias.core.designsystem.icon.AsIcons
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.liulishuo.okdownload.kotlin.DownloadProgress
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DownloadRoute(modifier: Modifier) {
    val viewModel: DownloadViewModel = hiltViewModel()
    val taskQueue by viewModel.taskFlow.collectAsState()
    DownloadScreen(taskQueue, onCancel = viewModel::onCancle)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DownloadScreen(
    uiState: ImmutableList<AsDownloadTask>,
    onCancel: (AsDownloadTask) -> Unit
) {
    var edit by remember { mutableStateOf(false) }
    var openConfirmationWindow by remember { mutableStateOf(false) }
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
            items(
                uiState,
                { it.fileType.toString() + it.viewInfo.bvid + it.viewInfo.cid }
            ) { item ->
                DownloadTaskItem(item) {
                    onCancel(item)
                    openConfirmationWindow = true
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
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                AsButton(onClick = onconfirm) {
                    Text("确定")
                }
            },
            modifier = modifier,
            dismissButton = {
                AsButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun DownloadTaskItem(task: AsDownloadTask, onCancel: () -> Unit) {
    var isShow by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = task.fileType.toString(),
                modifier = Modifier,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.viewInfo.title,
                    modifier = Modifier,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier, verticalArrangement = Arrangement.SpaceBetween) {
                    val currentState = remember(task.state) { task.state.cn }
                    Text(
                        text = currentState,
                        modifier = Modifier,
                        color = Color.LightGray
                    )
                    Text(
                        text = task.viewInfo.cid.toString(),
                        modifier = Modifier,
                        color = Color.LightGray
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { isShow = true }) {
                    Icon(
                        AsIcons.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            val progress = task.progress.collectAsState(initial = DownloadProgress())
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth(),
                progress = { progress.value.progress() },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
    ConfirmDeleteTaskDialog(
        isShow = isShow,
        onconfirm = onCancel,
        onDismiss = { isShow = false }
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
