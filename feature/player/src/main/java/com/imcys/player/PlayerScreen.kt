package com.imcys.player

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.*
import androidx.hilt.navigation.compose.*
import androidx.lifecycle.compose.*
import coil.compose.*
import com.imcys.designsystem.component.*
import com.imcys.player.sheet.*
import com.imcys.player.state.*
import com.shuyu.gsyvideoplayer.*
import com.shuyu.gsyvideoplayer.video.*
import kotlinx.coroutines.*
import timber.log.*

@Composable
internal fun PlayerRoute(
    navigateToUserSpace: (Long) -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val playerUiState by viewModel.playerUiState.collectAsStateWithLifecycle()
    val videoInfoUiState by viewModel.videoInfoUiState.collectAsStateWithLifecycle()

    PlayerScreen(
        navigateToUserSpace = navigateToUserSpace,
        addToDownloadQueue = viewModel::addToDownloadQueue,
        playerInfoUiState = videoInfoUiState,
        playerUiState = playerUiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerScreen(
    navigateToUserSpace: (Long) -> Unit,
    addToDownloadQueue: (List<String>, Int) -> Unit,
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
        sheetContent = {
            if (playerUiState is PlayerUiState.Success) {
                when (action) {
                    Action.DOWNLOAD_VIDEO -> SheetSeries(
                        descriptionWithQuality = playerUiState.descriptionWithQuality,
                        addToDownloadQueue = addToDownloadQueue,
                        playerInfoUiState,
                        playerUiState.defaultQuality
                    )

                    Action.DOWNLOAD_SUBTITLES -> TODO()
                    Action.DIRECT_LINK -> SheetDirectLink(
                        video = playerUiState.video,
                        audio = playerUiState.audio,
                    )

                    null -> {}
                }
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

            Box(Modifier.fillMaxWidth()) {
                val w = LocalConfiguration.current.screenWidthDp.dp / 3
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    items(state.pageData, key = { it.cid }) { item ->
//                        Card(
//                            onClick = { },
//                            shape = RoundedCornerShape(4.dp)
//                        ) {
//                            Text(
//                                item.part,
//                                Modifier
//                                    .align(Alignment.CenterHorizontally)
//                                    .width(w)
//                                    .height(60.dp)
//                                    .padding(4.dp),
//                                color = if (item.cid.toString() == state.cid) MaterialTheme.colorScheme.primary else Color.Unspecified,
//                                maxLines = 2,
//                                fontSize = 13.sp
//                            )
//                        }
//                    }
                }
                IconButton(onClick = { /*TODO*/ }, Modifier.align(Alignment.CenterEnd)) {
                    // Icon(
                    //     painter = painterResource(id = R.drawable.chevron_right),
                    //     contentDescription = "打开选集"
                    // )
                }
            }
            Row {
                AsButton(
                    onClick = {
                        action = Action.DOWNLOAD_VIDEO
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    Modifier.weight(1f),
                ) {
                    Text(text = "缓存视频")
                }
                AsButton(
                    onClick = {},
                    Modifier.weight(1f),
                ) {
                    Text(text = "缓存字幕")
                }
                AsButton(
                    onClick = {
                        action = Action.DIRECT_LINK
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    Modifier.weight(1f),
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
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    expanded = !expanded
                }
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
