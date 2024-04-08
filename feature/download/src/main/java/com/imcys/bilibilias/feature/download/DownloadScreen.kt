package com.imcys.bilibilias.feature.download

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun DownloadRoute() {
    val viewModel: DownloadViewModel = hiltViewModel()
    val uiState by viewModel.k.collectAsState()
    DownloadScreen(uiState, viewModel::cancel)
}

@Composable
internal fun DownloadScreen(uiState: List<TaskState>, onCancel: (TaskState) -> Unit) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(uiState, { it.type.toString() + it.bvid + it.cid }) { item ->
                TaskItem(item) {
                    onCancel(item)
                }
            }
        }
    }
}

@Composable
fun TaskItem(state: TaskState, onCancel: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(Color(251, 114, 153)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.type.toString(),
                modifier = Modifier,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = state.title,
                    modifier = Modifier,
                    maxLines = 1,
                    fontSize = 16.sp
                )
            }
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier, verticalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "正在下载",
                        modifier = Modifier,
                        color = Color.LightGray
                    )
                    Text(
                        text = state.cid.toString(),
                        modifier = Modifier,
                        color = Color.LightGray
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onCancel) {
                    Icon(
                        Icons.Default.Delete, contentDescription = "删除",
                        tint = Color(251, 114, 153)
                    )
                }
            }
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(1f, false)
                    .fillMaxWidth(),
                progress = { state.progress },
                color = Color(251, 114, 153),
                trackColor = Color(0xffd2e0)
            )
        }
    }
}
