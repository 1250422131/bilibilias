package com.imcys.bilibilias.ui.analysis.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
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
import com.imcys.bilibilias.weight.AsAutoError
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoDownloadScreen(
    downloadInfo: DownloadViewInfo?,
    videoPlayerInfo: NetWorkResult<BILIVideoPlayerInfo?>,
    currentBvId: String,
    viewInfo: NetWorkResult<BILIVideoViewInfo?>,
    onUpdateSelectedCid: (Long?) -> Unit,
    onVideoQualityChange: (Long?) -> Unit = {},
    onVideoCodeChange: (String) -> Unit = {},
    onAudioQualityChange: (Long?) -> Unit = {},
    onSelectSingleModel:(Boolean) -> Unit = { _ -> }
) {

    if (viewInfo.data?.isUpowerExclusive == true && viewInfo.data?.isUpowerPlay == false) {
        // 拦截缓存，暂不支持
        return
    }

    var selectEpisodeId by remember { mutableStateOf<Long?>(null) }
    var selectSectionId by remember { mutableStateOf<Long?>(null) }

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

        viewInfo.data?.ugcSeason?.sections?.firstOrNull {
            it.id == selectSectionId && it.episodes.size > 1
        }?.episodes?.firstOrNull {
            it.cid == viewInfo.data?.cid
        }?.let {
            selectEpisodeId = it.id
        } ?: run {
            // 如果没有找到对应的合集，则选择第一个合集
            selectEpisodeId = (viewInfo.data?.ugcSeason?.sections?.firstOrNull {
                it.id == selectSectionId
            }?.episodes ?: emptyList()).firstOrNull {
                it.pages.size > 1
            }?.id
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
            Row (verticalAlignment = Alignment.CenterVertically){
                Text("缓存倾向")
                Spacer(Modifier.weight(1f))
                SwitchSelectModelTabRow(onSelectSingle = onSelectSingleModel)
            }
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
                        Modifier.fillMaxWidth().shimmer(videoPlayerInfo.status != ApiStatus.SUCCESS),
                        downloadInfo,
                        videoPlayerInfo.status,
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

                        UgcSeasonScreen(
                            viewInfo,
                            downloadInfo,
                            selectSectionId,
                            onSelectSectionId = {
                                selectSectionId = it
                            },
                            onUpdateSelectedCid = onUpdateSelectedCid
                        )

                        UgcSeasonPageScreen(
                            viewInfo,
                            downloadInfo,
                            selectSectionId,
                            selectEpisodeId,
                            onSelectEpisodeId = {
                                selectEpisodeId = it
                            },
                            onUpdateSelectedCid = onUpdateSelectedCid
                        )


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
                        VideoPageScreen(
                            viewInfo,
                            downloadInfo,
                            onUpdateSelectedCid = onUpdateSelectedCid
                        )
                    }
                })
            }
        }
    }
}

@Composable
fun VideoPageScreen(
    viewInfo: NetWorkResult<BILIVideoViewInfo?>,
    downloadInfo: DownloadViewInfo?,
    onUpdateSelectedCid: (Long) -> Unit
) {

    var currentVideoPageListIndex by remember { mutableIntStateOf(0) }

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
                onClick = { onUpdateSelectedCid.invoke(it.cid) },
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


/**
 * 番剧合集内分P和分P子集选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UgcSeasonPageScreen(
    viewInfo: NetWorkResult<BILIVideoViewInfo?>,
    downloadInfo: DownloadViewInfo?,
    selectSectionId: Long?,
    selectEpisodeId: Long?,
    onSelectEpisodeId: (Long?) -> Unit,
    onUpdateSelectedCid: (Long) -> Unit
) {

    var currentVideoPageListIndex by remember { mutableIntStateOf(0) }
    var videoEpisodeExpanded by remember { mutableStateOf(false) }

    val epVideoList = (viewInfo.data?.ugcSeason?.sections?.firstOrNull {
        it.id == selectSectionId
    }?.episodes ?: emptyList()).filter {
        it.pages.size > 1
    }

    // 合计内章节子集分P视频
    epVideoList.takeIf { it.isNotEmpty() }?.let { episodes ->

        ExposedDropdownMenuBox(
            expanded = videoEpisodeExpanded,
            onExpandedChange = {
                videoEpisodeExpanded = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 12.sp
                ),
                value = episodes.firstOrNull { it.id == selectEpisodeId }?.title
                    ?: "",
                onValueChange = {},
                readOnly = true,
                singleLine = false,
                label = { Text("选择分P视频", fontSize = 12.sp) },
                trailingIcon = { TrailingIcon(expanded = false) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                shape = CardDefaults.shape
            )

            ExposedDropdownMenu(
                expanded = videoEpisodeExpanded,
                onDismissRequest = { videoEpisodeExpanded = false },
            ) {
                episodes.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                it.title,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            videoEpisodeExpanded = false
                            currentVideoPageListIndex = 0
                            onSelectEpisodeId.invoke(it.id)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        modifier = Modifier.background(
                            if (it.pages.any { page ->
                                    page.cid in (downloadInfo?.selectedCid
                                        ?: emptyList())
                                }) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                Color.Transparent
                            }
                        )
                    )
                }
            }
        }


        val episodeList = episodes.firstOrNull { selectEpisodeId == it.id }?.pages ?: emptyList()

        Column {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                val episodeCount = episodeList.size
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
}

/**
 * 番剧合集选择
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UgcSeasonScreen(
    viewInfo: NetWorkResult<BILIVideoViewInfo?>,
    downloadInfo: DownloadViewInfo?,
    selectSectionId: Long?,
    onSelectSectionId: (Long) -> Unit,
    onUpdateSelectedCid: (Long) -> Unit
) {

    var currentSectionPageListIndex by remember { mutableIntStateOf(0) }

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
                            onSelectSectionId.invoke(info.id)
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


        LazyVerticalGrid(
            GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.sizeIn(maxHeight = (60 * 2 + 2 * 10).dp)
        ) {
            val episodeList = viewInfo.data?.ugcSeason?.sections?.firstOrNull {
                it.id == selectSectionId
            }?.episodes?.filter {
                it.pages.size <= 1
            } ?: emptyList()
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
}