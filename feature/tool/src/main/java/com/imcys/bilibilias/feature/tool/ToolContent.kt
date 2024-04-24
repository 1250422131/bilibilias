package com.imcys.bilibilias.feature.tool

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.designsystem.component.AsTextButton
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.download.Format
import com.imcys.bilibilias.core.ui.radio.CodecsRadioGroup
import com.imcys.bilibilias.core.ui.radio.FileTypeRadioGroup
import com.imcys.bilibilias.core.ui.radio.TaskType
import com.imcys.bilibilias.core.ui.radio.rememberCodecsState
import com.imcys.bilibilias.core.ui.radio.rememberFileTypeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearches: () -> Unit,
    searchResultUiState: SearchResultUiState,
    onDownload: (DownloadRequest) -> Unit,
    modifier: Modifier,
    onSetting: () -> Unit
) {
    Scaffold(modifier, topBar = {
        TopAppBar(
            title = {},
            actions = {
                IconButton(onClick = onSetting) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "设置",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "清空输入框",
                        modifier = Modifier.clickable { onClearSearches() }
                    )
                },
                maxLines = 1,
            )
            when (searchResultUiState) {
                SearchResultUiState.EmptyQuery -> Unit
                SearchResultUiState.LoadFailed -> Unit
                SearchResultUiState.Loading -> Unit
                is SearchResultUiState.Success ->
                    LazyColumn(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        item {
                            val context = LocalContext.current
                            AsTextButton(
                                onClick = {
//                                    AsVideoActivity.actionStart(
//                                        context,
//                                        searchResultUiState.bvid
//                                    )
                                },
                                text = { Text(text = "点击进入详情页") }
                            )
                        }
                        items(searchResultUiState.collection, key = { it.cid }) { item ->
                            ViewItem(item.title, item.videoStreamDesc) {
                                onDownload(
                                    DownloadRequest(
                                        ViewInfo(
                                            searchResultUiState.aid,
                                            searchResultUiState.bvid,
                                            item.cid,
                                            item.title,
                                        ),
                                        it,
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewItem(
    title: String,
    streamDesc: VideoStreamDesc,
    onDownload: (Format) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        onClick = { expanded = !expanded },
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, modifier = Modifier.basicMarquee())
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = expanded) {
            var codecs by remember { mutableStateOf(streamDesc.supportCodecs.first()) }
            val codecsState = rememberCodecsState(codecs.useAV1, codecs.useH265, codecs.useH264)
            val typeState = rememberFileTypeState()
            var currentQuality by remember { mutableStateOf(streamDesc.descriptionQuality.first()) }
            Column {
                LazyRow {
                    itemsIndexed(streamDesc.descriptionQuality) { index, item ->
                        TextButton(
                            onClick = {
                                currentQuality = item
                                codecs = streamDesc.supportCodecs[index]
                            },
                            border = if (currentQuality == item) {
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            } else {
                                null
                            }
                        ) {
                            Text(text = item.desc)
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CodecsRadioGroup(codecsState)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FileTypeRadioGroup(typeState)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsButton(
                        onClick = {
                            onDownload(
                                Format(
                                    codecsState.current.codeid,
                                    typeState.current.mapToDownladTaskType(),
                                    codecs.quality
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                    ) {
                        Text(text = "下载")
                    }
                }
            }
        }
    }
}

private fun TaskType.mapToDownladTaskType(): com.imcys.bilibilias.core.download.TaskType {
    return when (this) {
        TaskType.ALL -> com.imcys.bilibilias.core.download.TaskType.ALL
        TaskType.VIDEO -> com.imcys.bilibilias.core.download.TaskType.VIDEO
        TaskType.AUDIO -> com.imcys.bilibilias.core.download.TaskType.AUDIO
    }
}
