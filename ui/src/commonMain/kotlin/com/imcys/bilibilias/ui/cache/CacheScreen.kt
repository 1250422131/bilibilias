package com.imcys.bilibilias.ui.cache

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.logic.cache.CacheComponent
import com.imcys.bilibilias.logic.cache.EpisodeCacheState

@Composable
fun CacheScreen(component: CacheComponent) {
    val state by component.uiState.collectAsState()
    CaCheContent(state)

}

// todo 点击事件 在下载时是 暂停/恢复 下载完成后是播放
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaCheContent(state: List<EpisodeCacheState>) {
    Scaffold { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(state) { item ->
                Card(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            Text(
                                item.episodeMetadata.title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Row {
                            IconButton({}) {
                                Icon(Icons.Outlined.Info, contentDescription = null)
                            }
                            IconButton({}) {
                                Icon(Icons.Outlined.ArrowOutward, contentDescription = null)
                            }
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}