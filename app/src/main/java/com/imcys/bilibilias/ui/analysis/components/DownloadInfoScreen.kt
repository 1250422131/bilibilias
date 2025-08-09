package com.imcys.bilibilias.ui.analysis.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurls
import com.imcys.bilibilias.network.model.video.BILIVideoSupportFormat
import com.imcys.bilibilias.network.model.video.convertAudioQualityIdValue
import com.imcys.bilibilias.ui.analysis.AnalysisViewModel
import kotlin.collections.forEach

/**
 * 音频质量选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioQualitySelectScreen(
    modifier: Modifier,
    downloadInfo: DownloadViewInfo?,
    audioList: List<BILIVideoDash.Audio>?,
    onAudioQualityChange: (Long?) -> Unit = {}
) {
    var modelExpanded by remember { mutableStateOf(false) }
    var selectValue: Long by remember { mutableLongStateOf(0) }

    LaunchedEffect(downloadInfo?.selectAudioQualityId) {
        selectValue = downloadInfo?.selectAudioQualityId ?: 0
    }

    if (!audioList.isNullOrEmpty()) {
        ExposedDropdownMenuBox(
            expanded = modelExpanded,
            onExpandedChange = {
                modelExpanded = it
            },
            modifier = modifier,
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 12.sp
                ),
                value = convertAudioQualityIdValue(selectValue),
                onValueChange = {},
                readOnly = true,
                singleLine = false,
                label = { Text("选择优先音频质量", fontSize = 12.sp) },
                trailingIcon = { TrailingIcon(expanded = false) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                shape = CardDefaults.shape
            )
            ExposedDropdownMenu(
                expanded = modelExpanded,
                onDismissRequest = { modelExpanded = false },
            ) {
                audioList.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                convertAudioQualityIdValue(it.id),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            modelExpanded = false
                            selectValue = it.id
                            onAudioQualityChange(it.id)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

/**
 * 视频质量与编码选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoSupportFormatsSelectScreen(
    modifier: Modifier,
    downloadInfo: DownloadViewInfo?,
    mSupportFormats: List<BILIVideoSupportFormat>?,
    dashVideoList: List<BILIVideoDash.Video>?,
    durlVideoList: List<BILIVideoDurls>?,
    onVideoQualityChange: (Long?) -> Unit = {},
    onVideoCodeChange: (String) -> Unit = {}
) {
    var videoModelExpanded by remember { mutableStateOf(false) }
    var videoCodeModelExpanded by remember { mutableStateOf(false) }
    // 选择的视频分辨率
    var selectVideoFormatValue: String by remember { mutableStateOf("") }
    // 选择的视频编码
    var selectVideoCodeValue: String by remember { mutableStateOf("") }

    var supportFormats by remember { mutableStateOf(listOf<BILIVideoSupportFormat>()) }
    var videoCodingList by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(downloadInfo?.selectVideoCode) {
        selectVideoCodeValue = downloadInfo?.selectVideoCode ?: ""
    }

    LaunchedEffect(downloadInfo?.selectVideoQualityId) {
        supportFormats = if (dashVideoList != null) {
            // Dash模式
            val mVideoCodingList = mutableSetOf<String>()
            mSupportFormats?.filter {
                // 筛选出支持的清晰度
                it.quality == downloadInfo?.selectVideoQualityId
            }?.forEach {
                it.codecs.forEach { code ->
                    mVideoCodingList.add(code.split(".")[0])
                }
            }
            videoCodingList = mVideoCodingList
            // 更新视频编码选择
            mVideoCodingList.firstOrNull()?.let {
                onVideoCodeChange(it)
            }
            mSupportFormats?.filter { supportFormat ->
                dashVideoList.any { item -> item.id == supportFormat.quality }
            } ?: emptyList()
        } else {
            // FLV模式
            mSupportFormats?.filter { supportFormat ->
                durlVideoList?.any { item -> item.quality == supportFormat.quality } == true
            }?.ifEmpty {
                // 充电视频下有可能找不到对应的清晰度
                mSupportFormats
            } ?: emptyList()
        }
        selectVideoFormatValue = supportFormats.firstOrNull {
            it.quality == downloadInfo?.selectVideoQualityId
        }?.run {
            description.ifBlank { newDescription }
        } ?: ""
    }



    Row(
        modifier.animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = videoModelExpanded,
            onExpandedChange = {
                videoModelExpanded = it
            },
            modifier = Modifier.weight(1f),
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 12.sp
                ),
                value = selectVideoFormatValue,
                onValueChange = {

                },
                readOnly = true,
                singleLine = false,
                label = { Text("选择优先分辨率", fontSize = 12.sp) },
                trailingIcon = { TrailingIcon(expanded = false) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                shape = CardDefaults.shape
            )

            ExposedDropdownMenu(
                expanded = videoModelExpanded,
                onDismissRequest = { videoModelExpanded = false },
            ) {
                supportFormats.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                it.description.ifBlank { it.newDescription },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            videoModelExpanded = false
                            selectVideoFormatValue = it.description.ifBlank { it.newDescription }
                            onVideoQualityChange(it.quality)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }

        }

        if (videoCodingList.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = videoCodeModelExpanded,
                onExpandedChange = {
                    videoCodeModelExpanded = it
                },
                modifier = Modifier.weight(1f),
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 12.sp
                    ),
                    value = selectVideoCodeValue,
                    onValueChange = {

                    },
                    readOnly = true,
                    singleLine = false,
                    label = { Text("选择优先编码", fontSize = 12.sp) },
                    trailingIcon = { TrailingIcon(expanded = false) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    shape = CardDefaults.shape
                )

                ExposedDropdownMenu(
                    expanded = videoCodeModelExpanded,
                    onDismissRequest = { videoCodeModelExpanded = false },
                ) {
                    videoCodingList.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                videoCodeModelExpanded = false
                                selectVideoCodeValue = it
                                onVideoCodeChange(it)
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }

            }
        }
    }
}
