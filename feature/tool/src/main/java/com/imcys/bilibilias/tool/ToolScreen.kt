package com.imcys.bilibilias.tool

import androidx.compose.animation.AnimatedVisibility
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
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolScreen(
    state: ToolState,
    clearInput: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    onNavigateToSettings: () -> Unit,
    updateInput: (String) -> Unit,
    inputText: String,
    onNavigateToBangumiFollow: () -> Unit
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
                            .clickable { onNavigateToSettings() },
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
            ToolScreenSearchTextField(
                inputText,
                onValueChange = updateInput,
                clearText = clearInput,
                isError = state.inputError
            )
            AnimatedVisibility(visible = state.isShowVideoCard) {
                if (state.videoType is VideoType.EP) {
                    // VideoCard(
                    //     pic = state.bangumi.cover,
                    //     title = state.bangumi.title,
                    //     desc = state.bangumi.evaluate,
                    //     view = state.bangumi.stat.views.digitalConversion(),
                    //     danmaku = state.bangumi.stat.danmakus.digitalConversion(),
                    //     onNavigateToPlayer = onNavigateToPlayer,
                    //     modifier = Modifier.animateContentSize()
                    // )
                } else {
                    VideoCard(
                        pic = state.pic,
                        title = state.title,
                        desc = state.desc,
                        view = state.view,
                        danmaku = state.danmaku,
                        onNavigateToPlayer = onNavigateToPlayer,
                        modifier = Modifier.animateContentSize()
                    )
                }
            }
            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxWidth()) {
                item("追番信息导出") {
                    ToolItem(
                        imgUrl = "https://s1.ax1x.com/2023/02/05/pS6IsAJ.png",
                        title = "追番信息导出",
                        containerColor = Color(android.graphics.Color.parseColor("#fb7299")),
                        onClick = onNavigateToBangumiFollow
                    )
                }
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
fun VideoCard(
    pic: String,
    title: String,
    desc: String,
    view: String,
    danmaku: String,
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onNavigateToPlayer,
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
private fun ToolScreenSearchTextField(
    query: String,
    onValueChange: (String) -> Unit,
    clearText: () -> Unit,
    isError: Boolean
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
        supportingText = {
            if (isError) {
                // Text(stringResource(R.string.app_ToolFragment_asVideoId2))
            }
        },
        isError = isError,
        // placeholder = { Text(text = stringResource(R.string.app_fragment_tool_input_tip)) },
        singleLine = true,
    )
}

@Preview(showBackground = true, name = "搜索框")
@Composable
fun PreviewSearchBar() {
    ToolScreenSearchTextField("bv1234567899", {}, {}, false)
}

@Preview(showBackground = true, name = "搜索结果")
@Composable
fun PreviewVideoCard() {
    VideoCard(
        pic = "",
        title = "这是一个标题",
        desc = "这是视频简介",
        view = "播放量",
        danmaku = "弹幕数",
        onNavigateToPlayer = {},
        modifier = Modifier
    )
}
