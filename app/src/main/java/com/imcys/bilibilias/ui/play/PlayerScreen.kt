package com.imcys.bilibilias.ui.play

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.noRippleClickable
import com.imcys.bilibilias.common.base.components.CenterRow
import com.imcys.bilibilias.common.base.extend.digitalConversion
import com.imcys.bilibilias.common.base.model.video.Dash
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import timber.log.Timber

private val tag = Timber.tag("PlayerScreen")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    state: PlayerState,
    onNavigateToDownloadOption: () -> Unit,
    onNavigateToDownloadAanmaku: () -> Unit,
    selectedQuality: (Int) -> Unit,
    selectedPage: (Long, Long) -> Unit,
) {
    Scaffold(Modifier.fillMaxSize(), topBar = {
        VideoWindows(
            video = state.video,
            audio = state.audio,
            title = state.title,
            pic = state.pic,
        )
    }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 视频简介
            VideoIntroduction(
                title = state.title,
                desc = state.desc,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            VideoActions(
                like = state.like,
                coin = state.coins,
                favorite = state.favorite,
                share = state.share,
                isLike = state.hasLike,
                isCoins = state.hasCoins,
                isCollection = state.hasCollection,
                modifier = Modifier,
            )
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
                    items(state.pages, key = { it.cid }) { item ->
                        Card(
                            onClick = { selectedPage(item.cid, state.aid) },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                item.part,
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(w)
                                    .height(60.dp)
                                    .padding(4.dp),
                                color = if (item.cid == state.cid) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                maxLines = 2,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                IconButton(onClick = { /*TODO*/ }, Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        painter = painterResource(id = com.imcys.bilibilias.R.drawable.chevron_right),
                        contentDescription = "打开选集"
                    )
                }
            }
            Row {
                Button(
                    onClick = { onNavigateToDownloadOption() },
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
                        onNavigateToDownloadAanmaku()
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
private fun VideoActions(
    like: Int,
    coin: Int,
    favorite: Int,
    share: Int,
    isLike: Boolean,
    isCoins: Boolean,
    isCollection: Boolean,
    modifier: Modifier = Modifier
) {
    CenterRow(
        modifier
            .fillMaxWidth()
    ) {
        ListItem(
            headlineContent = {
                Image(
                    painter = painterResource(R.drawable.ic_as_video_like),
                    contentDescription = "点赞按钮",
                    Modifier
                        .padding(8.dp)
                        .size(22.dp),
                    colorFilter = if (isLike) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null
                )
            },
            supportingContent = {
                Text(like.digitalConversion(), fontWeight = FontWeight.ExtraLight, fontSize = 11.sp)
            },
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
        ListItem(
            headlineContent = {
                Image(
                    painter = painterResource(R.drawable.ic_as_video_throw),
                    contentDescription = "投币按钮",
                    Modifier
                        .padding(8.dp)
                        .size(22.dp),
                    colorFilter = if (isCoins) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null
                )
            },
            supportingContent = {
                Text(coin.digitalConversion(), fontWeight = FontWeight.ExtraLight, fontSize = 11.sp)
            },
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
        ListItem(
            headlineContent = {
                Image(
                    painter = painterResource(R.drawable.ic_as_video_collec),
                    contentDescription = "收藏按钮",
                    Modifier
                        .padding(8.dp)
                        .size(22.dp),
                    colorFilter = if (isCollection) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null
                )
            },
            supportingContent = {
                Text(favorite.digitalConversion(), fontWeight = FontWeight.ExtraLight, fontSize = 11.sp)
            },
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
        ListItem(
            headlineContent = {
                Image(
                    painter = painterResource(R.drawable.ic_as_video_fasong),
                    contentDescription = "分享按钮",
                    Modifier
                        .padding(8.dp)
                        .size(22.dp),
                )
            },
            supportingContent = {
                Text(share.digitalConversion(), fontWeight = FontWeight.ExtraLight, fontSize = 11.sp)
            },
            modifier = Modifier
                .clickable { }
                .weight(1f)
        )
    }
}

@Composable
private fun VideoIntroduction(title: String, desc: String, modifier: Modifier = Modifier) {
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
                fontSize = 12.sp,
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
    video: Dash.Video,
    audio: Dash.Audio,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context -> AsGSYVideoPlayer(context) },
        modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { gsy ->
            val tree = Timber.tag("videoWindows")

            // getFormat(gsy, tree)
            getFormat2(gsy)
            // MimeTypes.VIDEO_DOLBY_VISION
            // MediaCodecVideoRenderer.getDecoderInfos()

            gsy.setMediaSource(video, audio)
            gsy.setUp(video.baseUrl, title, pic)
            when (gsy.currentState) {
                StandardGSYVideoPlayer.CURRENT_STATE_NORMAL -> {
                    Timber.tag(
                        "playerState"
                    ).d("正常=${gsy.gsyVideoManager.lastState},${gsy.progress},${gsy.currentPosition}")
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

@Composable
fun VideoController(modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewVideoController() {
    VideoController()
}
