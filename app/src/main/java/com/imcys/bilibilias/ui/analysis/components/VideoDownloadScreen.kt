package com.imcys.bilibilias.ui.analysis.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.app.AppOldCommonBean
import com.imcys.bilibilias.network.model.video.BILISteinEdgeInfo
import com.imcys.bilibilias.network.model.video.BILIVideoLanguage
import com.imcys.bilibilias.network.model.video.BILIVideoLanguageItem
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import com.imcys.bilibilias.network.model.video.SelectEpisodeType
import com.imcys.bilibilias.network.model.video.filterWithMultiplePages
import com.imcys.bilibilias.network.model.video.filterWithSinglePage
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.ui.weight.tip.ASErrorTip
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.ASCommonSelectGrid
import com.imcys.bilibilias.weight.ASEpisodeSelection
import com.imcys.bilibilias.weight.ASEpisodeTitle
import com.imcys.bilibilias.weight.ASSectionEpisodeSelection
import com.imcys.bilibilias.weight.AsAutoError
import com.imcys.bilibilias.weight.OnUpdateEpisodeListMode


typealias UpdateSelectedCid = (cid: Long?, selectEpisodeType: SelectEpisodeType, title: String, cover: String) -> Unit

typealias OnUpdateAudioLanguage = (BILIVideoLanguageItem) -> Unit

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoDownloadScreen(
    downloadInfo: DownloadViewInfo?,
    videoPlayerInfo: NetWorkResult<BILIVideoPlayerInfo?>,
    isSelectSingleModel: Boolean,
    episodeListMode: AppSettings.EpisodeListMode,
    currentBvId: String,
    viewInfo: NetWorkResult<BILIVideoViewInfo?>,
    interactiveVideo: NetWorkResult<BILISteinEdgeInfo?>,
    boostVideoInfo: AppOldCommonBean?,
    onUpdateSelectedCid: UpdateSelectedCid,
    onVideoQualityChange: (Long?) -> Unit = {},
    onVideoCodeChange: (String) -> Unit = {},
    onAudioQualityChange: (Long?) -> Unit = {},
    onSelectSingleModel: (Boolean) -> Unit = { _ -> },
    onToVideoCodingInfo: () -> Unit,
    onUpdateEpisodeListMode: OnUpdateEpisodeListMode,
    onUpdateAudioLanguage: OnUpdateAudioLanguage
) {

    if (viewInfo.data?.isUpowerExclusive == true) {
        // 拦截缓存，暂不支持
        if (viewInfo.data?.isUpowerPlay == false) {
            return
        }
        if (boostVideoInfo?.code != 0){
            return
        }
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
            ASErrorTip {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("缓存倾向")
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = "说明",
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            onToVideoCodingInfo.invoke()
                        })
                Spacer(Modifier.weight(1f))
                SwitchSelectModelTabRow(isSelectSingleModel, onSelectSingle = onSelectSingleModel)
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
                        Modifier
                            .fillMaxWidth()
                            .shimmer(videoPlayerInfo.status != ApiStatus.SUCCESS),
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

                        ASEpisodeTitle(
                            "选择缓存合集",
                            episodeListMode = episodeListMode,
                            onUpdateEpisodeListMode = onUpdateEpisodeListMode
                        )

                        UgcSeasonScreen(
                            viewInfo,
                            downloadInfo,
                            selectSectionId,
                            episodeListMode,
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
                            episodeListMode,
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
                        ASEpisodeTitle("选择缓存子集", episodeListMode, onUpdateEpisodeListMode)
                        VideoPageScreen(
                            viewInfo,
                            downloadInfo,
                            episodeListMode,
                            onUpdateSelectedCid = onUpdateSelectedCid,
                        )
                    }
                })
            }


            // 互动视频
            if (viewInfo.data?.rights?.isSteinGate != 0L) {
                AsAutoError(interactiveVideo, onSuccessContent = {
                    Column(
                        Modifier
                            .animateContentSize()
                            .shimmer(viewInfo.status != ApiStatus.SUCCESS)
                    ) {
                        InteractiveVideoPageScreen(
                            interactiveVideo.data,
                            downloadInfo,
                            episodeListMode = episodeListMode,
                            onUpdateSelectedCid = onUpdateSelectedCid,
                            onUpdateEpisodeListMode = onUpdateEpisodeListMode
                        )
                    }
                })
            }



            // AI原声翻译
            videoPlayerInfo.data?.language?.let {
                BILIAISimultaneousInterpretation(
                    it,
                    downloadInfo?.selectAudioLanguage,
                    onUpdateAudioLanguage = onUpdateAudioLanguage
                )
            }

        }
    }
}

@Composable
fun BILIAISimultaneousInterpretation(
    language: BILIVideoLanguage,
    selectAudioLanguage: BILIVideoLanguageItem?,
    onUpdateAudioLanguage: OnUpdateAudioLanguage
) {
    Column(
        modifier = Modifier.animateContentSize()
    ) {
        Text("AI原声翻译")
        AnimatedVisibility(
            visible = selectAudioLanguage != null
        ) {
            Spacer(Modifier.height(6.dp))
            ASWarringTip {
                Text("您正在使用哔哩哔哩还在Beta的功能，本APP提供的下载产物不代表B站最终品质。")
            }
        }
        Spacer(Modifier.height(6.dp))
        ASCommonSelectGrid(
            items = language.items,
            title = { it.title },
            selected = { it == selectAudioLanguage },
            key = { it.title },
            onClick = { onUpdateAudioLanguage(it) }
        )
    }
}

@Composable
fun InteractiveVideoPageScreen(
    steinEdgeInfo: BILISteinEdgeInfo?,
    downloadInfo: DownloadViewInfo?,
    episodeListMode: AppSettings.EpisodeListMode,
    onUpdateSelectedCid: UpdateSelectedCid,
    onUpdateEpisodeListMode: OnUpdateEpisodeListMode
) {
    Column {
        Text("选择互动视频")
        ASEpisodeTitle(
            "选择互动视频",
            episodeListMode,
            onUpdateEpisodeListMode = onUpdateEpisodeListMode
        )
        ASEpisodeSelection(
            episodeList = steinEdgeInfo?.storyList,
            episodeListMode = episodeListMode,
            episodeSelected = {
                downloadInfo?.selectedCid?.contains(it.cid) == true
            },
            episodeTitle = { it.title },
            onUpdateEpisodeSelected = {
                onUpdateSelectedCid.invoke(
                    it.cid,
                    SelectEpisodeType.AID(it.cid),
                    it.title,
                    it.cover
                )
            }
        )

    }
}

@Composable
fun VideoPageScreen(
    viewInfo: NetWorkResult<BILIVideoViewInfo?>,
    downloadInfo: DownloadViewInfo?,
    episodeListMode: AppSettings.EpisodeListMode,
    onUpdateSelectedCid: UpdateSelectedCid,
) {

    ASEpisodeSelection(
        episodeList = viewInfo.data?.pages,
        episodeSelected = {
            downloadInfo?.selectedCid?.contains(it.cid) == true
        },
        episodeListMode = episodeListMode,
        episodeTitle = { it.part },
        onUpdateEpisodeSelected = {
            onUpdateSelectedCid.invoke(
                it.cid,
                SelectEpisodeType.BVID(viewInfo.data?.bvid ?: ""),
                it.part,
                viewInfo.data?.pic ?: ""
            )
        }
    )

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
    episodeListMode: AppSettings.EpisodeListMode,
    onSelectEpisodeId: (Long?) -> Unit,
    onUpdateSelectedCid: UpdateSelectedCid,
) {

    var videoEpisodeExpanded by remember { mutableStateOf(false) }

    val epVideoList = (viewInfo.data?.ugcSeason?.sections?.firstOrNull {
        it.id == selectSectionId
    }?.episodes ?: emptyList()).filterWithMultiplePages()

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
                trailingIcon = { TrailingIcon(expanded = videoEpisodeExpanded) },
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
        ASEpisodeSelection(
            episodeListMode = episodeListMode,
            episodeList = episodeList,
            episodeSelected = {
                downloadInfo?.selectedCid?.contains(it.cid) == true
            },
            episodeTitle = { it.part },
            onUpdateEpisodeSelected = {
                onUpdateSelectedCid.invoke(
                    it.cid,
                    SelectEpisodeType.BVID(viewInfo.data?.bvid ?: ""),
                    it.part,
                    viewInfo.data?.pic ?: ""
                )
            }
        )

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
    episodeListMode: AppSettings.EpisodeListMode,
    onSelectSectionId: (Long) -> Unit,
    onUpdateSelectedCid: UpdateSelectedCid,
) {
    ASSectionEpisodeSelection(
        sectionList = viewInfo.data?.ugcSeason?.sections ?: emptyList(),
        sectionTitle = { info -> info.title },
        sectionChecked = { it.id == selectSectionId },
        episodeList = viewInfo.data?.ugcSeason?.sections?.firstOrNull {
            it.id == selectSectionId
        }?.episodes?.filterWithSinglePage(),
        episodeSelected = { downloadInfo?.selectedCid?.contains(it.cid) == true },
        episodeTitle = { it.title },
        episodeListMode = episodeListMode,
        onSelectSection = {
            onSelectSectionId.invoke(it.id)
        },
        onUpdateSelected = {
            onUpdateSelectedCid.invoke(
                it.cid,
                SelectEpisodeType.BVID(it.bvid),
                it.title,
                it.arc.pic
            )
        }
    )
}