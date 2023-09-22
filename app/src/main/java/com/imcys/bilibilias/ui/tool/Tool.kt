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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.startActivity
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.utils.AsVideoNumUtils
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import com.imcys.bilibilias.home.ui.activity.tool.WebAsActivity
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tool() {
    val toolViewModel: ToolViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        toolViewModel.getOldItemList()
    }
    val context = LocalContext.current
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(R.string.app_fragment_tool_title),
                    Modifier.padding(20.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }, actions = {
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
            val state by toolViewModel.toolState.collectAsStateWithLifecycle()
            ReadFromClipboard()
            SearchTextField(
                state.query,
                onValueChange = {
                    toolViewModel.parsesBvOrAvOrEp(it)
                },
                clearText = { toolViewModel.clearText() },
                isError = state.isInputError
            )
            AnimatedVisibility(visible = state.isShowVideoCard) {
                VideoCard(
                    state.videoMate.videoId,
                    state.videoMate.pic,
                    state.videoMate.title,
                    state.videoMate.desc,
                    state.videoMate.playVolume,
                    state.videoMate.danmaku,
                    Modifier.animateContentSize()
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

@Composable
private fun ReadFromClipboard(toolViewModel: ToolViewModel = hiltViewModel()) {
    val clipboardManager = LocalClipboardManager.current
    LaunchedEffect(clipboardManager.getText()?.text) {
        val text = clipboardManager.getText()?.text ?: return@LaunchedEffect
        if (AsVideoNumUtils.isBVStart(text) || AsVideoNumUtils.isBVHttp(text) || AsVideoNumUtils.isAV(text)) {
            toolViewModel.clearText()
            toolViewModel.parsesBvOrAvOrEp(text)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCard(
    videoId: String,
    pic: String,
    title: String,
    desc: String,
    playVolume: String,
    danmaku: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        onClick = { context.startActivity(AsVideoActivity::class.java, bundleOf(BVID to videoId)) },
        modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = pic,
                contentDescription = "视频封面",
                contentScale = ContentScale.FillWidth,
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
                Image(
                    painter = painterResource(R.drawable.ic_play_num),
                    contentDescription = "播放量",
                    Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
                Text(
                    text = playVolume,
                    Modifier.padding(start = 3.dp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Image(
                    painter = painterResource(R.drawable.ic_danmaku_num),
                    contentDescription = "弹幕量",
                    Modifier
                        .size(18.dp)
                        .padding(start = 10.dp),
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
private fun SearchTextField(query: String, onValueChange: (String) -> Unit, clearText: () -> Unit, isError: Boolean) {
    val focusRequester = remember { FocusRequester() } // 焦点
    val softKeyboard = LocalSoftwareKeyboardController.current // 软键盘
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
    SearchTextField("", {}, {}, false)
}

@Preview(showBackground = true, name = "搜索结果")
@Composable
fun PreviewVideoCard() {
    VideoCard(pic = "", title = "", desc = "", playVolume = "", danmaku = "", videoId = "")
}
