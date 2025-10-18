package com.imcys.bilibilias.weight

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.dwonload.AppDownloadTask
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTextButton
import kotlin.math.ceil
import kotlin.text.ifEmpty


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadTaskCard(
    modifier: Modifier = Modifier,
    task: AppDownloadTask,
    onPause: () -> Unit = {},
    onResume: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = CardDefaults.shape,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // 左侧图片
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
            ) {

                ASAsyncImage(
                    "${
                        task.cover?.ifEmpty { task.downloadTask.cover }
                            ?.toHttps()
                    }@540w_404h",
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
                    text = task.downloadSegment.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400
                )
                Spacer(Modifier.height(12.dp))

                val animatedProgress by
                animateFloatAsState(
                    targetValue = task.progress,
                    animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                )

                when (task.downloadState) {
                    DownloadState.COMPLETED,
                    DownloadState.WAITING -> {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    DownloadState.PAUSE,
                    DownloadState.MERGING,
                    DownloadState.DOWNLOADING -> {
                        LinearWavyProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        )
                    }

                    DownloadState.ERROR -> {}
                    DownloadState.CANCELLED -> {}
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        when (task.downloadState) {
                            DownloadState.WAITING -> "等待中"
                            DownloadState.PAUSE -> "已暂停"
                            DownloadState.DOWNLOADING -> "下载中:${ceil(animatedProgress * 100).toInt()}%"
                            DownloadState.MERGING -> "合并中:${ceil(animatedProgress * 100).toInt()}%"
                            DownloadState.COMPLETED -> "已完成"
                            DownloadState.ERROR -> "错误"
                            DownloadState.CANCELLED -> "已取消"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400
                    )

                    Spacer(Modifier.weight(1f))

                    Surface(
                        shape = CardDefaults.shape,
                        color = MaterialTheme.colorScheme.primary,
                    ) {
                        Text(
                            text = task.downloadSegment.downloadMode.title,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }

                    Spacer(Modifier.width(10.dp))


                    // 暂停时可取消任务
                    if (task.downloadState != DownloadState.DOWNLOADING) {
                        Spacer(Modifier.width(10.dp))
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = "取消下载",
                            modifier = Modifier.clickable {
                                onCancel()
                            }
                        )
                    }

                    Spacer(Modifier.width(4.dp))

                    if (task.downloadState == DownloadState.DOWNLOADING || task.downloadState == DownloadState.PAUSE) {
                        Icon(
                            if (task.downloadState == DownloadState.DOWNLOADING) {
                                Icons.Outlined.Pause
                            } else {
                                Icons.Outlined.PlayArrow
                            },
                            contentDescription = if (task.downloadState == DownloadState.DOWNLOADING) {
                                "暂停下载"
                            } else {
                                "继续下载"
                            },
                            modifier = Modifier.clickable {
                                if (task.downloadState == DownloadState.DOWNLOADING) {
                                    onPause()
                                } else {
                                    onResume()
                                }
                            })

                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadFinishTaskCard(
    modifier: Modifier = Modifier,
    downloadSegment: DownloadSegment,
    downloadFinishEditState: Boolean,
    selectDeleteList: SnapshotStateList<DownloadSegment>,
    onSelect: () -> Unit = {},
    onDeleteTaskAndFile: () -> Unit
) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = CardDefaults.shape,
        modifier = modifier.fillMaxWidth(),
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
                    .weight(0.4f)
                    .aspectRatio(16f / 9f)
                    .fillMaxHeight(),
            ) {
                ASAsyncImage(
                    "${downloadSegment.cover?.toHttps()}",
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
                    .weight(0.6f)
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically), // 垂直居中
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = downloadSegment.title,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = MaterialTheme.colorScheme.primary,
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 0.dp
                            ),
                            text = downloadSegment.downloadMode.title,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.W400,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterVertically), // 垂直居中
                verticalArrangement = Arrangement.Center
            ) {

                AnimatedContent(downloadFinishEditState) { state ->
                    if (!state) {
                        ASIconButton(onClick = {
                            showDeleteDialog = true
                        }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "删除下载任务")
                        }
                    } else {
                        Checkbox(
                            checked = downloadSegment in selectDeleteList,
                            onCheckedChange = {
                                onSelect()
                            }
                        )
                    }
                }
            }
        }

        if (showDeleteDialog) {
            // 显示删除对话框
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("删除下载任务") },
                text = { Text("是否删除该下载任务及其文件？") },
                confirmButton = {
                    ASTextButton(
                        onClick = {
                            onDeleteTaskAndFile()
                            showDeleteDialog = false
                        }
                    ) {
                        Text("删除")
                    }
                },
                dismissButton = {
                    ASTextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("取消")
                    }
                }
            )
        }
    }
}