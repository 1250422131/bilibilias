package com.imcys.bilibilias.weight

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.dwonload.AppDownloadTask
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.skydoves.cloudy.cloudy
import kotlin.text.ifEmpty


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadTaskCard(task: AppDownloadTask) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
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
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(16f / 9f),
            ) {

                ASAsyncImage(
                    "${
                        task.cover?.ifEmpty { task.downloadTask.cover }
                            ?.toHttps()
                    }@672w_378h_1c.avif",
                    modifier = Modifier
                        .run {
                            if (task.downloadState != DownloadState.WAITING) {
                                cloudy()
                            } else this
                        }
                        .fillMaxSize(),
                    shape = CardDefaults.shape,
                    contentDescription = "封面图片"
                )

                when (task.downloadState) {
                    DownloadState.WAITING -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(25.dp)
                        )
                    }

                    DownloadState.PAUSE -> {}
                    DownloadState.MERGING,
                    DownloadState.DOWNLOADING -> {
                        val animatedProgress by
                        animateFloatAsState(
                            targetValue = task.progress,
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
                        )
                        CircularWavyProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(25.dp)
                        )
                    }

                    DownloadState.COMPLETED -> {}
                    DownloadState.ERROR -> {}
                }

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
                    maxLines = 2,
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
                            text = task.downloadSegment.downloadMode.title,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.W400,
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadFinishTaskCard(downloadSegment: DownloadSegment, onDeleteTaskAndFile: () -> Unit) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
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
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .aspectRatio(16f / 9f),
            ) {

                ASAsyncImage(
                    "${
                        downloadSegment.cover?.toHttps()
                    }@672w_378h_1c.avif",
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
                    text = downloadSegment.title,
                    maxLines = 2,
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
                IconButton(onClick = {
                    showDeleteDialog = true
                }) {
                    Icon(Icons.Outlined.Delete, contentDescription = "删除下载任务")
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
                    androidx.compose.material3.TextButton(
                        onClick = {
                            onDeleteTaskAndFile()
                            showDeleteDialog = false
                        }
                    ) {
                        Text("删除")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { showDeleteDialog = false }
                    ) {
                        Text("取消")
                    }
                }
            )
        }
    }
}