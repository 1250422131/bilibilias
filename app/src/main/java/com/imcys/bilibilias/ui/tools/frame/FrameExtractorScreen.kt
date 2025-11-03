package com.imcys.bilibilias.ui.tools.frame


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorViewModel.UIState
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.ASFramePlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object FrameExtractorRoute : NavKey

private const val AWAIT_TIME = 2000L
@Composable
fun FrameExtractorScreen(
    frameExtractorRoute: FrameExtractorRoute,
    onToBack: () -> Unit
) {


    val vm = koinViewModel<FrameExtractorViewModel>()
    val downloadList by vm.allDownloadSegment.collectAsState()
    val context = LocalContext.current

    var showSelectVideoModel by remember { mutableStateOf(false) }
    var showSelectVideoList by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.initVideoInfo(context)
    }
    DisposableEffect(Unit) {
        onDispose {
            vm.deleteCacheDir(context)
        }
    }
    FrameExtractorScaffold(
        onToBack = onToBack,
        onShowInputVideo = {
            showSelectVideoModel = true
        }
    ) { paddingValues ->
        FrameExtractorContent(vm, paddingValues)
    }

    SelectVideoModelDialog(showSelectVideoModel, onClose = {
        showSelectVideoModel = false
    }, onSelectDownloadFile = {
        showSelectVideoList = true
    })

    SelectVideoListDialog(showSelectVideoList, downloadList, onClose = {
        showSelectVideoList = false
    }, onSelectVideo = {
        vm.importVideo(context, it.savePath, 1)
    })

}

@Composable
fun SelectVideoListDialog(
    show: Boolean,
    downloadList: List<DownloadSegment>,
    onClose: () -> Unit,
    onSelectVideo: (DownloadSegment) -> Unit = {},
) {
    ASAlertDialog(
        showState = show,
        title = { Text(stringResource(R.string.frame_选择导)) },
        text = {
            LazyColumn(
                modifier = Modifier.sizeIn(maxHeight = 600.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(downloadList) { item ->
                    Surface(
                        shape = CardDefaults.shape,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onClose.invoke()
                            onSelectVideo.invoke(item)
                        }
                    ) {

                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // 左侧图片
                            Column(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .fillMaxHeight(),
                            ) {
                                ASAsyncImage(
                                    "${item.cover?.toHttps()}",
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    shape = CardDefaults.shape,
                                    contentDescription = stringResource(R.string.download_封面图)
                                )
                            }


                            Spacer(Modifier.width(10.dp))

                            // 右侧内容
                            Column(
                                modifier = Modifier
                                    .weight(0.7f)
                                    .fillMaxHeight()
                                    .align(Alignment.CenterVertically), // 垂直居中
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W400
                                )

                                Text(
                                    "${formatDuration((item.tempDuration / 1000).toInt())}",
                                    modifier = Modifier.padding(horizontal = 2.dp),
                                    style = LocalTextStyle.current.copy(fontSize = 10.sp)
                                )
                            }
                        }
                    }
                }
            }
        }, confirmButton = {
            Text(stringResource(R.string.common_cancel), Modifier.clickable {
                onClose.invoke()
            })
        })
}

@Composable
fun SelectVideoModelDialog(
    showSelectVideoModel: Boolean,
    onClose: () -> Unit,
    onSelectDownloadFile: () -> Unit,
) {
    val haptics = LocalHapticFeedback.current
    ASAlertDialog(
        showState = showSelectVideoModel, clickBlankDismiss = true,
        title = { Text(stringResource(R.string.frame_选择导_1)) },
        text = {
            Column {
                Surface(
                    shape = CardDefaults.shape,
                    border = CardDefaults.outlinedCardBorder(),
                    color = Color.Transparent,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                        onClose.invoke()
                        onSelectDownloadFile.invoke()
                    }
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.DownloadDone, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text(stringResource(R.string.frame_从已下))
                    }
                }
//                Spacer(Modifier.height(5.dp))
//                Surface(
//                    shape = CardDefaults.shape,
//                    border = CardDefaults.outlinedCardBorder(),
//                    color = Color.Transparent,
//                    onClick = {
//                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
//                    }
//                ) {
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(15.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(Icons.Outlined.FileOpen, contentDescription = null)
//                        Spacer(Modifier.width(10.dp))
//                        Text("从本地文件导入")
//                    }
//                }

            }
        }, confirmButton = {
            Text(stringResource(R.string.common_cancel), Modifier.clickable {
                onClose.invoke()
            })
        })
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FrameExtractorContent(
    vm: FrameExtractorViewModel,
    paddingValues: PaddingValues
) {
    val uiState by vm.uiState.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 10.dp)
            .padding(top = 5.dp)
    ) {

        when (val state = uiState) {
            UIState.Default -> ASWarringTip {
                Text(stringResource(R.string.frame_请在右))
            }

            is UIState.ImportSuccess -> ImportSuccessScreen(state, vm)
            is UIState.Importing -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(10.dp))
                    CircularWavyProgressIndicator(
                        progress = { state.progress },
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        stringResource(R.string.frame_正在导_请稍候),
                        fontSize = 16.sp
                    )
                }
            }

            is UIState.Exporting -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(10.dp))
                    CircularWavyProgressIndicator(
                        progress = { state.progress },
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        stringResource(R.string.frame_正在导_请不要),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

}


@Composable
fun ImportSuccessScreen(state: UIState.ImportSuccess, vm: FrameExtractorViewModel) {
    // 协程
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())

    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f),
            shape = CardDefaults.shape
        ) {
            ASFramePlayer(
                modifier = Modifier
                    .fillMaxSize(),
                list = state.frameList,
                fps = state.selectFps
            )
        }
        Spacer(Modifier.height(10.dp))
        // 选择帧率组件
        SelectFpsItem(state.selectFps, state.videoFps, {
            scope.launch {
                vm.updateSelectFps(currentFps = it)
            }
        }, 1..state.videoFps)
        Spacer(Modifier.height(10.dp))
        ExportFpsButton(onExport = {
            vm.exportFrameToImage(context = context,it)
        })
    }
}

// 秒转换时:分:秒格式
@SuppressLint("DefaultLocale")
private fun formatDuration(duration: Int): String {
    val hours = duration / 3600
    val minutes = (duration % 3600) / 60
    val seconds = duration % 60
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}


@Composable
fun ExportFpsButton(onExport: (String) -> Unit = {}) {
    var showExportDialog by remember { mutableStateOf(false) }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            modifier = Modifier,
            onClick = { showExportDialog = true }
        ) {
            Text(
                stringResource(R.string.frame_导出逐),
                fontSize = 16.sp
            )
        }
    }

    ExportDialog(showExportDialog,onDismiss = {
        showExportDialog = false
    }, onSelectUri = { uri ->
        showExportDialog = false
        onExport(uri)
    })

}

@Composable
fun ExportDialog(
    showExportDialog: Boolean,
    onDismiss: (() -> Unit)? = null,
    onSelectUri: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var selectUri by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            selectUri = it.toString()
            onSelectUri(selectUri)
        }
    }

    ASAlertDialog(
        showState = showExportDialog,
        title = { Text(stringResource(R.string.frame_选择导_2)) },
        text = {
            Text(stringResource(R.string.frame_请选择_建议对))
        },
        confirmButton = {
            ASTextButton(onClick = { launcher.launch(null) }) {
                Text(stringResource(R.string.frame_选择位))
            }
        },
        onDismiss = onDismiss,
        dismissButton = {
            ASTextButton(onClick = { onDismiss?.invoke() }) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectFpsItem(
    fps: Int,
    totalFps: Int,
    updateFps: (Int) -> Unit,
    valueRange: IntRange,
) {
    var currentFps by remember { mutableIntStateOf(fps) }
    val scope = rememberCoroutineScope()
    var updateJob by remember { mutableStateOf<Job?>(null) }

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.frame_最大_帧))
        Spacer(Modifier.height(10.dp))
        Surface(shape = CardDefaults.shape) {
            Row(
                modifier = Modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    enabled = currentFps - 1 >= valueRange.start,
                    shape = CardDefaults.shape,
                    onClick = {
                        updateJob?.cancel()
                        updateJob = scope.launch {
                            currentFps = currentFps - 1
                            delay(AWAIT_TIME)
                            updateFps(currentFps)
                        }
                    }
                ) {
                    Text("-", fontSize = 20.sp)
                }

                BasicTextField(
                    value = "$currentFps",
                    onValueChange = {
                        val num = it.toIntOrNull()
                        val localValue = if (num != null && num > valueRange.endInclusive) {
                            valueRange.endInclusive
                        } else if (num != null && num < valueRange.start) {
                            valueRange.start
                        } else {
                            num ?: 1
                        }
                        updateJob?.cancel()
                        updateJob = scope.launch {
                            currentFps = localValue
                            delay(AWAIT_TIME)
                            updateFps(localValue)
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center
                    ),
                ) { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.widthIn(min = 40.dp)
                    ) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .width(IntrinsicSize.Min)
                                .widthIn(min = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                        Text(stringResource(R.string.frame_帧_秒), modifier = Modifier.padding(horizontal = 6.dp))
                    }
                }

                Button(
                    enabled = fps <= valueRange.endInclusive - 1,
                    shape = CardDefaults.shape,
                    onClick = {
                        updateJob?.cancel()
                        updateJob = scope.launch {
                            currentFps = currentFps + 1
                            delay(AWAIT_TIME)
                            updateFps(currentFps)
                        }
                    }
                ) {
                    Text("+", fontSize = 20.sp)
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FrameExtractorScaffold(
    onToBack: () -> Unit,
    onShowInputVideo: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text(text = stringResource(R.string.tools_逐帧提))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        AsBackIconButton(onClick = {
                            onToBack.invoke()
                        })
                    },
                    actions = {
                        ASIconButton(onClick = {
                            expandedMenu = !expandedMenu
                        }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                contentDescription = stringResource(R.string.login_操作)
                            )
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.frame_打开视)) },
                                onClick = {
                                    expandedMenu = false
                                    onShowInputVideo.invoke()
                                }
                            )
                        }

                    }
                )
            }
        },
    ) {
        content.invoke(it)
    }


}
