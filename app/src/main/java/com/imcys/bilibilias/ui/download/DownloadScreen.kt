package com.imcys.bilibilias.ui.download


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.ui.download.navigation.DownloadRoute
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.DownloadFinishTaskCard
import com.imcys.bilibilias.weight.DownloadTaskCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DownloadScreen(route: DownloadRoute, onToBack: () -> Unit) {
    val vm = koinViewModel<DownloadViewModel>()
    val downloadListState by vm.downloadListState.collectAsState()
    val allDownloadSegment by vm.allDownloadSegment.collectAsState()
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    var selectIndex by remember { mutableIntStateOf(0) }
    var downloadFinishEditState by remember { mutableStateOf(false) }
    val selectDeleteList = remember { mutableStateListOf<DownloadSegment>() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(route.defaultListIndex) {
        selectIndex = route.defaultListIndex
    }

    val completedSegments = allDownloadSegment.filter {
        it.downloadState == DownloadState.COMPLETED
    }

    LaunchedEffect(completedSegments) {
        if (completedSegments.isEmpty()) {
            downloadFinishEditState = false
        }
    }

    LaunchedEffect(downloadFinishEditState) {
        selectDeleteList.clear()
    }

    BackHandler(enabled = true) {
        if (downloadFinishEditState) {
            downloadFinishEditState = false
        } else {
            onToBack()
        }
    }

    fun updateSelectItems(segment: DownloadSegment) {
        if (selectDeleteList.contains(segment)) {
            selectDeleteList.remove(segment)
        } else {
            selectDeleteList.add(segment)
        }
    }

    DownloadScaffold(onToBack = onToBack) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            AnimatedContent(downloadFinishEditState, label = "") { state ->
                if (state) {
                    EditTopTools(
                        completedSegments, selectDeleteList,
                        onUpdateDownloadFinishEditState = {
                            downloadFinishEditState = it
                        },
                        onUpdateShowDeleteDialog = {
                            showDeleteDialog = it
                        }
                    )
                } else {
                    PageChangeTools(selectIndex, haptics, onUpdateSelectIndex = {
                        selectIndex = it
                    })
                }
            }

            LazyColumn(
                modifier = Modifier.padding(bottom = 10.dp, end = 10.dp, start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                when (selectIndex) {
                    0 -> {
                        items(downloadListState, key = { it.downloadSegment.platformId }) {
                            DownloadTaskCard(
                                modifier = Modifier.animateItem(),
                                task = it,
                                onPause = {
                                    vm.pauseDownloadTask(it.downloadSegment.segmentId)
                                },
                                onResume = {
                                    vm.resumeDownloadTask(it.downloadSegment.segmentId)
                                },
                                onCancel = {
                                    vm.cancelDownloadTask(it.downloadSegment.segmentId)
                                }
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
                                            updateSelectItems(segment)
                                        } else {
                                            vm.openDownloadSegmentFile(context, segment)
                                        }
                                    },
                                downloadSegment = segment,
                                downloadFinishEditState = downloadFinishEditState,
                                selectDeleteList = selectDeleteList,
                                onDeleteTaskAndFile = {
                                    vm.deleteDownloadSegment(context, segment)
                                },
                                onSelect = {
                                    updateSelectItems(segment)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDeleteDialog) {
            // 显示删除对话框
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(R.string.download_delete)) },
                text = { Text(stringResource(R.string.download_confirm)) },
                confirmButton = {
                    ASTextButton(
                        onClick = {
                            vm.downloadSelectedTasks(selectDeleteList.toList())
                            showDeleteDialog = false
                        }
                    ) {
                        Text(stringResource(R.string.common_delete))
                    }
                },
                dismissButton = {
                    ASTextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text(stringResource(R.string.common_cancel))
                    }
                }
            )
        }

    }


}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PageChangeTools(
    selectIndex: Int,
    haptics: HapticFeedback,
    onUpdateSelectIndex: (Int) -> Unit = {}
) {
    Row(Modifier.padding(10.dp)) {
        ToggleButton(
            checked = selectIndex == 0,
            onCheckedChange = {
                if (it) {
                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onUpdateSelectIndex(0)
                }
            },
        ) {
            Text(stringResource(R.string.download_downloading))
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
            Text(stringResource(R.string.download_download_4))
        }
    }
}

@Composable
private fun EditTopTools(
    completedSegments: List<DownloadSegment>,
    selectDeleteList: SnapshotStateList<DownloadSegment>,
    onUpdateDownloadFinishEditState: (Boolean) -> Unit = { _ -> },
    onUpdateShowDeleteDialog: (Boolean) -> Unit = { _ -> },
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
                completedSegments.forEach { completedSegment ->
                    if (completedSegment in selectDeleteList) {
                        selectDeleteList.remove(completedSegment)
                    } else {
                        selectDeleteList.add(completedSegment)
                    }
                }
            },
            border = CardDefaults.outlinedCardBorder()
        ) {
            Text(stringResource(R.string.download_deselect))
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
            onClick = {
                onUpdateDownloadFinishEditState.invoke(false)
            },
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
            onClick = {
                onUpdateShowDeleteDialog(true)
            },
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
fun DownloadScaffold(onToBack: () -> Unit = {}, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = { Text(text = stringResource(R.string.download_download_2)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        AsBackIconButton(onClick = onToBack)
                    },
                )
            }
        },
    ) { paddingValues ->
        content(paddingValues)
    }
}