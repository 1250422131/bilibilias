package com.imcys.bilibilias.ui.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.DownloadFinishTaskCard
import com.imcys.bilibilias.weight.DownloadTaskCard
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadScreen(onToBack: () -> Unit) {
    val vm = koinViewModel<DownloadViewModel>()
    val downloadListState by vm.downloadListState.collectAsState()
    val allDownloadSegment by vm.allDownloadSegment.collectAsState()

    var selectIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    DownloadScaffold(
        onToBack = onToBack
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            Row(
                Modifier.padding(10.dp)
            ) {
                ToggleButton(
                    checked = selectIndex == 0,
                    onCheckedChange = {
                        selectIndex = 0
                    },
                ) {
                    Text("正在下载")
                }
                Spacer(Modifier.width(10.dp))
                ToggleButton(
                    checked = selectIndex == 1,
                    onCheckedChange = {
                        selectIndex = 1
                    },
                ) {
                    Text("已完成下载")
                }
            }
            LazyColumn(
                modifier = Modifier.padding(bottom = 10.dp, end = 10.dp, start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                when (selectIndex) {
                    0 -> {
                        items(downloadListState, key = { it.downloadSegment.segmentId }) {
                            DownloadTaskCard(
                                it, onPause = {
                                    vm.pauseDownloadTask(it.downloadSegment.segmentId)
                                },
                                onResume = {
                                    vm.resumeDownloadTask(it.downloadSegment.segmentId)
                                })
                        }
                    }

                    1 -> {
                        items(allDownloadSegment.filter {
                            it.downloadState == DownloadState.COMPLETED
                        }, key = { it.segmentId }) {
                            DownloadFinishTaskCard(it, onDeleteTaskAndFile = {
                                vm.deleteDownloadSegment(context, it)
                            })
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadScaffold(onToBack: () -> Unit, content: @Composable (PaddingValues) -> Unit) {

    var showDownloadTip by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text(text = "下载管理")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            onToBack.invoke()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            showDownloadTip = true
                        }) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = "问题提示"
                            )
                        }
                    }
                )
            }
        },
    ) {
        content.invoke(it)
    }

    if (showDownloadTip) {
        DownloadTipDialog(
            onDismiss = { showDownloadTip = false },
            onDownload = {
                showDownloadTip = false
            }
        )
    }

}

@Composable
fun DownloadTipDialog(onDismiss: () -> Unit, onDownload: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("提示") },
        text = {
            Text(
                """
                    下载失败的视频暂时不会被记录到这里。
                    当前页面并非正式页面，只用作测试下载功能。
                """.trimIndent()
            )
        },
        confirmButton = {
            Button(onClick = onDownload) {
                Text("了解")
            }
        },
    )
}