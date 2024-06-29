package com.imcys.bilibilias.feature.download

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.imcys.bilibilias.core.common.utils.DataSize.Companion.bytes
import com.imcys.bilibilias.core.common.utils.DataUnit
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.database.model.Task
import com.imcys.bilibilias.core.designsystem.component.AsTextButton
import com.imcys.bilibilias.core.designsystem.icon.AsIcons
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.download.component.DownloadComponent
import com.imcys.bilibilias.feature.download.component.Event
import com.imcys.bilibilias.feature.download.component.Model
import com.imcys.bilibilias.feature.download.sheet.BottomSheetContent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DownloadContent(
    component: DownloadComponent,
    navigationToPlayer: (viewInfo: ViewInfo) -> Unit
) {
    DownloadScreen(component = component, navigationToPlayer = navigationToPlayer)
}

@Composable
internal fun DownloadScreen(
    component: DownloadComponent,
    navigationToPlayer: (viewInfo: ViewInfo) -> Unit
) {
    val model by component.models.collectAsStateWithLifecycle()
    val tasks by component.tasks.collectAsState()
    val dialogSlot by component.dialogSlot.subscribeAsState()
    dialogSlot.child?.instance?.let {
        BottomSheetContent(it, navigationToPlayer)
    }
    DownloadScreen(
        model = model,
        onEvent = component::take,
        onSettingsClicked = component::onSettingsClicked,
        tasks = tasks
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DownloadScreen(
    model: Model,
    onEvent: (Event) -> Unit,
    onSettingsClicked: (ViewInfo, FileType) -> Unit,
    tasks: ImmutableList<ImmutableList<DownloadTaskEntity>>,
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
            tasks.forEach {
                items(it) { item ->
                    DownloadTaskItem(
                        task = item,
                        onSettingsClicked = onSettingsClicked
                    )
                }
                item {
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadTaskItem(
    task: DownloadTaskEntity,
    onSettingsClicked: (ViewInfo, FileType) -> Unit
) {
    Column {
        ListItem(
            modifier = Modifier.combinedClickable {
                onSettingsClicked(
                    ViewInfo(task.aid, task.bvid, task.cid, task.title),
                    task.fileType
                )
            },
            leadingContent = {
                Card(
                    modifier = Modifier.size(80.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(251, 114, 153)
                    )
                ) {
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
                Text(text = "${task.state.cn}·")
//            ${task.uri.toFile().length().bytes.toLong(DataUnit.MEGABYTES)}MB
            },
            trailingContent = {
                Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
            },
        )
        if (task.state == State.RUNNING) {
            LinearProgressIndicator(progress = { task.progress })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DownloadTaskItem(
    task: Task,
    onSettingsClicked: (ViewInfo, FileType) -> Unit
) {
    ListItem(
        modifier = Modifier.combinedClickable {
            onSettingsClicked(ViewInfo(task.aid, task.bvid, task.cid, task.title), task.fileType)
        },
        leadingContent = {
            Card(
                modifier = Modifier.size(80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(251, 114, 153)
                )
            ) {
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
            Text(text = task.state.cn)
//            Text("${task.state}·${task.uri.toFile().length().bytes.toLong(DataUnit.MEGABYTES)}MB")
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
