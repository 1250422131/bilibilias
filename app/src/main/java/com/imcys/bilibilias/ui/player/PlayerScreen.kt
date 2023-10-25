package com.imcys.bilibilias.ui.player

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.imcys.bilibilias.base.utils.noRippleClickable
import com.imcys.bilibilias.common.base.components.CenterRow
import com.imcys.bilibilias.common.base.components.VerticalTwoTerms
import com.imcys.bilibilias.common.base.extend.digitalConversion
import com.imcys.bilibilias.common.base.model.video.Dash
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import okhttp3.OkHttpClient
import timber.log.Timber

private val tag = Timber.tag("PlayerScreen")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    state: PlayerState,
    onNavigateToDownloadOption: () -> Unit,
    changeUrl: (Long) -> Unit,
    onNavigateToDownloadAanmaku: () -> Unit,
    selectedQuality: (Int) -> Unit,
) {
    Scaffold(Modifier.fillMaxSize(), topBar = {
        VideoWindows(
            video = state.video,
            audio = state.audio,
            title = state.videoDetails.title,
            pic = state.videoDetails.pic,
            http = state.okHttpClient
        )
    }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 视频简介
            VideoIntroduction(
                title = state.videoDetails.title,
                desc = state.videoDetails.descV2?.firstOrNull()?.rawText ?: state.videoDetails.desc,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            VideoActions(
                like = state.videoDetails.stat.like,
                coin = state.videoDetails.stat.coin,
                favorite = state.videoDetails.stat.favorite,
                share = state.videoDetails.stat.share,
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
                var selected by remember { mutableLongStateOf(state.videoDetails.pages.firstOrNull()?.cid ?: 0) }
                val w = LocalConfiguration.current.screenWidthDp.dp / 3
                LazyRow(
                    Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.videoDetails.pages, key = { it.cid }) { item ->
                        Card(
                            onClick = {
                                changeUrl(item.cid)
                                selected = item.cid
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                item.part,
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(w)
                                    .padding(4.dp),
                                color = if (item.cid == selected) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                maxLines = 2,
                                fontSize = 13.sp
                            )
                        }
                    }
                    // item {
                    //     Surface(onClick = { /*TODO*/ }, Modifier) {
                    //         Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = "打开选集")
                    //     }
                    // }
                }
                IconButton(onClick = { /*TODO*/ }, Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        painter = painterResource(id = com.imcys.bilibilias.R.drawable.chevron_right),
                        contentDescription = null
                    )
                }
            }
            Row {
                Button(
                    onClick = {
                        onNavigateToDownloadOption()
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
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        VerticalTwoTerms(
            top = {
                Image(
                    painterResource(com.imcys.bilibilias.R.drawable.ic_as_video_like),
                    contentDescription = "点赞按钮",
                    Modifier.size(22.dp),
                    colorFilter = if (isLike) {
                        ColorFilter.tint(
                            MaterialTheme.colorScheme.primary
                        )
                    } else {
                        null
                    }
                )
            },
            bottom = { Text(like.digitalConversion(), fontWeight = FontWeight.ExtraLight, fontSize = 11.sp) },
            Modifier
                .clickable { }
                .padding(16.dp)
                .weight(1f)
        )
        VerticalTwoTerms(
            top = {
                Image(
                    painterResource(com.imcys.bilibilias.R.drawable.ic_as_video_throw),
                    contentDescription = "投币按钮",
                    Modifier.size(22.dp),
                    colorFilter = if (isCoins) {
                        ColorFilter.tint(
                            MaterialTheme.colorScheme.primary
                        )
                    } else {
                        null
                    }
                )
            },
            bottom = { Text(coin.digitalConversion(), fontWeight = FontWeight.Thin, fontSize = 11.sp) },
            Modifier
                .clickable { }
                .padding(16.dp)
                .weight(1f)

        )
        VerticalTwoTerms(
            top = {
                Image(
                    painterResource(com.imcys.bilibilias.R.drawable.ic_as_video_collec),
                    contentDescription = "收藏按钮",
                    Modifier.size(22.dp),
                    colorFilter = if (isCollection) {
                        ColorFilter.tint(
                            MaterialTheme.colorScheme.primary
                        )
                    } else {
                        null
                    }
                )
            },
            bottom = { Text(favorite.digitalConversion(), fontWeight = FontWeight.Thin, fontSize = 11.sp) },
            Modifier
                .clickable { }
                .padding(16.dp)
                .weight(1f)

        )
        VerticalTwoTerms(
            top = {
                Image(
                    painterResource(com.imcys.bilibilias.R.drawable.ic_as_video_fasong),
                    contentDescription = "分享按钮",
                    Modifier.size(22.dp)
                )
            },
            bottom = { Text(share.digitalConversion(), fontWeight = FontWeight.Thin, fontSize = 11.sp) },
            Modifier
                .clickable { }
                .padding(16.dp)
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
 * @param url 播放地址
 * @param title 视频标题
 * @param pic 封面
 */
@Composable
@androidx.annotation.OptIn(
    androidx.media3.common.util.UnstableApi::class,
)
private fun VideoWindows(
    title: String,
    pic: String,
    video: Dash.Video,
    audio: Dash.Audio,
    http: OkHttpClient,
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

            gsy.setMediaSource(http, video, audio)
            gsy.setUp(video.baseUrl, title, pic)

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
