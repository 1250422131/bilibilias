package com.imcys.space

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.imcys.model.space.SpaceArcSearch

@Composable
internal fun UserSpaceRoute(
    navigateToCollectionDownload: (String) -> Unit,
    viewModel: UserSpaceViewModel = hiltViewModel()
) {
    val spaceArcSearchItems = viewModel.spaceArcSearchPagingSource.collectAsLazyPagingItems()
    UserSpaceScreen(spaceArcSearchItems, navigateToCollectionDownload)
}

@Composable
internal fun UserSpaceScreen(
    spaceArcSearchItems: LazyPagingItems<SpaceArcSearch.Lists.Vlist>,
    navigateToCollectionDownload: (String) -> Unit
) {
    val items = rememberMutableStateListOf<String>()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("已选择: ${items.size} 项") },
                actions = {
                    TextButton(
                        onClick = { navigateToCollectionDownload(items.joinToString(",")) },
                        enabled = items.isNotEmpty()
                    ) {
                        Text(text = "确定")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(spaceArcSearchItems.itemCount, key = spaceArcSearchItems.itemKey()) { index ->
                val item = spaceArcSearchItems[index] ?: return@items
                PagingItem(item.title, item.length, item.pic) {
                    if (item.bvid in items) {
                        items -= item.bvid
                    } else {
                        items += item.bvid
                    }
                }
            }
            if (spaceArcSearchItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun PagingItem(
    title: String,
    duration: String,
    pic: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    var checked by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .then(
                if (checked)
                    Modifier.border(1.dp, MaterialTheme.colorScheme.primary)
                else Modifier
            )
            .clickable { onClick();checked = !checked },
        headlineContent = { },
        overlineContent = {
            CommonTitle(text = title)
        },
        supportingContent = { CommonTitle("supportingContent") },
        leadingContent = {
            AsyncImage(
                model = pic, contentDescription = "视频封面",
                modifier = Modifier
                    .size(128.dp, 72.dp)
                    .drawWithContent {
                        val layoutResult = textMeasurer.measure(
                            duration,
                            style = TextStyle.Default.copy(
                                fontSize = 11.sp,
//                                shadow = Shadow(),
                                background = Color.DarkGray,
                            ),
                        )
                        drawContent()
                        drawText(
                            layoutResult, topLeft = Offset(
                                size.width - layoutResult.size.width,
                                size.height - layoutResult.size.height
                            )
                        )
                    },
                contentScale = ContentScale.Crop
            )
        },
    )
}

val smallFontSize = 11.sp

@Composable
fun CommonTitle(text: String) {
    Text(text = text, fontSize = smallFontSize, maxLines = 2, overflow = TextOverflow.Ellipsis)
}

@Composable
fun <T : Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                if (stateList.isNotEmpty()) {
                    val first = stateList.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }
                stateList.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}