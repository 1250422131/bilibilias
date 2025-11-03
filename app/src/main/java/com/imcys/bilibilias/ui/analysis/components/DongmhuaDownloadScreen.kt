package com.imcys.bilibilias.ui.analysis.components


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.SelectEpisodeType
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.weight.ASEpisodeTitle
import com.imcys.bilibilias.weight.ASSectionEpisodeSelection
import com.imcys.bilibilias.weight.AsAutoError
import com.imcys.bilibilias.weight.OnUpdateEpisodeListMode


typealias UpdateSelectedEpId = (epId: Long?, selectEpisodeType: SelectEpisodeType, title: String, cover: String) -> Unit


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DongmhuaDownloadScreen(
    downloadInfo: DownloadViewInfo?,
    donghuaPlayerInfo: NetWorkResult<BILIDonghuaPlayerInfo?>,
    currentUserInfo: BILIUsersEntity?,
    isSelectSingleModel: Boolean,
    episodeListMode: AppSettings.EpisodeListMode,
    currentEpId: Long,
    donghuaViewInfo: NetWorkResult<BILIDonghuaSeasonInfo?>,
    onSelectSeason: (Long) -> Unit,
    onUpdateSelectedEpId: UpdateSelectedEpId,
    onVideoQualityChange: (Long?) -> Unit = {},
    onVideoCodeChange: (String) -> Unit = {},
    onAudioQualityChange: (Long?) -> Unit = {},
    onSelectSingleModel: (Boolean) -> Unit = { _ -> },
    onUpdateEpisodeListMode: OnUpdateEpisodeListMode,
    onToVideoCodingInfo: () -> Unit
) {

    var selectSeasonsId by remember {
        mutableStateOf<Long?>(null)
    }
    var selectSectionId by remember {
        mutableStateOf<Long?>(null)
    }

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
                Text(stringResource(R.string.download_缓存倾))
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.download_说明),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            onToVideoCodingInfo.invoke()
                        })
                Spacer(Modifier.weight(1f))
                SwitchSelectModelTabRow(
                    isSelectSingleModel = isSelectSingleModel,
                    onSelectSingle = onSelectSingleModel
                )
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
                            Column {
                                ASEpisodeTitle(
                                    stringResource(R.string.download_选择缓_2),
                                    episodeListMode = episodeListMode,
                                    onUpdateEpisodeListMode = onUpdateEpisodeListMode
                                )
                                ASSectionEpisodeSelection(
                                    sectionList = donghuaViewInfo.data?.seasons,
                                    episodeList = donghuaViewInfo.data?.episodes,
                                    sectionChecked = {
                                        selectSeasonsId == it.seasonId
                                    },
                                    episodeSelected = {
                                        downloadInfo?.selectedEpId?.contains(it.epId) == true
                                    },
                                    episodeTitle = { it.longTitle.ifBlank { it.title } },
                                    episodeListMode = episodeListMode,
                                    sectionTitle = { it.seasonTitle },
                                    episodeEnabled = { !(!isVip && it.badge == stringResource(R.string.analysis_会员)) },
                                    episodeContentContainer = { it, content ->
                                        Box {
                                            content()
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
                                    },
                                    onUpdateSelected = {
                                        onUpdateSelectedEpId.invoke(
                                            it.epId,
                                            if (it.epId == 0L) {
                                                SelectEpisodeType.AID(it.aid)
                                            } else {
                                                SelectEpisodeType.EPID(it.epId)
                                            },
                                            it.longTitle.ifBlank { it.title },
                                            it.cover
                                        )
                                    },
                                    onSelectSection = { info ->
                                        selectSeasonsId = info.seasonId
                                        onSelectSeason(info.seasonId)
                                    }

                                )
                            }

                            Column(
                                Modifier
                                    .animateContentSize()
                                    .shimmer(donghuaViewInfo.status != ApiStatus.SUCCESS)
                            ) {
                                ASEpisodeTitle(
                                    stringResource(R.string.download_选择缓_3),
                                    episodeListMode,
                                    onUpdateEpisodeListMode = onUpdateEpisodeListMode
                                )
                                ASSectionEpisodeSelection(
                                    sectionList = donghuaViewInfo.data?.section,
                                    sectionTitle = { it.title },
                                    episodeListMode = episodeListMode,
                                    sectionChecked = { selectSectionId == it.id },
                                    episodeList = donghuaViewInfo.data?.section?.firstOrNull { it.id == selectSectionId }?.episodes,
                                    episodeSelected = {
                                        downloadInfo?.selectedEpId?.contains(it.epId) == true
                                    },
                                    episodeTitle = {
                                        it.longTitle.ifBlank { it.title }
                                    },
                                    onSelectSection = {
                                        selectSectionId = it.id
                                    },
                                    onUpdateSelected = {
                                        onUpdateSelectedEpId.invoke(
                                            it.epId,
                                            if (it.epId == 0L) {
                                                SelectEpisodeType.AID(it.aid)
                                            } else {
                                                SelectEpisodeType.EPID(it.epId)
                                            },
                                            it.longTitle.ifBlank { it.title },
                                            it.cover
                                        )
                                    }

                                )
                            }
                        }
                    }
                })
        }
    }
}
