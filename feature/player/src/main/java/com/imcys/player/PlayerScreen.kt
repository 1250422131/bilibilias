package com.imcys.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.imcys.common.utils.noRippleClickable
import com.imcys.model.video.PageData
import com.imcys.player.sheet.SheetDirectLink
import com.imcys.player.sheet.SheetPages
import com.imcys.player.state.PlayInfoUiState
import com.imcys.player.state.PlayerUiState
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
internal fun PlayerRoute(
    navigateToDownloadAanmaku: () -> Unit,
    navigateToUserSpace: (Long) -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()
    val videoInfoUiState by viewModel.videoInfoUiState.collectAsStateWithLifecycle()

    PlayerScreen(
        admDownload = viewModel::admDownload,
        idmDownload = viewModel::idmDownload,
        state = state,
        onNavigateToDownloadAanmaku = navigateToDownloadAanmaku,
        navigateToUserSpace = navigateToUserSpace,
        selectedQuality = viewModel::selectedQuality,
        selectedPage = viewModel::selectedPage,
        addToDownloadQueue = viewModel::addToDownloadQueue,
        playerInfoUiState = videoInfoUiState,
        playerUiState = playerUiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerScreen(
    admDownload: (String) -> Unit,
    idmDownload: (String) -> Unit,
    state: PlayerState,
    onNavigateToDownloadAanmaku: () -> Unit,
    navigateToUserSpace: (Long) -> Unit,
    selectedQuality: (Int) -> Unit,
    selectedPage: (String, Long) -> Unit,
    addToDownloadQueue: (List<PageData>, Int) -> Unit,
    playerInfoUiState: PlayInfoUiState = PlayInfoUiState.Loading,
    playerUiState: PlayerUiState = PlayerUiState.Loading
) {
    var action by remember { mutableStateOf<Action?>(null) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(
        SheetValue.Hidden,
        confirmValueChange = {
            if (it == SheetValue.Hidden) {
                action = null
            }
            true
        },
        skipHiddenState = false
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    BottomSheetScaffold(
        topBar = {
            VideoWindows(
                video = state.video,
                audio = state.audio,
                title = state.title,
                pic = state.pic,
            )
        },
        sheetContent = {
            if (playerUiState is PlayerUiState.Success) {
                when (action) {
                    Action.DOWNLOAD_VIDEO -> SheetPages(
                        qualityDescriptionList = playerUiState.qualityDescription,
                        addToDownloadQueue = addToDownloadQueue,
                        pageData = playerUiState.pageData,
                        admDownload = admDownload,
                        idmDownload = idmDownload
                    )

                    Action.DOWNLOAD_SUBTITLES -> TODO()
                    Action.DIRECT_LINK -> SheetDirectLink(
                        video = playerUiState.video,
                        audio = playerUiState.audio,
                        dolby = playerUiState.dolby,
                    )

                    null -> Unit
                }
            } else {
                Unit
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState, Modifier.navigationBarsPadding())
        },
        sheetPeekHeight = if (action == null) 0.dp else 300.dp,
        scaffoldState = bottomSheetScaffoldState,
        sheetDragHandle = null,
        sheetShape = RectangleShape,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (playerInfoUiState) {
                PlayInfoUiState.LoadFailed,
                PlayInfoUiState.Loading -> Unit

                is PlayInfoUiState.Success -> {
                    UpInfoContainer(
                        navigateToUserSpace = navigateToUserSpace,
                        ownerName = playerInfoUiState.owner.name,
                        ownerAvatar = playerInfoUiState.owner.face,
                        ownerId = playerInfoUiState.owner.mid,
                        modifier = Modifier
                    )
                    VideoDesc(
                        title = playerInfoUiState.title,
                        desc = playerInfoUiState.desc,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    ToolBar(
                        like = playerInfoUiState.toolBarReport.like,
                        coin = playerInfoUiState.toolBarReport.coin,
                        favorite = playerInfoUiState.toolBarReport.favorite,
                        share = playerInfoUiState.toolBarReport.share,
                        isLike = playerInfoUiState.toolBarReport.isLike,
                        isCoins = playerInfoUiState.toolBarReport.isCoin,
                        isFavoured = playerInfoUiState.toolBarReport.isFavoured,
                        modifier = Modifier,
                    )
                }
            }

            LazyRow(Modifier.fillMaxWidth()) {
                items(state.descriptionAndQuality) { pair ->
                    // todo 缺少个边框 bro
                    TextButton(onClick = { selectedQuality(pair.second) }) {
                        Text(text = pair.first)
                    }
                }
            }
            Box(Modifier.fillMaxWidth()) {
                val w = LocalConfiguration.current.screenWidthDp.dp / 3
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.pageData, key = { it.cid }) { item ->
                        Card(
                            onClick = { selectedPage(item.cid.toString(), state.aid) },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                item.part,
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(w)
                                    .height(60.dp)
                                    .padding(4.dp),
                                color = if (item.cid.toString() == state.cid) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                maxLines = 2,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                IconButton(onClick = { /*TODO*/ }, Modifier.align(Alignment.CenterEnd)) {
                    // Icon(
                    //     painter = painterResource(id = R.drawable.chevron_right),
                    //     contentDescription = "打开选集"
                    // )
                }
            }
            Row {
                Button(
                    onClick = {
                        action = Action.DOWNLOAD_VIDEO
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "缓存视频")
                }
                Button(
                    onClick = {
                        onNavigateToDownloadAanmaku()
                    },
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "缓存字幕")
                }
                Button(
                    onClick = {
                        action = Action.DIRECT_LINK
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "直链解析")
                }
            }
        }
    }
}

@Composable
fun UpInfoContainer(
    ownerName: String,
    ownerAvatar: String,
    ownerId: Long,
    navigateToUserSpace: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateToUserSpace(ownerId) },
        leadingContent = {
            Avatar(
                ownerAvatar,
                modifier = Modifier
                    .size(50.dp)
                    .padding(4.dp)
            )
        },
        headlineContent = {
            Text(
                text = ownerName,
                modifier = Modifier,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Composable
fun Avatar(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = "头像",
        modifier = modifier
            .clip(CircleShape),
    )
}

@Composable
fun VerticalChunk(
    top: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        top()
        bottom()
    }
}

@Composable
private fun VideoDesc(title: String, desc: String, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(modifier) {
        Row(
            Modifier
                .noRippleClickable { expanded = !expanded }
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .weight(10f)
                    .animateContentSize(animationSpec = tween(100)),
                overflow = TextOverflow.Ellipsis,
                softWrap = expanded,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                onTextLayout = { it.size.height },
            )
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.weight(1f),
            )
        }
        AnimatedVisibility(visible = expanded) {
            Text(
                desc,
                fontSize = 11.sp,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

/**
 * @param title 视频标题
 * @param pic 封面
 */
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private fun VideoWindows(
    title: String,
    pic: String,
    video: com.imcys.model.Dash.Video,
    audio: com.imcys.model.Dash.Audio,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context -> AsGSYVideoPlayer(context) },
        modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { gsy ->
            gsy.setMediaSource(video, audio)
            gsy.setUp(video.baseUrl, title, pic)
            when (gsy.currentState) {
                StandardGSYVideoPlayer.CURRENT_STATE_NORMAL -> {
                    Timber.tag(
                        "playerState"
                    )
                        .d("正常=${gsy.gsyVideoManager.lastState},${gsy.progress},${gsy.currentPosition}")
                }

                StandardGSYVideoPlayer.CURRENT_STATE_PREPAREING -> {
                    Timber.tag("playerState").d("准备播放")
                }

                StandardGSYVideoPlayer.CURRENT_STATE_PLAYING -> {
                    Timber.tag("playerState").d("播放")
                }

                StandardGSYVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START -> {
                    Timber.tag("playerState").d("缓冲")
                }

                StandardGSYVideoPlayer.CURRENT_STATE_PAUSE -> {
                    Timber.tag("playerState").d("暂停=${gsy.progress},${gsy.currentPosition}")
                }

                StandardGSYVideoPlayer.CURRENT_STATE_AUTO_COMPLETE -> {}
                StandardGSYVideoPlayer.CURRENT_STATE_ERROR -> {
                    Timber.tag("playerState").d("错误")
                }

                else -> {
                    Timber.tag("playerState").d("unknow")
                }
            }
            // 设置返回按键功能
            // gsy.backButton.setOnClickListener { }
        },
        onRelease = { gsy ->
            GSYVideoManager.releaseAllVideos()
        },
        onReset = { gsy ->
            // GSYVideoManager.releaseAllVideos()
        },
    )
}
