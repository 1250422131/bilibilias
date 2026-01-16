package com.imcys.bilibilias.ui.download

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.event.sendToastEvent
import com.imcys.bilibilias.common.event.sendToastEventOnBlocking
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.download.navigation.DownloadRoute
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.DownloadFinishTaskCard
import com.imcys.bilibilias.weight.DownloadTaskCard
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DownloadScreen(route: DownloadRoute, onToBack: () -> Unit) {
    val vm = koinViewModel<DownloadViewModel>()
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    // 收集状态
    val downloadListState by vm.downloadListState.collectAsState()
    val completedSegments by vm.completedSegments.collectAsState()
    val currentSortType by vm.downloadSortType.collectAsState()

    // 本地 UI 状态
    var selectIndex by remember { mutableIntStateOf(0) }
    var downloadFinishEditState by remember { mutableStateOf(false) }
    val selectDeleteList = remember { mutableStateListOf<DownloadSegment>() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // 收集事件
    LaunchedEffect(Unit) {
        vm.uiEvent.collect { event ->
            when (event) {
                is DownloadUiEvent.ShowToast -> {
                    sendToastEvent(event.message)
                }

                is DownloadUiEvent.OpenFile -> {
                    openFile(context, event.segment)
                }
            }
        }
    }

    // 同步默认标签页
    LaunchedEffect(route.defaultListIndex) {
        selectIndex = route.defaultListIndex
    }

    // 当没有已完成项目时退出编辑模式
    LaunchedEffect(completedSegments) {
        if (completedSegments.isEmpty()) {
            downloadFinishEditState = false
        }
    }

    // 退出编辑模式时清空选择列表
    LaunchedEffect(downloadFinishEditState) {
        selectDeleteList.clear()
    }

    // 返回键处理
    BackHandler(enabled = true) {
        if (downloadFinishEditState) {
            downloadFinishEditState = false
        } else {
            onToBack()
        }
    }

    DownloadScaffold(onToBack = onToBack) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            AnimatedContent(downloadFinishEditState, label = "toolbar") { isEditing ->
                if (isEditing) {
                    EditTopTools(
                        completedSegments = completedSegments,
                        selectDeleteList = selectDeleteList,
                        onCancelEdit = { downloadFinishEditState = false },
                        onShowDeleteDialog = { showDeleteDialog = true }
                    )
                } else {
                    PageChangeTools(
                        selectIndex = selectIndex,
                        haptics = haptics,
                        currentSortType = currentSortType,
                        onUpdateSelectIndex = { selectIndex = it },
                        onUpdateSortType = { vm.updateDownloadSortType(it) }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.padding(bottom = 10.dp, end = 10.dp, start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                when (selectIndex) {
                    0 -> {
                        items(downloadListState, key = { it.downloadSegment.platformId }) { task ->
                            DownloadTaskCard(
                                modifier = Modifier.animateItem(),
                                task = task,
                                onPause = { vm.pauseDownloadTask(task.downloadSegment.segmentId) },
                                onResume = { vm.resumeDownloadTask(task.downloadSegment.segmentId) },
                                onCancel = { vm.cancelDownloadTask(task.downloadSegment.segmentId) }
                            )
                        }
                    }

                    1 -> {
                        items(completedSegments, key = { it.segmentId }) { segment ->
                            DownloadFinishTaskCard(
                                modifier = Modifier
                                    .animateItem()
                                    .combinedClickable(
                                        onLongClick = {
                                            downloadFinishEditState = !downloadFinishEditState
                                        }
                                    ) {
                                        if (downloadFinishEditState) {
                                            toggleSelection(selectDeleteList, segment)
                                        } else {
                                            vm.requestOpenFile(segment)
                                        }
                                    },
                                downloadSegment = segment,
                                downloadFinishEditState = downloadFinishEditState,
                                selectDeleteList = selectDeleteList,
                                onDeleteTaskAndFile = { vm.deleteDownloadSegment(segment) },
                                onSelect = { toggleSelection(selectDeleteList, segment) }
                            )
                        }
                    }
                }
            }
        }

        // 删除确认对话框
        if (showDeleteDialog) {
            DeleteConfirmDialog(
                onConfirm = {
                    vm.deleteSelectedTasks(selectDeleteList.toList())
                    showDeleteDialog = false
                    downloadFinishEditState = false
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}

// region 辅助函数
private fun toggleSelection(list: SnapshotStateList<DownloadSegment>, segment: DownloadSegment) {
    if (segment in list) {
        list.remove(segment)
    } else {
        list.add(segment)
    }
}

private fun openFile(context: Context, segment: DownloadSegment) {
    val savePath = segment.savePath
    val (uri, type) = if (savePath.startsWith("content://")) {
        val fileUri = savePath.toUri()
        fileUri to (context.contentResolver.getType(fileUri) ?: "")
    } else {
        val file = File(savePath)
        val fileUri = runCatching {
            getUriForFile(context, "${context.applicationContext.packageName}.provider", file)
        }.getOrNull()

        if (fileUri == null) {
            sendToastEventOnBlocking("无法打开此文件，可能没有合适的应用")
            return
        }
        fileUri to (context.contentResolver.getType(fileUri) ?: "")
    }

    val intent = Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(uri, type)
    }

    runCatching {
        context.startActivity(intent)
    }.onFailure {
        sendToastEventOnBlocking("无法打开此文件，可能没有合适的应用")
    }
}
// endregion

// region 子组件
@Composable
private fun DeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.download_batch_delete_title)) },
        text = { Text(stringResource(R.string.download_delete_confirm_message)) },
        confirmButton = {
            ASTextButton(onClick = onConfirm) {
                Text(stringResource(R.string.common_delete))
            }
        },
        dismissButton = {
            ASTextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PageChangeTools(
    selectIndex: Int,
    haptics: HapticFeedback,
    currentSortType: AppSettings.DownloadSortType,
    onUpdateSelectIndex: (Int) -> Unit,
    onUpdateSortType: (AppSettings.DownloadSortType) -> Unit
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleButton(
            checked = selectIndex == 0,
            onCheckedChange = {
                if (it) {
                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onUpdateSelectIndex(0)
                }
            },
        ) {
            Text(stringResource(R.string.status_downloading_title))
        }

        Spacer(Modifier.width(10.dp))

        ToggleButton(
            checked = selectIndex == 1,
            onCheckedChange = {
                if (it) {
                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onUpdateSelectIndex(1)
                }
            },
        ) {
            Text(stringResource(R.string.status_completed_title))
        }

        // 排序选择器（仅在已完成标签页显示）
        if (selectIndex == 1) {
            Spacer(Modifier.weight(1f))
            Box {
                TextButton(onClick = { showSortMenu = true }) {
                    Text(getSortTypeDisplayName(currentSortType))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    AppSettings.DownloadSortType.entries
                        .filter { it != AppSettings.DownloadSortType.UNRECOGNIZED }
                        .forEach { sortType ->
                            DropdownMenuItem(
                                text = { Text(getSortTypeDisplayName(sortType)) },
                                onClick = {
                                    onUpdateSortType(sortType)
                                    showSortMenu = false
                                }
                            )
                        }
                }
            }
        }
    }
}

private fun getSortTypeDisplayName(sortType: AppSettings.DownloadSortType): String {
    return when (sortType) {
        AppSettings.DownloadSortType.DownloadSort_TimeDesc -> "时间↓"
        AppSettings.DownloadSortType.DownloadSort_TimeAsc -> "时间↑"
        AppSettings.DownloadSortType.DownloadSort_TitleAsc -> "标题A→Z"
        AppSettings.DownloadSortType.DownloadSort_TitleDesc -> "标题Z→A"
        AppSettings.DownloadSortType.DownloadSort_SizeDesc -> "大小↓"
        AppSettings.DownloadSortType.DownloadSort_SizeAsc -> "大小↑"
        else -> "时间↓"
    }
}

@Composable
private fun EditTopTools(
    completedSegments: List<DownloadSegment>,
    selectDeleteList: SnapshotStateList<DownloadSegment>,
    onCancelEdit: () -> Unit,
    onShowDeleteDialog: () -> Unit
) {
    Row(
        Modifier
            .padding(10.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedButton(
            shape = CardDefaults.shape,
            onClick = {
                completedSegments.forEach { segment ->
                    toggleSelection(selectDeleteList, segment)
                }
            },
            border = CardDefaults.outlinedCardBorder()
        ) {
            Text(stringResource(R.string.download_deselect_all))
        }

        OutlinedButton(
            shape = CardDefaults.shape,
            onClick = {
                selectDeleteList.clear()
                selectDeleteList.addAll(completedSegments)
            },
        ) {
            Text(stringResource(R.string.download_select_all))
        }

        Button(
            shape = CardDefaults.shape,
            onClick = onCancelEdit,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Text(stringResource(R.string.common_cancel))
        }

        Button(
            enabled = selectDeleteList.isNotEmpty(),
            shape = CardDefaults.shape,
            onClick = onShowDeleteDialog,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(stringResource(R.string.common_delete))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadScaffold(
    onToBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                style = BILIBILIASTopAppBarStyle.Small,
                title = { Text(text = "下载管理") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                navigationIcon = {
                    AsBackIconButton(onClick = onToBack)
                },
            )
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}
// endregion