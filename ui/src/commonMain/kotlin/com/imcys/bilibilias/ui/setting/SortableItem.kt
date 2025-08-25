package com.imcys.bilibilias.ui.setting

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Reorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.ui.SettingsMenuLink
import org.jetbrains.compose.ui.tooling.preview.Preview
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun <T> SortableItem(
    values: () -> List<T>,
    onSort: (List<T>) -> Unit,
    exposed: @Composable (List<T>) -> Unit,
    item: @Composable (T) -> Unit,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    description: @Composable (() -> Unit)? = null,
    dialogDescription: @Composable (() -> Unit)? = description,
    dialogItemDescription: @Composable ((T) -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
    title: @Composable () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val valuesState by remember(values) { derivedStateOf { values() } }
    SettingsMenuLink(
        title = { exposed(valuesState) },
        icon = icon,
        modifier = modifier,
    ) {
        showDialog = true
    }
    if (showDialog) {
        var sortingData by remember(valuesState) {
            mutableStateOf(valuesState)
        }

        val lazyListState = rememberLazyListState()
        val state = rememberReorderableLazyListState(lazyListState) { from, to ->
            sortingData = sortingData.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { title() },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                        dialogDescription?.let {
                            it()
                        }
                    }
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                    ) {
                        itemsIndexed(sortingData, key = { _, it -> key(it) }) { index, item ->
                            ReorderableItem(state, key = key(item)) { isDragging ->
                                val elevation = animateDpAsState(if (isDragging) 32.dp else 0.dp)
                                Row(
                                    modifier = Modifier
                                        .longPressDraggableHandle {

                                        }
                                        .shadow(elevation.value)
                                        .background(Color.Transparent) // match card background
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Row(Modifier.weight(1f)) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            item(item)
                                            ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                                                dialogItemDescription?.invoke(item)
                                            }
                                        }
                                    }

                                    Icon(
                                        Icons.Rounded.Reorder,
                                        "长按排序",
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onConfirm?.invoke()
                        onSort(sortingData)
                    },
                ) {
                    Text("保存")
                }
            },
            dismissButton = {
                TextButton({ showDialog = false }) {
                    Text("取消")
                }
            },
        )
    }
}

@Preview
@Composable
fun ItemPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    SortableItem(
        values = { items },
        onSort = {},
        exposed = { list -> Text(text = "Exposed: ${list.joinToString()}") },
        item = { item -> Text(text = item) },
        key = { it },
        title = { Text("Sort Items") },
        description = { Text("Drag and drop to reorder items.") },
        dialogDescription = { Text("Dialog: Drag and drop to reorder items.") },
        dialogItemDescription = { item -> Text("Description for $item") },
        icon = { Icon(Icons.Rounded.Reorder, contentDescription = "Sort Icon") },
        onConfirm = {}
    )
}
