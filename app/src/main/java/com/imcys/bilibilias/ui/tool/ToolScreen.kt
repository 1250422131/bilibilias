package com.imcys.bilibilias.ui.tool

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.startActivity
import com.imcys.bilibilias.common.base.extend.digitalConversion
import com.imcys.bilibilias.common.base.utils.AsVideoUtils
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import com.imcys.bilibilias.home.ui.activity.tool.WebAsActivity
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolScreen(
    state: ToolState,
    parsesBvOrAvOrEp: (String) -> Unit,
    clearSearchText: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    onBack: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    LaunchedEffect(clipboardManager.getText()?.text) {
        val text = clipboardManager.getText()?.text ?: return@LaunchedEffect
        if (AsVideoUtils.isResolvable(text)) {
            clearSearchText()
            parsesBvOrAvOrEp(text)
        }
    }
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(R.string.app_fragment_tool_title),
                    Modifier
                        .size(24.dp)
                        .padding(20.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }, actions = {
                val context = LocalContext.current
                AsyncImage(
                    model = "https://s1.ax1x.com/2023/02/04/pSyHEy6.png",
                    contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .clickable {
                            context.startActivity(WebAsActivity::class.java)
                        },
                    colorFilter = ColorFilter.tint(Color(android.graphics.Color.parseColor("#fb7299")))
                )
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "设置",
                    Modifier
                        .padding(16.dp)
                        .clickable {
                            context.startActivity(SettingActivity::class.java)
                        },
                    tint = Color(android.graphics.Color.parseColor("#fb7299"))
                )
            })
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ToolScreenSearchTextField(
                state.text,
                onValueChange = {
                    parsesBvOrAvOrEp(it)
                },
                clearText = clearSearchText,
                isError = state.inputError
            )
            AnimatedVisibility(visible = state.isShowVideoCard) {
                VideoCard(
                    bvid = state.videoDetails.bvid,
                    pic = state.videoDetails.pic,
                    title = state.videoDetails.title,
                    desc = state.videoDetails.desc,
                    view = state.videoDetails.stat.view.digitalConversion(),
                    danmaku = state.videoDetails.stat.danmaku.digitalConversion(),
                    duration = state.videoDetails.duration,
                    onNavigateToPlayer = onNavigateToPlayer,
                    modifier = Modifier.animateContentSize()
                )
            }

            LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxWidth()) {
                item("日志导出") {
                    ToolItem(
                        imgUrl = "https://s1.ax1x.com/2023/02/05/pS6IsAJ.png",
                        title = "日志导出",
                        containerColor = Color(android.graphics.Color.parseColor("#fb7299")),
                        modifier = Modifier.clickable {
                        }
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
    bvid: String,
    pic: String,
    title: String,
    desc: String,
    view: String,
    danmaku: String,
    duration: Int,
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
            val time = remember(
                duration
            ) { String.format(Locale.SIMPLIFIED_CHINESE, "%d:%02d", duration / 60, duration % 60) }
            val textMeasurer = rememberTextMeasurer()
            val measure = textMeasurer.measure(time)

            AsyncImage(
                model = pic,
                contentDescription = "视频封面",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .drawWithContent {
                        drawContent()
                        val topLeft = Offset(
                            10.dp
                                .roundToPx()
                                .toFloat(),
                            160.dp
                                .roundToPx()
                                .toFloat()
                        )
                        val textWidth = measure.size.width
                        val textHeight = measure.size.height
                        // todo 颜色记得更换为半透明的
                        drawRect(
                            Color.Red,
                            topLeft = topLeft - Offset(
                                1.dp
                                    .roundToPx()
                                    .toFloat(),
                                2.dp
                                    .roundToPx()
                                    .toFloat()
                            ),
                            size = Size(
                                (textWidth + 3.dp
                                    .roundToPx()
                                    .toFloat()),
                                (textHeight + 2.dp
                                    .roundToPx()
                                    .toFloat())
                            )
                        )
                        drawText(measure, topLeft = topLeft)
                    }
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
                Image(
                    painter = painterResource(R.drawable.ic_play_num),
                    contentDescription = "播放数",
                    Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
                Text(
                    text = view,
                    Modifier.padding(start = 3.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Image(
                    painter = painterResource(R.drawable.ic_danmaku_num),
                    contentDescription = "弹幕数",
                    Modifier
                        .padding(start = 10.dp)
                        .size(18.dp),
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
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
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { /*todo*/ },
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ToolScreenSearchTextField(
    query: String,
    onValueChange: (String) -> Unit,
    clearText: () -> Unit,
    isError: Boolean
) {
    // 焦点
    val focusRequester = remember { FocusRequester() }
    // 软键盘
    val softKeyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        softKeyboard?.show()
    }
    OutlinedTextField(
        value = query,
        onValueChange = onValueChange,
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .padding(8.dp),
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.ic_tool_search),
                contentDescription = "search"
            )
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
                Text(stringResource(R.string.app_ToolFragment_asVideoId2))
            }
        },
        isError = isError,
        placeholder = { Text(text = stringResource(R.string.app_fragment_tool_input_tip)) },
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
        bvid = "",
        pic = "",
        title = "这是一个标题",
        desc = "这是视频简介",
        view = "播放量",
        danmaku = "弹幕数",
        duration = 123456,
        {}
    )
}
