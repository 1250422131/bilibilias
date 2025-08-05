package com.imcys.bilibilias.ui.analysis.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ReportGmailerrorred
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.ui.weight.tip.AsErrorTip
import com.imcys.bilibilias.ui.weight.tip.AsWarringTip
import com.imcys.bilibilias.weight.AsAutoError
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VideoDownloadScreen(
    downloadInfo: DownloadViewInfo?,
    videoPlayerInfo: NetWorkResult<BILIVideoPlayerInfo?>,
    currentBvId: String,
    viewInfo: NetWorkResult<BILIVideoViewInfo?>,
    onUpdateSelectedCid: (Long?) -> Unit,
    onVideoQualityChange: (Long?) -> Unit = {},
    onVideoCodeChange: (String) -> Unit = {},
    onAudioQualityChange: (Long?) -> Unit = {}
) {

    if (viewInfo.data?.isUpowerExclusive == true && viewInfo.data?.isUpowerPlay == false) {
        // 拦截缓存，暂不支持
        return
    }

    var selectSectionId by remember { mutableStateOf<Long?>(null) }
    var currentSectionPageListIndex by remember { mutableIntStateOf(0) }

    var currentVideoPageListIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(currentBvId, viewInfo.data?.ugcSeason?.sections) {
        selectSectionId = viewInfo.data?.ugcSeason?.sections?.let lastRun@{ data ->
            data.forEach { section ->
                section.episodes.forEach { ep ->
                    if (ep.bvid == currentBvId) {
                        return@lastRun section.id
                    }
                }
            }
            data.firstOrNull()?.id
        }
    }

    SurfaceColorCard {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsErrorTip {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "警告",
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        "未经作者允许禁止转载",
                        fontSize = 14.sp,
                    )
                }
            }
            Text("缓存倾向")
            AsAutoError(videoPlayerInfo, onSuccessContent = {
                Column {
                    VideoSupportFormatsSelectScreen(
                        Modifier
                            .fillMaxWidth()
                            .shimmer(videoPlayerInfo.status != ApiStatus.SUCCESS),
                        downloadInfo,
                        videoPlayerInfo.data?.supportFormats,
                        videoPlayerInfo.data?.dash?.video,
                        videoPlayerInfo.data?.durls,
                        onVideoQualityChange = onVideoQualityChange,
                        onVideoCodeChange = onVideoCodeChange
                    )

                    Spacer(Modifier.height(6.dp))
                    AudioQualitySelectScreen(
                        Modifier.fillMaxWidth(),
                        downloadInfo,
                        videoPlayerInfo.data?.dash?.audio,
                        onAudioQualityChange = onAudioQualityChange
                    )
                }
            })

            if (viewInfo.data?.ugcSeason?.sections?.isNotEmpty() == true) {
                AsAutoError(viewInfo, onSuccessContent = {
                    Column(
                        Modifier
                            .animateContentSize()
                            .shimmer(viewInfo.status != ApiStatus.SUCCESS)
                    ) {
                        Text("选择缓存合集")
                        Column {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                items(
                                    viewInfo.data?.ugcSeason?.sections ?: emptyList(),
                                    key = { it.id }
                                ) { info ->
                                    ToggleButton(
                                        checked = info.id == selectSectionId,
                                        onCheckedChange = {
                                            if (it) {
                                                currentSectionPageListIndex = 0
                                                selectSectionId = info.id
                                            }
                                        },
                                    ) {
                                        Text(info.title)
                                    }
                                }
                            }

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                val episodeCount = viewInfo.data?.ugcSeason?.sections?.firstOrNull {
                                    it.id == selectSectionId
                                }?.episodes?.size ?: 0
                                val pageCount = ceil(episodeCount / 12.0).toInt()
                                items(
                                    pageCount,
                                    key = { "ep_$it" }
                                ) { index ->
                                    val startEp = index * 12 + 1
                                    val endEp = minOf((index + 1) * 12, episodeCount)
                                    FilterChip(
                                        onClick = {
                                            currentSectionPageListIndex = index
                                        },
                                        label = {
                                            Text("$startEp~$endEp")
                                        },
                                        selected = index == currentSectionPageListIndex,
                                    )
                                }
                            }
                        }
                        LazyVerticalGrid(
                            GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.sizeIn(maxHeight = (60 * 2 + 2 * 10).dp)
                        ) {
                            val episodeList = viewInfo.data?.ugcSeason?.sections?.firstOrNull {
                                it.id == selectSectionId
                            }?.episodes ?: emptyList()
                            val startIndex = currentSectionPageListIndex * 12
                            val endIndex =
                                minOf((currentSectionPageListIndex + 1) * 12, episodeList.size)
                            items(
                                episodeList.subList(startIndex, endIndex)
                            ) {
                                FilterChip(
                                    selected = downloadInfo?.selectedCid?.contains(it.cid) == true,
                                    onClick = {
                                        onUpdateSelectedCid.invoke(it.cid)
                                    },
                                    label = {
                                        Column(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(60.dp)
                                                .padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                it.title,
                                                maxLines = 2,
                                                fontSize = 14.sp,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                })
            } else {
                AsAutoError(viewInfo, onSuccessContent = {
                    Column(
                        Modifier
                            .animateContentSize()
                            .shimmer(viewInfo.status != ApiStatus.SUCCESS)
                    ) {
                        Text("选择缓存子集")
                        Column {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                val episodeCount = viewInfo.data?.pages?.size ?: 0
                                val pageCount = ceil(episodeCount / 12.0).toInt()
                                items(
                                    pageCount,
                                    key = { "p_$it" }
                                ) { index ->
                                    val startEp = index * 12 + 1
                                    val endEp = minOf((index + 1) * 12, episodeCount)
                                    FilterChip(
                                        onClick = {
                                            currentVideoPageListIndex = index
                                        },
                                        label = {
                                            Text("$startEp~$endEp")
                                        },
                                        selected = index == currentVideoPageListIndex,
                                    )
                                }
                            }
                        }
                        LazyVerticalGrid(
                            GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.sizeIn(maxHeight = (60 * 2 + 2 * 10).dp)
                        ) {
                            val episodeList = viewInfo.data?.pages ?: emptyList()
                            val startIndex = currentVideoPageListIndex * 12
                            val endIndex =
                                minOf((currentVideoPageListIndex + 1) * 12, episodeList.size)
                            items(
                                episodeList.subList(startIndex, endIndex)
                            ) {
                                FilterChip(
                                    selected = downloadInfo?.selectedCid?.contains(it.cid) == true,
                                    onClick = {
                                        onUpdateSelectedCid.invoke(it.cid)
                                    },
                                    label = {
                                        Column(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(60.dp)
                                                .padding(8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                it.part,
                                                maxLines = 2,
                                                fontSize = 14.sp,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                })
            }
        }
    }
}