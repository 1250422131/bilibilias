package com.imcys.bilibilias.tool

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
internal fun ToolRoute(
    onNavigateToPlayer: (Long, String, Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToBangumiFollow: () -> Unit,
    viewModel: ToolViewModel = hiltViewModel()
) {
    val searchResultUiState by viewModel.searchResultUiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    ToolScreen(
        navigateToPlayer = onNavigateToPlayer,
        navigateToSetting = onNavigateToSettings,
        navigateToBangumiFollow = onNavigateToBangumiFollow,
        searchQueryChanged = viewModel::onSearchQueryChanged,
        clearSearches = viewModel::clearSearches,
        searchQuery = searchQuery,
        searchResultUiState = searchResultUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ToolScreen(
    navigateToPlayer: (Long, String, Long) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToBangumiFollow: () -> Unit,
    searchQueryChanged: (String) -> Unit,
    clearSearches: () -> Unit,
    searchQuery: String,
    searchResultUiState: SearchResultUiState = SearchResultUiState.Loading,
) {
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    val context = LocalContext.current
                    AsyncImage(
                        model = "https://s1.ax1x.com/2023/02/04/pSyHEy6.png",
                        contentDescription = null,
                        Modifier
                            .size(24.dp)
                            .clickable { /*context.startActivity(WebAsActivity::class.java)*/ },
                        colorFilter = ColorFilter.tint(Color(android.graphics.Color.parseColor("#fb7299")))
                    )
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "设置",
                        Modifier
                            .padding(16.dp)
                            .clickable { navigateToSetting() },
                        tint = Color(android.graphics.Color.parseColor("#fb7299"))
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SearchTextField(
                query = searchQuery,
                onValueChange = searchQueryChanged,
                clearText = clearSearches,
            )
            when (searchResultUiState) {
                SearchResultUiState.EmptyQuery -> Unit
                SearchResultUiState.LoadFailed, SearchResultUiState.Loading -> Unit
                is SearchResultUiState.Success -> {
                    VideoCard(
                        pic = searchResultUiState.pic,
                        title = searchResultUiState.title,
                        desc = searchResultUiState.desc,
                        view = searchResultUiState.view,
                        danmaku = searchResultUiState.danmaku,
                        onClick = {
                            navigateToPlayer(
                                searchResultUiState.aid,
                                searchResultUiState.bvid,
                                searchResultUiState.cid,
                            )
                        },
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxWidth()) {
            item("追番信息导出") {
                ToolItem(
                    imgUrl = "https://s1.ax1x.com/2023/02/05/pS6IsAJ.png",
                    title = "追番信息导出",
                    containerColor = Color(android.graphics.Color.parseColor("#fb7299")),
                    onClick = navigateToBangumiFollow
                )
            }
        }
    }
}

/**
 * @param view 播放量
 * @param danmaku 弹幕数
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoCard(
    pic: String,
    title: String,
    desc: String,
    view: String,
    danmaku: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = pic,
                contentDescription = "视频封面",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
            )
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            Text(
                desc,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 2
            )
            Row {
                // Image(
                //     painter = painterResource(R.drawable.ic_play_num),
                //     contentDescription = "播放数",
                //     Modifier.size(18.dp),
                //     colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                // )
                Text(
                    text = view,
                    Modifier.padding(start = 3.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                // Image(
                //     painter = painterResource(R.drawable.ic_danmaku_num),
                //     contentDescription = "弹幕数",
                //     Modifier
                //         .padding(start = 10.dp)
                //         .size(18.dp),
                //     contentScale = ContentScale.Inside,
                //     colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                // )
                Text(
                    text = danmaku,
                    Modifier.padding(start = 3.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolItem(
    imgUrl: String,
    title: String,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .size(96.dp)
            .padding(8.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = imgUrl,
                contentDescription = null,
                Modifier
                    .size(35.dp)
                    .padding(top = 10.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                text = title,
                Modifier
                    .padding(top = 10.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onValueChange: (String) -> Unit,
    clearText: () -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        leadingIcon = {
            // Image(
            //     painter = painterResource(R.drawable.ic_tool_search),
            //     contentDescription = "search"
            // )
        },
        trailingIcon = {
            if (query.isNotBlank()) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier.clickable(onClick = clearText)
                )
            }
        },
        // placeholder = { Text(text = stringResource(R.string.app_fragment_tool_input_tip)) },
        singleLine = true,
    )
}

@Preview(showBackground = true, name = "搜索框")
@Composable
fun PreviewSearchBar() {
    SearchTextField("bv1234567899", {}, {})
}
