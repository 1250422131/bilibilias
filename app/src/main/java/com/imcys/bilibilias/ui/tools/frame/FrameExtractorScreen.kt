package com.imcys.bilibilias.ui.tools.frame

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import coil3.Bitmap
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.ASPlayer
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object FrameExtractorRoute : NavKey


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
    })

}

@Composable
fun SelectVideoListDialog(
    show: Boolean,
    downloadList: List<DownloadSegment>,
    onClose: () -> Unit
) {
    ASAlertDialog(
        showState = show,
        title = { Text("选择导入视频") },
        text = {
            LazyColumn(
                modifier = Modifier.sizeIn(maxHeight = 600.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(downloadList) { item ->
                    Surface(
                        shape = CardDefaults.shape,
                        modifier = Modifier.fillMaxWidth()
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
                                    contentDescription = "封面图片"
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
                                    maxLines = 2,
                                    minLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }
                        }
                    }
                }
            }
        }, confirmButton = {
            Text("取消", Modifier.clickable {
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
        title = { Text("选择导入视频方式") },
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
                        Text("从已下载导入")
                    }
                }
                Spacer(Modifier.height(5.dp))
                Surface(
                    shape = CardDefaults.shape,
                    border = CardDefaults.outlinedCardBorder(),
                    color = Color.Transparent,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                    }
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.FileOpen, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text("从本地文件导入")
                    }
                }

            }
        }, confirmButton = {
            Text("取消", Modifier.clickable {
                onClose.invoke()
            })
        })
}

@Composable
private fun FrameExtractorContent(
    vm: FrameExtractorViewModel,
    paddingValues: PaddingValues
) {
    val uiState by vm.uiState.collectAsState()
    var selectFps by remember { mutableIntStateOf(1) }
    val fpsList by vm.fpsList.collectAsState()
    val context = LocalContext.current

    Column(
        Modifier
            .padding(paddingValues)
            .padding(horizontal = 10.dp)
            .padding(top = 5.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (uiState.selectVideoPath.isNullOrEmpty()) {
            ASWarringTip {
                Text("请在右上角选择导入视频（暂未实现该页面功能，请留意QQ频道更新）")
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                shape = CardDefaults.shape
            ) {
                ASPlayer(Modifier.fillMaxSize())
            }

            Spacer(Modifier.height(10.dp))
            // 选择帧率组件
            SelectFpsItem(selectFps, {
                selectFps = it
            }, 1..30)
            Spacer(Modifier.height(10.dp))
            Text("总计:${fpsList.size}", fontSize = 16.sp)
            FrameList(fpsList, {
                vm.getFrameBitmap(context, it)
            })
            ExportFpsButton()
        }
    }
}

@Composable
fun FrameList(fpsList: List<String>, onLoadImage: (String) -> Bitmap?) {
    LazyRow (
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        items(fpsList) { item ->
            onLoadImage.invoke(item)?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "帧图片",
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(16 / 9f),
                )
            }

        }
    }
}

@Composable
fun ExportFpsButton() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
            modifier = Modifier,
            onClick = { }
        ) {
            Text(
                "导出逐帧图片",
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectFpsItem(
    fps: Int, updateFps: (Int) -> Unit,
    valueRange: IntRange,
) {

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("提取帧率")
        Spacer(Modifier.height(10.dp))

        Spacer(Modifier.height(10.dp))
        Surface(shape = CardDefaults.shape) {
            Row(
                modifier = Modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    enabled = fps - 1 >= valueRange.start,
                    shape = CardDefaults.shape,
                    onClick = {
                        updateFps(fps - 1)
                    }
                ) {
                    Text("-", fontSize = 20.sp)
                }

                BasicTextField(
                    value = "$fps",
                    onValueChange = {
                        val num = it.toIntOrNull()
                        if (num != null && num in valueRange.start..valueRange.endInclusive) {
                            updateFps(num)
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
                        Text("帧/秒", modifier = Modifier.padding(horizontal = 6.dp))
                    }
                }

                Button(
                    enabled = fps <= valueRange.endInclusive - 1,
                    shape = CardDefaults.shape,
                    onClick = {
                        updateFps(fps + 1)
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
                        Text(text = "逐帧提取")
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
                                contentDescription = "操作"
                            )
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("打开视频") },
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
