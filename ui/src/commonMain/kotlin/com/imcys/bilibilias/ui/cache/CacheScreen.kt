package com.imcys.bilibilias.ui.cache

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.logic.cache.CacheComponent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CacheScreen(component: CacheComponent) {

}

// todo 点击事件 在下载时是 暂停/恢复 下载完成后是播放
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent() {
    Scaffold { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(10) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Text(
                            "[3Dio Binaural] Immersive Ear Licking ASMR｜Instant Sleep Trigger｜No Talking Brain Tingles｜晓美ASMR",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleSmall
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCacheContent() {
    SearchContent()
}