package com.imcys.bilibilias.feature.download

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.designsystem.component.AsTextButton
import com.imcys.bilibilias.core.designsystem.icon.AsIcons
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.core.utils.selected
import com.imcys.bilibilias.feature.download.component.DownloadComponent
import com.imcys.bilibilias.feature.download.component.Event
import com.imcys.bilibilias.feature.download.component.Model
import com.imcys.bilibilias.feature.download.component.PreviewDownloadComponent

@Composable
fun DownloadContent(component: DownloadComponent) {
    DownloadScreen(component = component)
}

@Composable
internal fun DownloadScreen(
    component: DownloadComponent,
) {
    val model by component.models.collectAsStateWithLifecycle()

    DownloadScreen(
        model = model,
        onEvent = component::take,
        onSettingsClicked = { info, type -> },
        component.selectedDeletes,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DownloadScreen(
    model: Model,
    onEvent: (Event) -> Unit,
    onSettingsClicked: (ViewInfo, FileType) -> Unit,
    selectedDeletes: List<Int>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    if (model.canDelete) {
                        IconButton(onClick = { onEvent(Event.ConfirmDeletion) }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                    EditButton(
                        model.canDelete,
                        editable = { onEvent(Event.OpenDeleteOption) },
                        cancelSelection = { onEvent(Event.CloseDeleteOption) },
                    )
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(4.dp),
        ) {
            model.entities.forEach {
                items(it, key = { it.id }) { item ->
                    DownloadTaskPanel(
                        task = item,
                        onSettingsClicked = onSettingsClicked,
                        isOpenSelect = model.canDelete,
                        onSelect = { onEvent(Event.UserSelecte(it)) },
                        isSelected = selectedDeletes.selected(item.id),
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
fun DownloadTaskPanel(
    task: DownloadTaskEntity,
    onSettingsClicked: (ViewInfo, FileType) -> Unit,
    isOpenSelect: Boolean,
    onSelect: (Int) -> Unit,
    isSelected: Boolean,
) {
    Column {
        ListItem(
            modifier = Modifier.clickable {
                onSettingsClicked(
                    ViewInfo(task.aid, task.bvid, task.cid, task.title),
                    task.fileType,
                )
            },
            leadingContent = {
                Card(
                    modifier = Modifier.size(80.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
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
            },
            trailingContent = {
                if (isOpenSelect) {
                    Checkbox(checked = isSelected, onCheckedChange = { onSelect(task.id) })
                }
            },
        )
        if (task.state == State.RUNNING) {
            LinearProgressIndicator(progress = { task.progress })
        }
    }
}

@Composable
fun EditButton(
    isEdit: Boolean,
    editable: () -> Unit,
    cancelSelection: () -> Unit,
) {
    if (!isEdit) {
        IconButton(onClick = editable) {
            Icon(imageVector = AsIcons.EditNote, contentDescription = "编辑")
        }
    } else {
        AsTextButton(onClick = cancelSelection, text = { Text(text = "取消") })
    }
}

@Preview(showSystemUi = true, showBackground = false)
@Composable
private fun PreviewDownloadScreen() {
    DownloadContent(component = PreviewDownloadComponent())
}
