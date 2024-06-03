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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.imcys.bilibilias.core.designsystem.component.AsTextButton
import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.download.Format
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.core.ui.radio.CodecsRadioGroup
import com.imcys.bilibilias.core.ui.radio.FileTypeRadioGroup
import com.imcys.bilibilias.core.ui.radio.rememberCodecsState
import com.imcys.bilibilias.core.ui.radio.rememberFileTypeState
import kotlin.reflect.KFunction0

@Composable
fun ToolContent(component: ToolComponent, navigationToSettings: KFunction0<Unit>) {
//    viewModel(modelClass = )
    val searchQuery by component.searchQuery.collectAsStateWithLifecycle()
    val searchResultUiState by component.searchResultUiState.collectAsStateWithLifecycle()
    ToolContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = component::onSearchQueryChanged,
        onClearSearches = component::clearSearches,
        searchResultUiState = searchResultUiState,
        onDownload = component::download,
        onSetting = navigationToSettings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearches: () -> Unit,
    searchResultUiState: SearchResultUiState,
    onDownload: (DownloadRequest) -> Unit,
    onSetting: () -> Unit
) {
    Scaffold(
        topBar = {
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
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "清空输入框",
                            modifier = Modifier.clickable { onClearSearches() }
                        )
                    }
                },
                singleLine = true,
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
                            AsyncImage(
                                model = searchResultUiState.face,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
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
            val codecsState = rememberCodecsState()
            val typeState = rememberFileTypeState()
            var currentQuality by remember { mutableStateOf(streamDesc.descriptionQuality.first()) }
            Column {
                LazyRow {
                    items(streamDesc.descriptionQuality) { item ->
                        TextButton(
                            onClick = { currentQuality = item },
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
                CodecsRadioGroup(codecsState)
                FileTypeRadioGroup(typeState)
                AsTextButton(
                    onClick = {
                        onDownload(
                            Format(
                                codecsState.current.codeid,
                                typeState.current,
                                currentQuality.quality
                            )
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text(text = "下载")
                }
            }
        }
    }
}
