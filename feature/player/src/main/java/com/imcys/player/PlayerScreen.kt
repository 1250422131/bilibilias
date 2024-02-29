package com.imcys.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.imcys.designsystem.component.AsButton
import com.imcys.player.sheet.SheetDirectLink
import com.imcys.player.sheet.SheetSeries
import com.imcys.player.state.PlayInfoUiState
import com.imcys.player.state.PlayerUiState
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import timber.log.Timber

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

@Composable
internal fun PlayerScreen(
    navigateToUserSpace: (Long) -> Unit,
    addToDownloadQueue: (List<String>, Int) -> Unit,
    playerInfoUiState: PlayInfoUiState = PlayInfoUiState.Loading,
    playerUiState: PlayerUiState = PlayerUiState.Loading
) {
    var action by remember { mutableStateOf(Action.INIT) }
//    MultiBottomSheetLayout(mainContent = {}, sheetContent = {})
    if (action != Action.INIT) {
        BottomSheetDialog(
            onDismissRequest = {
                action = Action.INIT
            }
        ) {
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

                    Action.INIT -> Unit
                }
            }
        }
    }

    Scaffold(
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
            Row {
                AsButton(
                    onClick = {
                        action = Action.DOWNLOAD_VIDEO
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
