package com.imcys.bilibilias.ui.player

import android.content.res.Configuration
import android.net.Uri
import android.widget.ImageView
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.datasource.DataSink
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.TransferListener
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import coil.load
import com.imcys.bilibilias.base.utils.getActivity
import com.imcys.bilibilias.base.utils.noRippleClickable
import com.imcys.bilibilias.common.base.components.CenterRow
import com.imcys.bilibilias.common.base.components.VerticalTwoTerms
import com.imcys.bilibilias.common.base.constant.BROWSER_USER_AGENT
import com.imcys.bilibilias.common.base.extend.digitalConversion
import com.imcys.bilibilias.common.base.model.video.Dash
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import io.ktor.http.HttpHeaders
import timber.log.Timber
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener
import tv.danmaku.ijk.media.exo2.ExoSourceManager
import tv.danmaku.ijk.media.exo2.ExoSourceManager.inferContentType
import java.io.File

private val tag = Timber.tag("PlayerScreen")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    state: PlayerState,
    onNavigateToDownloadOption: () -> Unit,
    changeUrl: (Long) -> Unit,
    onNavigateToDownloadAanmaku: () -> Unit
) {
    Scaffold(Modifier.fillMaxSize(), topBar = {
        VideoWindows(
            video = state.videos,
            audio = state.audios,
            title = state.videoDetails.title,
            pic = state.videoDetails.pic,
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
            Box {
                var selected by remember { mutableLongStateOf(state.videoDetails.pages.firstOrNull()?.cid ?: 0) }
                val w = LocalConfiguration.current.screenWidthDp.dp / 3
                LazyRow(
                    Modifier
                        .fillMaxWidth(),
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
    modifier: Modifier = Modifier,
    video: List<Dash.Video>,
    audio: List<Dash.Audio>
) {
    // val url = remember(video) {
    //     video.groupBy { it.id }.maxBy { it.key }.value.firstOrNull()?.baseUrl ?: ""
    // }
    AndroidView(
        factory = { context ->
            PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
            StandardGSYVideoPlayer(context)
        },
        modifier
            .fillMaxWidth()
            .height(200.dp),
        update = { gsy ->
            Timber.tag("video").d(video.toString())
            val dataSourceFactory: DataSource.Factory =
                DefaultHttpDataSource.Factory()
            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(fromUri(video.firstOrNull()?.baseUrl ?: ""))
            val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(fromUri(audio.firstOrNull()?.baseUrl ?: ""))

            val mergeSource: MediaSource = MergingMediaSource(videoSource, audioSource)
            ExoSourceManager.setExoMediaSourceInterceptListener(object : ExoMediaSourceInterceptListener {
                override fun getMediaSource(
                    dataSource: String?,
                    preview: Boolean,
                    cacheEnable: Boolean,
                    isLooping: Boolean,
                    cacheDir: File?
                ): MediaSource {
                    val contentUri = Uri.parse(dataSource)
                    val contentType: Int = inferContentType(dataSource, "mpd")
                    // Type =  4
                    // when (contentType) {
                    // HlsMediaSource.Factory()
                    // DashMediaSource.Factory(DataSource.Factory {mergeSource })
                    //     C.CONTENT_TYPE_HLS -> return HlsMediaSource.Factory(CustomSourceTag.getDataSourceFactory(this@GSYApplication.getApplicationContext(), preview))
                    // //         .createMediaSource(contentUri)
                    // }
                    // CONTENT_TYPE_OTHER
                    Timber.tag("source").d("type=$contentType,uri=$contentUri")
                    return mergeSource
                }

                override fun getHttpDataSourceFactory(
                    userAgent: String?,
                    listener: TransferListener?,
                    connectTimeoutMillis: Int,
                    readTimeoutMillis: Int,
                    mapHeadData: MutableMap<String, String>?,
                    allowCrossProtocolRedirects: Boolean
                ): DataSource.Factory? = null

                override fun cacheWriteDataSinkFactory(cachePath: String?, url: String?): DataSink.Factory? = null
            })
            // type=4,uri=http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4
            gsy.titleTextView.text = title
            gsy.isReleaseWhenLossAudio = false
            gsy.setUp(
                video.firstOrNull()?.baseUrl ?: "",
                false,
                null,
                mapOf(HttpHeaders.UserAgent to BROWSER_USER_AGENT),
                null
            )
            val imageView = ImageView(gsy.context)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.load(pic)
            gsy.thumbImageView = imageView
            // 增加title
            gsy.titleTextView.isVisible = true
            // 设置返回键
            gsy.backButton.isVisible = true
            // 设置旋转
            val orientationUtils = OrientationUtils(gsy.context.getActivity(), gsy)
            gsy.fullscreenButton.setOnClickListener { // ------- ！！！如果不需要旋转屏幕，可以不调用！！！-------
                // 不需要屏幕旋转，还需要设置 setNeedOrientationUtils(false)
                orientationUtils.resolveByClick()
            }
            // gsy.overrideExtension = "mpd"

            // 是否可以滑动调整
            gsy.setIsTouchWiget(true)
            // 设置返回按键功能
            // gsy.backButton.setOnClickListener { }
        },
        onRelease = { gsy ->
            GSYVideoManager.releaseAllVideos()
        },
        onReset = { gsy ->
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
