package com.imcys.bilibilias.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VideoCameraBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorRoute

@Composable
fun ToolsScreen(vm: HomeViewModel, onToPage: (NavKey) -> Unit) {
    Column(
        Modifier
            .padding(horizontal = 15.dp)
            .padding(top = 10.dp),
    ) {
        ToolsContent(vm, onToPage)
    }
}


data class ToolInfo(
    val name: String,
    val desc: String,
    val icon: ImageVector? = null,
    val iconRes: Int? = null,
    val navKey: NavKey
)

@Composable
private fun ToolsContent(vm: HomeViewModel, onToPage: (NavKey) -> Unit) {

    val tools = listOf(
        ToolInfo(
            name = "逐帧提取",
            desc = "从视频中逐帧提取图片，画手书的好帮手！",
            icon = Icons.Outlined.VideoCameraBack,
            navKey = FrameExtractorRoute
        )
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Text("视频处理")
        }
        items(tools) {
            ToolCard(it, onClick = {
                onToPage.invoke(it.navKey)
            })
        }
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun ToolCard(
    toolInfo: ToolInfo = ToolInfo(
        name = "逐帧提取",
        desc = "从视频中逐帧提取图片，画手书的好帮手！",
        icon = Icons.Outlined.VideoCameraBack,
        navKey = FrameExtractorRoute
    ),
    onClick: () -> Unit = { }
) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = CardDefaults.shape, onClick = onClick) {
        Column(
            Modifier
                .padding(10.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialShapes.Circle.toShape()
            ) {
                Icon(
                    toolInfo.icon!!,
                    contentDescription = "图标",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(22.dp)
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(toolInfo.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(
                toolInfo.desc,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                minLines = 2
            )
        }
    }
}