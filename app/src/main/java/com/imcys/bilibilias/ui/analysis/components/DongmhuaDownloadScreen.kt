package com.imcys.bilibilias.ui.analysis.components

import androidx.compose.ui.unit.sp
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.ui.analysis.AnalysisViewModel
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.weight.AsAutoError
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DongmhuaDownloadScreen(
    downloadInfo: DownloadViewInfo?,
    donghuaPlayerInfo: NetWorkResult<BILIDonghuaPlayerInfo?>,
    currentUserInfo: BILIUsersEntity?,
    currentEpId: Long,
    donghuaViewInfo: NetWorkResult<BILIDonghuaSeasonInfo?>,
    onSelectSeason: (Long) -> Unit,
    onUpdateSelectedEpId: (epId: Long?, title: String, cover: String) -> Unit,
    onVideoQualityChange: (Long?) -> Unit = {},
    onVideoCodeChange: (String) -> Unit = {},
    onAudioQualityChange: (Long?) -> Unit = {},
    onSelectSingleModel: (Boolean) -> Unit = { _ -> },
    onToVideoCodingInfo:()-> Unit
) {

    var selectSeasonsId by remember {
        mutableStateOf<Long?>(null)
    }
    var selectSectionId by remember {
        mutableStateOf<Long?>(null)
    }
    var currentEpListIndex by remember { mutableIntStateOf(0) }

    val isVip = currentUserInfo?.isVip() == true
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(donghuaViewInfo.data?.seasonId, donghuaViewInfo.data?.seasons) {
        selectSeasonsId = donghuaViewInfo.data?.seasons
            ?.firstOrNull { it.seasonId == donghuaViewInfo.data?.seasonId }
            ?.seasonId
            ?: donghuaViewInfo.data?.seasons?.firstOrNull()?.seasonId
    }

    LaunchedEffect(currentEpId, donghuaViewInfo.data?.section) {
        selectSectionId = donghuaViewInfo.data?.section?.let lastRun@{ data ->
            data.forEach { section ->
                section.episodes.forEach {
                    if (it.epId == currentEpId) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("缓存倾向")
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Outlined.Info, contentDescription = "说明", modifier = Modifier.size(18.dp).clickable{
                    onToVideoCodingInfo.invoke()
                })
                Spacer(Modifier.weight(1f))
                SwitchSelectModelTabRow(onSelectSingle = onSelectSingleModel)
            }
            AsAutoError(donghuaPlayerInfo, onSuccessContent = {
                Column {
                    VideoSupportFormatsSelectScreen(
                        Modifier
                            .fillMaxWidth()
                            .shimmer(donghuaPlayerInfo.status != ApiStatus.SUCCESS),
                        downloadInfo,
                        donghuaPlayerInfo.data?.supportFormats,
                        donghuaPlayerInfo.data?.dash?.video,
                        donghuaPlayerInfo.data?.durls,
                        onVideoQualityChange = onVideoQualityChange,
                        onVideoCodeChange = onVideoCodeChange
                    )
                    Spacer(Modifier.height(6.dp))
                    // 当音频质量列表不为空时才显示音频质量选择
                    AudioQualitySelectScreen(
                        Modifier
                            .fillMaxWidth()
                            .shimmer(donghuaPlayerInfo.status != ApiStatus.SUCCESS),
                        downloadInfo,
                        donghuaPlayerInfo.status,
                        donghuaPlayerInfo.data?.dash?.audio,
                        onAudioQualityChange = onAudioQualityChange
                    )
                }
            })

            AsAutoError(
                donghuaViewInfo,
                onSuccessContent = {
                    Column(
                        Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            Modifier
                                .animateContentSize()
                                .shimmer(donghuaViewInfo.status != ApiStatus.SUCCESS)
                        ) {
                            Text("选择缓存剧集")
                            Column {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    items(
                                        donghuaViewInfo.data?.seasons ?: emptyList(),
                                        key = { it.seasonId }
                                    ) { info ->
                                        ToggleButton(
                                            checked = info.seasonId == selectSeasonsId,
                                            onCheckedChange = {
                                                if (it) {
                                                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                                    currentEpListIndex = 0
                                                    selectSeasonsId = info.seasonId
                                                    onSelectSeason(info.seasonId)
                                                }
                                            },
                                        ) {
                                            Text(info.seasonTitle)
                                        }
                                    }
                                }

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    val episodeCount = donghuaViewInfo.data?.episodes?.size ?: 0
                                    val pageCount = ceil(episodeCount / 12.0).toInt()
                                    items(
                                        pageCount,
                                        key = { "ep_$it" }
                                    ) { index ->
                                        val startEp = index * 12 + 1
                                        val endEp = minOf((index + 1) * 12, episodeCount)
                                        FilterChip(
                                            onClick = {
                                                currentEpListIndex = index
                                            },
                                            label = {
                                                Text("$startEp~$endEp")
                                            },
                                            selected = index == currentEpListIndex,
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
                                val episodeList = donghuaViewInfo.data?.episodes ?: emptyList()
                                val startIndex = currentEpListIndex * 12
                                val endIndex =
                                    minOf((currentEpListIndex + 1) * 12, episodeList.size)

                                items(episodeList.subList(startIndex, endIndex)) {
                                    Box {
                                        FilterChip(
                                            enabled = !(!isVip && it.badge == "会员"),
                                            selected = downloadInfo?.selectedEpId?.contains(it.epId) == true,
                                            onClick = {
                                                onUpdateSelectedEpId.invoke(
                                                    it.epId,
                                                    it.longTitle.ifBlank { it.title },
                                                    it.cover
                                                )
                                            },
                                            label = {
                                                Column(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .height(60.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        it.longTitle.ifBlank { it.title },
                                                        maxLines = 2,
                                                        fontSize = 14.sp,
                                                        overflow = TextOverflow.Ellipsis,
                                                    )
                                                }
                                            }
                                        )
                                        if (it.badge.isNotEmpty()) {
                                            Surface(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd),
                                                shape = FilterChipDefaults.shape,
                                                color = MaterialTheme.colorScheme.primary,
                                            ) {
                                                Text(
                                                    it.badge, modifier = Modifier.padding(3.dp),
                                                    fontSize = 8.sp,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false
                                                        )
                                                    )
                                                )
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        Column(
                            Modifier
                                .animateContentSize()
                                .shimmer(donghuaViewInfo.status != ApiStatus.SUCCESS)
                        ) {
                            Text("选择缓存预告")
                            Column {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    items(donghuaViewInfo.data?.section ?: emptyList(), key = {
                                        it.id
                                    }) { info ->
                                        ToggleButton(
                                            checked = selectSectionId == info.id,
                                            onCheckedChange = {
                                                if (it) {
                                                    haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                                    selectSectionId = info.id
                                                }
                                            },
                                        ) {
                                            Text(info.title)
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            LazyVerticalGrid(
                                GridCells.Fixed(3),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.sizeIn(maxHeight = (60 * 2 + 2 * 10).dp)
                            ) {
                                items(
                                    donghuaViewInfo.data?.section?.firstOrNull { it.id == selectSectionId }?.episodes
                                        ?: emptyList()
                                ) {
                                    FilterChip(
                                        selected = downloadInfo?.selectedEpId?.contains(it.epId) == true,
                                        onClick = {
                                            onUpdateSelectedEpId.invoke(it.epId, it.longTitle.ifBlank { it.title }, it.cover)
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
                                                    it.longTitle.ifBlank { it.title }, maxLines = 2,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                })

        }
    }
}
