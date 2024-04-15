package com.imcys.bilibilias.feature.tool

import DownloadFileRequest
import SearchResultUiState
import VideoStreamDesc
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearches: () -> Unit,
    searchResultUiState: SearchResultUiState,
    onDownload: (DownloadFileRequest) -> Unit,
    modifier: Modifier,
    onSetting: () -> Unit
) {
    Scaffold(modifier, topBar = {
        TopAppBar(
            title = { Text(text = "haha") },
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
                }
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
                            TextButton(
                                onClick = {
//                                    AsVideoActivity.actionStart(
//                                        context,
//                                        searchResultUiState.bvid
//                                    )
                                }
                            ) {
                                Text(text = "点击进入详情页")
                            }
                        }
                        items(searchResultUiState.collection, key = { it.cid }) { item ->
                            ViewItem(item.title, item.videoStreamDesc) {
                                onDownload(
                                    DownloadFileRequest(
                                        searchResultUiState.aid,
                                        searchResultUiState.bvid,
                                        item.cid,
                                        it
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun ViewItem(
    title: String,
    videoStreamDesc: VideoStreamDesc,
    selected: (Int) -> Unit
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
            Text(text = title)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column {
                LazyRow {
                    items(videoStreamDesc.descriptionQuality) {
                        TextButton(onClick = { selected(it.quality) }) {
                            Text(text = it.desc)
                        }
                    }
                }
            }
        }
    }
}