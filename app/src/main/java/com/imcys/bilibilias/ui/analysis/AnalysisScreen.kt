package com.imcys.bilibilias.ui.analysis

import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Build
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.utils.copyText
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.download.CCFileType
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.app.AppOldCommonBean
import com.imcys.bilibilias.network.model.app.AppOldSoFreezeBean
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfoV2
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import com.imcys.bilibilias.network.model.video.SelectEpisodeType
import com.imcys.bilibilias.ui.analysis.components.DongmhuaDownloadScreen
import com.imcys.bilibilias.ui.analysis.components.VideoDownloadScreen
import com.imcys.bilibilias.ui.analysis.navigation.AnalysisRoute
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASCardTextField
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.ui.weight.tip.ASErrorTip
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.ASCommonSelectGrid
import com.imcys.bilibilias.weight.AsAutoError
import com.imcys.bilibilias.weight.AsUserInfoRow
import com.imcys.bilibilias.weight.dialog.PermissionRequestTipDialog
import kotlinx.coroutines.launch
import kotlin.text.ifEmpty

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnalysisScreen(
    analysisRoute: AnalysisRoute,
    vm: AnalysisViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onToBack: () -> Unit,
    goToUser: (mid: Long) -> Unit,
    onToVideoCodingInfo: () -> Unit,
    onToLogin: () -> Unit
) {

    val uiState by vm.uiState.collectAsState()
    val isSelectSingleModel = uiState.isSelectSingleModel
    val episodeListMode = uiState.episodeListMode

    LaunchedEffect(analysisRoute.asInputText) {
        // 解析分享内容
        vm.updateInputAsText(analysisRoute.asInputText)
    }


    AnalysisScaffold(
        uiState.asLinkResultType,
        uiState.downloadInfo,
        onDownload = {
            vm.createDownloadTask()
        },
        onToBack = onToBack
    ) {
        Column(Modifier.padding(it)) {
            with(sharedTransitionScope) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    ASCardTextField(
                        modifier = Modifier.sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "card-input-analysis"),
                            animatedVisibilityScope = animatedContentScope
                        ),
                        elevation = CardDefaults.cardElevation(0.dp),
                        value = uiState.inputAsText,
                        onValueChange = { value ->
                            vm.updateInputAsText(value)
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(
                                        key = "icon-input-analysis"
                                    ),
                                    animatedVisibilityScope = animatedContentScope
                                ),
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "视频解析",
                            )
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

            uiState.asLinkResultType?.let { type ->
                AnalysisVideoCardList(
                    uiState.downloadInfo,
                    type,
                    uiState.isBILILogin,
                    uiState.analysisBaseInfo,
                    isSelectSingleModel = isSelectSingleModel,
                    episodeListMode = episodeListMode,
                    viewModel = vm,
                    goToUser = goToUser,
                    onToVideoCodingInfo = onToVideoCodingInfo,
                    onToLogin = onToLogin
                )
            }

        }
    }

    CreateDownloadTaskLoadingDialog(
        uiState.isCreateDownloadLoading,
    )

//    VideoFreezeTip(
//        uiState.appOldSoFreezeBean,
//        onDismiss = {
//            vm.closeShowVideoFreezeTip()
//        }
//    )

}

/**
 *
 */
@Composable
fun VideoFreezeTip(
    appOldSoFreezeBean: AppOldSoFreezeBean?,
    onDismiss: () -> Unit
) {

    if (appOldSoFreezeBean == null) return
    ASAlertDialog(showState = true, title = {
        Text(stringResource(R.string.analysis_protected_video))
    }, text = {
        val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
        AndroidView(
            factory = { TextView(it) },
            update = {
                val tip = """
                        ${appOldSoFreezeBean.msg} <br>
                        暂时无法提供该视频的缓存，这是由UP自己或其他主体可发起的冻结，冻结后BILIBILIAS不再提供对这些视频的缓存。<br>
                        具体详见：<br>
                        <a href="https://docs.qq.com/doc/DVVdQa1J5aGxJcm5Y">《BILIBILIAS UP主权益保护计划》</a>。
                    """.trimIndent()
                it.apply {
                    it.setTextColor(textColor)
                    text = HtmlCompat.fromHtml(tip, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    movementMethod = LinkMovementMethod.getInstance()
                }
            }
        )
    }, confirmButton = {
        ASTextButton(onClick = onDismiss) {
            Text(stringResource(R.string.common_i_know))
        }
    })
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CreateDownloadTaskLoadingDialog(show: Boolean) {
    if (show) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "创建下载任务") },
            text = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ContainedLoadingIndicator()
                    Text(text = "正在创建任务，创建过程中请勿挂后台...")
                }
            },
            confirmButton = {},
            dismissButton = {},
        )
    }
}

@Composable
fun ColumnScope.AnalysisVideoCardList(
    downloadInfo: DownloadViewInfo?,
    asLinkResultType: ASLinkResultType,
    isBILILogin: Boolean,
    analysisBaseInfo: AnalysisBaseInfo,
    isSelectSingleModel: Boolean,
    episodeListMode: AppSettings.EpisodeListMode,
    viewModel: AnalysisViewModel,
    goToUser: (Long) -> Unit,
    onToVideoCodingInfo: () -> Unit,
    onToLogin: () -> Unit,
) {
    val donghuaPlayerInfo by viewModel.donghuaPlayerInfo.collectAsState()
    val videoPlayerInfo by viewModel.videoPlayerInfo.collectAsState()
    val currentUserInfo by viewModel.currentUserInfo.collectAsState()
    val interactiveVideo by viewModel.interactiveVideo.collectAsState()
    val boostVideoInfo by viewModel.boostVideoInfo.collectAsState()

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 15.dp)
    ) {
        item {
            AnalysisVideoCard(asLinkResultType, isBILILogin, analysisBaseInfo, savePic = {
                viewModel.downloadImageToAlbum(context, it, "BILIBILIAS")
            }, goToUser, onToLogin, boostVideoInfo)
        }
        item {
            when (asLinkResultType) {
                is ASLinkResultType.BILI.Donghua -> {
                    DongmhuaDownloadScreen(
                        downloadInfo,
                        donghuaPlayerInfo,
                        currentUserInfo,
                        isSelectSingleModel,
                        episodeListMode,
                        asLinkResultType.currentEpId,
                        asLinkResultType.donghuaViewInfo,
                        onSelectSeason = {
                            viewModel.updateSelectSeason(it)
                        },
                        onUpdateSelectedEpId = { epId, selectType, title, cover ->
                            if (isSelectSingleModel) {
                                viewModel.clearSelectedEpIdList()
                                viewModel.updateSelectedPlayerInfo(
                                    epId ?: 0L,
                                    selectType,
                                    title,
                                    cover
                                )
                                if (selectType is SelectEpisodeType.EPID) {
                                    viewModel.updateSelectedEpIdList(epId)
                                }
                            } else {
                                viewModel.updateSelectedEpIdList(epId)
                            }
                        },
                        onVideoQualityChange = {
                            viewModel.updateVideoQualityId(it)
                        },
                        onVideoCodeChange = {
                            viewModel.updateVideoCode(it)
                        },
                        onAudioQualityChange = {
                            viewModel.updateAudioQualityId(it)
                        },
                        onSelectSingleModel = {
                            viewModel.updateSelectSingleModel(it)
                            if (it) {
                                // 切换到单选模式，清空已选择列表
                                val lastEpId = downloadInfo?.selectedEpId?.lastOrNull()
                                viewModel.clearSelectedEpIdList()
                                viewModel.updateSelectedEpIdList(lastEpId)
                            }
                        },
                        onToVideoCodingInfo = onToVideoCodingInfo,
                        onUpdateEpisodeListMode = {
                            viewModel.updateEpisodeListMode(it)
                        }
                    )
                }

                is ASLinkResultType.BILI.Video -> {

                    if (asLinkResultType.isNotFound()) {
                        CheckInputASTextTip()
                        return@item
                    }

                    VideoDownloadScreen(
                        downloadInfo,
                        videoPlayerInfo,
                        isSelectSingleModel,
                        episodeListMode,
                        asLinkResultType.currentBvId,
                        asLinkResultType.viewInfo,
                        interactiveVideo,
                        boostVideoInfo,
                        onUpdateSelectedCid = { it, selectType, title, cover ->
                            if (isSelectSingleModel) {
                                viewModel.clearSelectedCidList()
                                viewModel.updateSelectedPlayerInfo(
                                    it ?: 0L,
                                    selectType,
                                    title,
                                    cover
                                )
                                viewModel.updateSelectedCidList(it)
                            } else {
                                viewModel.updateSelectedCidList(it)
                            }
                        },
                        onVideoQualityChange = {
                            viewModel.updateVideoQualityId(it)
                        },
                        onVideoCodeChange = {
                            viewModel.updateVideoCode(it)
                        },
                        onAudioQualityChange = {
                            viewModel.updateAudioQualityId(it)
                        },
                        onSelectSingleModel = {
                            viewModel.updateSelectSingleModel(it)
                            if (it) {
                                // 切换到单选模式，清空已选择列表
                                val lastCid = downloadInfo?.selectedCid?.lastOrNull()
                                viewModel.clearSelectedCidList()
                                viewModel.updateSelectedCidList(lastCid)
                            }
                        },
                        onToVideoCodingInfo = onToVideoCodingInfo,
                        onUpdateEpisodeListMode = {
                            viewModel.updateEpisodeListMode(it)
                        },
                        onUpdateAudioLanguage = {
                            viewModel.updateAudioLanguage(it)
                        }
                    )
                }

                else -> {}
            }
        }
        item {
            when (asLinkResultType) {
                is ASLinkResultType.BILI.Donghua -> {
                    AdvancedSetting(
                        isSelectSingleModel,
                        emptyNetWorkResult(),
                        downloadInfo,
                        donghuaPlayerInfo,
                        donghuaPlayerInfo.data?.dash,
                        onCheckCoverDownload = {
                            viewModel.updateDownloadCover(it)
                        },
                        onCheckDownloadDanmaku = {
                            viewModel.updateDownloadDanmaku(it)
                        },
                        onCheckMediaDownload = {
                            viewModel.updateDownloadMedia(it)
                        }
                    ) {
                        viewModel.updateDownloadMode(it)
                    }
                }

                is ASLinkResultType.BILI.Video ->
                    AdvancedSetting(
                        isSelectSingleModel,
                        asLinkResultType.viewInfo,
                        downloadInfo,
                        videoPlayerInfo,
                        videoPlayerInfo.data?.dash,
                        onCheckCoverDownload = {
                            viewModel.updateDownloadCover(it)
                        },
                        onCheckDownloadDanmaku = {
                            viewModel.updateDownloadDanmaku(it)
                        },
                        onCheckMediaDownload = {
                            viewModel.updateDownloadMedia(it)
                        },
                        onSelectCCId = { id, type ->
                            viewModel.updateSelectCCIdList(id, type)
                        },
                        onCleanCCId = {
                            viewModel.clearCCIdList()
                        }
                    ) {
                        viewModel.updateDownloadMode(it)
                    }

                else -> {

                }
            }

        }
        item {
            Spacer(Modifier.height(15.dp))
        }
    }

}

/**
 * 检查输入
 */
@Composable
fun CheckInputASTextTip() {
    ASWarringTip {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "输入的视频不存在哦~请检查后重新输入",
                fontSize = 14.sp,
            )
        }
    }
}

/**
 * 下载视频的高级设置
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSetting(
    isSelectSingleModel: Boolean,
    videoInfo: NetWorkResult<BILIVideoViewInfo?>,
    downloadInfo: DownloadViewInfo?,
    playerInfo: NetWorkResult<Any?>,
    dash: BILIVideoDash?,
    onCheckCoverDownload: (Boolean) -> Unit,
    onCheckDownloadDanmaku: (Boolean) -> Unit,
    onCheckMediaDownload: (Boolean) -> Unit,
    onSelectCCId: (Long, CCFileType) -> Unit = { _, _ -> },
    onCleanCCId: () -> Unit = {},
    onSelectDownloadMode: (DownloadMode) -> Unit,
) {
    var downloadModeExpanded by rememberSaveable { mutableStateOf(false) }
    var selectDownloadMode by rememberSaveable { mutableStateOf(DownloadMode.AUDIO_VIDEO) }

    LaunchedEffect(dash?.audio) {
        if (dash != null && dash.audio.isEmpty()) {
            selectDownloadMode = DownloadMode.VIDEO_ONLY
            onSelectDownloadMode(selectDownloadMode)
        }
    }


    // 选择是否合并下载
    AsAutoError(playerInfo, onSuccessContent = {
        if (dash == null && playerInfo.status != ApiStatus.LOADING) return@AsAutoError
        SurfaceColorCard {
            Column(
                Modifier
                    .shimmer(playerInfo.status != ApiStatus.SUCCESS)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(stringResource(R.string.analysis_cache_config))
                Column {
                    ExposedDropdownMenuBox(
                        expanded = downloadModeExpanded,
                        onExpandedChange = {
                            downloadModeExpanded = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        TextField(
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 12.sp
                            ),
                            value = selectDownloadMode.title,
                            onValueChange = {

                            },
                            readOnly = true,
                            singleLine = false,
                            label = { Text(stringResource(R.string.analysis_select_cache_mode), fontSize = 12.sp) },
                            trailingIcon = { TrailingIcon(expanded = downloadModeExpanded) },
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
                            expanded = downloadModeExpanded,
                            onDismissRequest = { downloadModeExpanded = false },
                        ) {
                            DownloadMode.entries.filter {
                                // 如果没有音频资源，则不显示音视频分离选项
                                if (dash?.audio.isNullOrEmpty() && (it == DownloadMode.AUDIO_VIDEO || it == DownloadMode.AUDIO_ONLY)) {
                                    false
                                } else true
                            }.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            it.title,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        downloadModeExpanded = false
                                        selectDownloadMode = it
                                        onSelectDownloadMode(it)
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }

                    }
                    Spacer(Modifier.height(4.dp))
                    ASWarringTip {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "注意：如果选中的子集没有音视频分离资源，将无法单独进行下载。",
                                fontSize = 14.sp,
                            )
                        }
                    }
                }

                Column {
                    Text(stringResource(R.string.analysis_download_content))
                    ExtraCache(
                        isSelectSingleModel,
                        downloadInfo,
                        downloadInfo?.videoPlayerInfoV2,
                        playerInfo,
                        onCheckCoverDownload = onCheckCoverDownload,
                        onCheckDownloadDanmaku = onCheckDownloadDanmaku,
                        onCheckMediaDownload = onCheckMediaDownload,
                        onSelectCCId = onSelectCCId,
                        onCleanCCId = onCleanCCId,
                    )
                }

            }
        }
    })
}

@Composable
fun ExtraCache(
    isSelectSingleModel: Boolean,
    downloadInfo: DownloadViewInfo?,
    playerInfoV2: NetWorkResult<BILIVideoPlayerInfoV2?>?,
    playerInfo: NetWorkResult<Any?>,
    onCheckCoverDownload: (Boolean) -> Unit,
    onCheckDownloadDanmaku: (Boolean) -> Unit,
    onCheckMediaDownload: (Boolean) -> Unit,
    onSelectCCId: (Long, CCFileType) -> Unit = { _, _ -> },
    onCleanCCId: () -> Unit = {},
) {

    var selectACCDownload by rememberSaveable { mutableStateOf(false) }

    val isDonghua = playerInfo.data is BILIDonghuaPlayerInfo
    val isVideo = playerInfo.data is BILIVideoPlayerInfo

    LaunchedEffect(isSelectSingleModel) {
        selectACCDownload = false
    }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        FilterChip(
            label = {
                Text(stringResource(R.string.analysis_stream_download), fontSize = 12.sp)
            },
            selected = downloadInfo?.downloadMedia == true,
            leadingIcon = {
                if (downloadInfo?.downloadMedia == true) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = "已选中图标",
                        modifier = Modifier.size(15.dp)
                    )
                }
            },
            onClick = {
                onCheckMediaDownload(!(downloadInfo?.downloadMedia ?: false))
            },
        )



        FilterChip(
            label = {
                Text(stringResource(R.string.analysis_cover_download), fontSize = 12.sp)
            },
            selected = downloadInfo?.downloadCover == true,
            leadingIcon = {
                if (downloadInfo?.downloadCover == true) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = "已选中图标",
                        modifier = Modifier.size(15.dp)
                    )
                }
            },
            onClick = {
                onCheckCoverDownload(!(downloadInfo?.downloadCover ?: false))
            },
        )

        if (!playerInfoV2?.data?.subtitle?.subtitles.isNullOrEmpty() && isSelectSingleModel) {
            FilterChip(
                label = {
                    Text(stringResource(R.string.analysis_download_subtitle), fontSize = 12.sp)
                },
                selected = selectACCDownload,
                leadingIcon = {
                    if (selectACCDownload) {
                        Icon(
                            Icons.Outlined.Check,
                            contentDescription = "已选中图标",
                            modifier = Modifier.size(15.dp)
                        )
                    }
                },
                onClick = {
                    selectACCDownload = !selectACCDownload
                    if (!selectACCDownload) {
                        onCleanCCId()
                    }
                },
            )
        }


        FilterChip(
            label = {
                Text(stringResource(R.string.analysis_danmaku_download), fontSize = 12.sp)
            },
            selected = downloadInfo?.downloadDanmaku == true,
            leadingIcon = {
                if (downloadInfo?.downloadDanmaku == true) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = "已选中图标",
                        modifier = Modifier.size(15.dp)
                    )
                }
            },
            onClick = {
                onCheckDownloadDanmaku(!(downloadInfo?.downloadDanmaku ?: false))
            },
        )
    }

    if (selectACCDownload && isSelectSingleModel) {
        Text(stringResource(R.string.analysis_download_subtitle))
        Spacer(Modifier.height(5.dp))
        SelectACCCard(downloadInfo, onSelectCCId = onSelectCCId)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectACCCard(
    downloadInfo: DownloadViewInfo?,
    onSelectCCId: (Long, CCFileType) -> Unit = { _, _ -> },
) {
    var selectType by rememberSaveable { mutableStateOf(CCFileType.SRT) }
    var ccFileTypeExpanded by rememberSaveable { mutableStateOf(false) }
    if (downloadInfo?.videoPlayerInfoV2 == null) return
    AsAutoError(downloadInfo.videoPlayerInfoV2, onSuccessContent = {
        Column {
            ExposedDropdownMenuBox(
                expanded = ccFileTypeExpanded,
                onExpandedChange = {
                    ccFileTypeExpanded = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer(downloadInfo.videoPlayerInfoV2.status != ApiStatus.SUCCESS),
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 12.sp
                    ),
                    value = selectType.name,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    label = { Text(stringResource(R.string.analysis_select_subtitle_type), fontSize = 12.sp) },
                    trailingIcon = { TrailingIcon(expanded = ccFileTypeExpanded) },
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
                    expanded = ccFileTypeExpanded,
                    onDismissRequest = { ccFileTypeExpanded = false },
                ) {
                    CCFileType.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    it.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                ccFileTypeExpanded = false
                                selectType = it
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }

            }

            Spacer(Modifier.height(5.dp))

            ASCommonSelectGrid(
                items = downloadInfo.videoPlayerInfoV2.data?.subtitle?.subtitles ?: emptyList(),
                key = { it.id },
                title = { it.lanDoc },
                onClick = { onSelectCCId.invoke(it.id, selectType) },
                selected = { downloadInfo.selectedCCId.contains(it.id) }
            )
        }
    })
}


@Composable
fun AnalysisVideoCard(
    asLinkResultType: ASLinkResultType,
    isBILILogin: Boolean,
    analysisBaseInfo: AnalysisBaseInfo,
    savePic: suspend (String?) -> Unit,
    goToUser: (Long) -> Unit,
    onToLogin: () -> Unit,
    boostVideoInfo: AppOldCommonBean?,
) {
    when (asLinkResultType) {
        is ASLinkResultType.BILI.Video -> {
            BILIVideoCard(
                asLinkResultType,
                asLinkResultType.viewInfo,
                analysisBaseInfo,
                isBILILogin,
                boostVideoInfo,
                savePic = savePic,
                goToUser = goToUser,
                onToLogin = onToLogin
            )
        }

        is ASLinkResultType.BILI.User -> {
            BILIUserSpaceCard(asLinkResultType.userInfo, goToUser)
        }

        is ASLinkResultType.BILI.Donghua -> {
            BILIDonghuaCard(
                asLinkResultType.donghuaViewInfo,
                asLinkResultType.currentEpId,
                analysisBaseInfo,
                isBILILogin,
                savePic = savePic,
                onToLogin = onToLogin
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BILIDonghuaCard(
    donghuaViewInfo: NetWorkResult<BILIDonghuaSeasonInfo?>,
    currentEpId: Long,
    analysisBaseInfo: AnalysisBaseInfo,
    isBILILogin: Boolean,
    savePic: suspend (String?) -> Unit,
    onToLogin: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    val episodeInfo = remember(donghuaViewInfo, currentEpId) {
        donghuaViewInfo.data?.episodes?.firstOrNull { it.epId == currentEpId }
            ?: donghuaViewInfo.data?.section?.run lastRun@{
                map { it.episodes }.forEach {
                    it.forEach { info ->
                        if (info.epId == currentEpId) {
                            return@lastRun info
                        }
                    }
                }
                donghuaViewInfo.data?.episodes?.firstOrNull()
            }
    }

    var picSaving by rememberSaveable { mutableStateOf(false) }

    AsAutoError(
        netWorkResult = donghuaViewInfo,
        onSuccessContent = {
            Column(Modifier.fillMaxWidth()) {
                SurfaceColorCard {
                    Column(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .shimmer(donghuaViewInfo.status != ApiStatus.SUCCESS),
                        ) {
                            ASAsyncImage(
                                if (analysisBaseInfo.enabledSelectInfo) analysisBaseInfo.cover else
                                    "${episodeInfo?.cover?.toHttps()}",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentDescription = "视频封面",
                                shape = CardDefaults.shape
                            )


                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(5.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CardDefaults.shape
                            ) {
                                Text(stringResource(R.string.analysis_bangumi), Modifier.padding(5.dp))
                            }


                            ExtendedFloatingActionButton(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(5.dp)
                                    .animateContentSize(),
                                onClick = click@{
                                    if (picSaving) return@click
                                    picSaving = true
                                    coroutineScope.launch {
                                        savePic.invoke(
                                            if (analysisBaseInfo.enabledSelectInfo) {
                                                analysisBaseInfo.cover
                                            } else {
                                                episodeInfo?.cover?.toHttps()
                                            }
                                        )
                                        picSaving = false
                                    }
                                },
                                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                                containerColor = Color(0xffD8FFC0),
                                contentColor = MaterialTheme.colorScheme.scrim,
                            ) {
                                if (picSaving) {
                                    CircularWavyProgressIndicator()
                                } else {
                                    Icon(Icons.Outlined.Image, contentDescription = "下载封面")
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(stringResource(R.string.analysis_download_cover))
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        val title =
                            if (analysisBaseInfo.enabledSelectInfo) analysisBaseInfo.title else
                                episodeInfo?.longTitle?.ifEmpty { episodeInfo.title } ?: "视频标题"
                        Text(
                            title,
                            fontSize = 22.sp,
                            modifier = Modifier
                                .animateContentSize()
                                .shimmer(donghuaViewInfo.status != ApiStatus.SUCCESS)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        title.copyText(context, "视频标题")
                                    }
                                ),
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                if (!isBILILogin) {
                    ASWarringTip(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        enabledPadding = false
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "你当前还未绑定B站账号，缓存权益受限哦。",
                                fontSize = 14.sp,
                            )
                            Spacer(Modifier.weight(1f))
                            ASIconButton(onClick = onToLogin) {
                                Icon(Icons.Outlined.NorthEast, contentDescription = "去登录")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun BILIUserSpaceCard(userInfo: NetWorkResult<BILIUserSpaceAccInfo?>, goToUser: (Long) -> Unit) {
    AsAutoError(
        netWorkResult = userInfo,
        onSuccessContent = {
            SurfaceColorCard(modifier = Modifier.clickable {
                goToUser(userInfo.data?.mid ?: 0)
            }) {
                AsUserInfoRow(
                    Modifier
                        .padding(16.dp),
                    userInfo,
                    userInfo.status == ApiStatus.LOADING
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BILIVideoCard(
    asLinkResultType: ASLinkResultType.BILI.Video,
    videoInfo: NetWorkResult<BILIVideoViewInfo?>,
    analysisBaseInfo: AnalysisBaseInfo,
    isBILILogin: Boolean,
    boostVideoInfo: AppOldCommonBean?,
    savePic: suspend (String?) -> Unit,
    goToUser: (Long) -> Unit,
    onToLogin: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var picSaving by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    // 拦截未查到视频的错误
    if (asLinkResultType.isNotFound()) {
        return
    }

    AsAutoError(
        netWorkResult = videoInfo,
        onSuccessContent = {
            Column(Modifier.fillMaxWidth()) {
                SurfaceColorCard {
                    Column(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()

                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        ) {
                            ASAsyncImage(
                                if (analysisBaseInfo.enabledSelectInfo) analysisBaseInfo.cover else
                                    "${
                                        videoInfo.data?.pic?.toHttps()
                                    }",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shimmer(videoInfo.status == ApiStatus.LOADING),
                                contentDescription = "视频封面",
                                shape = CardDefaults.shape
                            )

                            if (videoInfo.data?.isUpowerExclusive == true) {
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(5.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = CardDefaults.shape
                                ) {
                                    Text(stringResource(R.string.analysis_charged_video), Modifier.padding(5.dp))
                                }
                            }


                            ExtendedFloatingActionButton(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(5.dp),
                                onClick = click@{
                                    if (picSaving) return@click
                                    picSaving = true
                                    coroutineScope.launch {
                                        savePic.invoke(
                                            if (analysisBaseInfo.enabledSelectInfo) {
                                                analysisBaseInfo.cover
                                            } else {
                                                videoInfo.data?.pic?.toHttps()
                                            }
                                        )
                                        picSaving = false
                                    }
                                },
                                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                                containerColor = Color(0xffD8FFC0),
                                contentColor = MaterialTheme.colorScheme.scrim
                            ) {
                                if (picSaving) {
                                    CircularWavyProgressIndicator()
                                } else {
                                    Icon(Icons.Outlined.Image, contentDescription = "下载封面")
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(stringResource(R.string.analysis_download_cover))
                                }
                            }
                        }

                        AuthorInfoContent(videoInfo, goToUser)

                        val title =
                            if (analysisBaseInfo.enabledSelectInfo) analysisBaseInfo.title else
                                videoInfo.data?.title ?: "视频标题"
                        Text(
                            title,
                            fontSize = 22.sp,
                            modifier = Modifier
                                .animateContentSize()
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        title.copyText(context, "视频标题")
                                    }
                                ),
                        )
                    }
                }


                // 如果是充电视频，提示用户充电
                if (!asLinkResultType.isCanPlay()) {
                    Spacer(Modifier.height(16.dp))
                    ASWarringTip {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "当前视频属充电视频，请充电后申请缓存。",
                                fontSize = 14.sp,
                            )
                        }
                    }

                    if (boostVideoInfo?.code != 0) {
                        Spacer(Modifier.height(10.dp))
                        ASErrorTip {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    Icons.Outlined.ErrorOutline,
                                    contentDescription = "警告",
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    boostVideoInfo?.msg ?: "",
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }

                }


                // 如果未登录B站账号，提示用户登录
                if (!isBILILogin) {
                    Spacer(Modifier.height(11.dp))
                    ASWarringTip(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        enabledPadding = false
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "你当前还未绑定B站账号，缓存权益受限哦。",
                                fontSize = 14.sp,
                            )
                            Spacer(Modifier.weight(1f))
                            ASIconButton(onClick = onToLogin) {
                                Icon(Icons.Outlined.NorthEast, contentDescription = "去登录")
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AuthorInfoContent(
    result: NetWorkResult<BILIVideoViewInfo?>,
    goToUser: (Long) -> Unit
) {
    val videoInfo = result.data
    val authorList = when {
        videoInfo?.staff.isNullOrEmpty() -> videoInfo?.owner?.let { listOf(it) } ?: emptyList()
        else -> videoInfo.staff.filter { it.mid != 0L }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth().run {
            if (authorList.size == 1) {
                clickable { goToUser(authorList.first().mid) }
            } else this
        }
    ) {
        items(authorList, key = { it.mid }) { user ->
            Surface(
                shape = RoundedCornerShape(4.dp),
                onClick = { goToUser(user.mid) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ASAsyncImage(
                        model = user.face,
                        contentDescription = "up头像",
                        shape = CircleShape,
                        modifier = Modifier
                            .size(22.dp)
                            .aspectRatio(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        user.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScaffold(
    asResultType: ASLinkResultType?,
    downloadInfo: DownloadViewInfo?,
    onToBack: () -> Unit,
    onDownload: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    var showDownloadTip by rememberSaveable { mutableStateOf(false) }

    var showRequestPermissionTip by rememberSaveable { mutableStateOf(false) }

    val permissionsToRequest = arrayOf(
        permission.READ_EXTERNAL_STORAGE,
        permission.WRITE_EXTERNAL_STORAGE
    )


    val context = LocalContext.current
    var hasSavePermissions by remember {
        // 在开始时检查权限状态
        mutableStateOf(
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P || permissionsToRequest.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        )
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text(stringResource(R.string.analysis_parse_video))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        AsBackIconButton(onClick = {
                            onToBack.invoke()
                        })
                    },
                    actions = {
                        ASIconButton(onClick = {
                            showDownloadTip = true
                        }) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = "问题提示"
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            val isCanPlay =
                if (asResultType is ASLinkResultType.BILI.Video) asResultType.isCanPlay() else true
            val visible = (downloadInfo?.selectedCid?.isNotEmpty() == true ||
                    downloadInfo?.selectedEpId?.isNotEmpty() == true) && asResultType != null && isCanPlay

            AnimatedVisibility(
                visible = visible,
            ) {
                FloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                            when {
                                !hasSavePermissions -> {
                                    showRequestPermissionTip = true
                                }

                                else -> {
                                    onDownload()
                                }
                            }
                        } else {
                            onDownload()
                        }
                    },
                ) {
                    Icon(Icons.Outlined.Download, "下载视频")
                }
            }
        }
    ) {
        content.invoke(it)
    }

    if (showDownloadTip) {
        DownloadTipDialog(
            onDismiss = { showDownloadTip = false },
            onDownload = {
                showDownloadTip = false
            }
        )
    }

    if (showRequestPermissionTip && !hasSavePermissions) {
        WritePermissionRequestTipDialog(permissionsToRequest, onDismiss = {
            showRequestPermissionTip = false
        }, onRequest = {
            hasSavePermissions = permissionsToRequest.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
            // 存储权限
            if (hasSavePermissions) {
                onDownload.invoke()
            }
        })
    }

}


@Composable
fun WritePermissionRequestTipDialog(
    permissionsToRequest: Array<String>,
    onDismiss: () -> Unit,
    onRequest: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val allGranted = result.values.all { it }
        if (allGranted) {
            onRequest()
        } else {
            Toast.makeText(context, "权限未被授予", Toast.LENGTH_SHORT).show()
        }
    }
    PermissionRequestTipDialog(
        show = true,
        message = "需要存储权限以保存下载内容，是否继续？",
        onConfirm = {
            launcher.launch(permissionsToRequest)
            onRequest()
            onDismiss()
        },
        onDismiss = onDismiss
    )
}


@Composable
fun DownloadTipDialog(onDismiss: () -> Unit, onDownload: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.analysis_download_hint)) },
        text = {
            Text(
                """
                    可以选择的分辨率取决于你当前B站账号的大会员状态和当前解析视频的实际可选分辨率。
                    因此建议选择合集/分P内分辨率最高的视频进行解析，同时开通大会员可享受更高的分辨率缓存。
                """.trimIndent()
            )
        },
        confirmButton = {
            ASTextButton(onClick = onDownload) {
                Text(stringResource(R.string.common_understand))
            }
        },
    )
}

