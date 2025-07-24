package com.imcys.bilibilias.ui.analysis

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import com.imcys.bilibilias.network.model.video.convertAudioQualityIdValue
import com.imcys.bilibilias.network.model.video.convertVideoQualityIdValue
import com.imcys.bilibilias.ui.analysis.components.DongmhuaDownloadScreen
import com.imcys.bilibilias.ui.analysis.components.VideoDownloadScreen
import com.imcys.bilibilias.ui.analysis.navigation.AnalysisRoute
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsCardTextField
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.ui.weight.tip.AsWarringTip
import com.imcys.bilibilias.weight.AsAutoError
import com.imcys.bilibilias.weight.AsUserInfoRow
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AnalysisScreen(
    analysisRoute: AnalysisRoute,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onToBack: () -> Unit,
    goToUser: (mid: Long) -> Unit,
) {
    val vm = koinViewModel<AnalysisViewModel>()

    val uiState by vm.uiState.collectAsState()


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
                    AsCardTextField(
                        modifier = Modifier.sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "card-input-analysis"),
                            animatedVisibilityScope = animatedContentScope
                        ),
                        elevation = CardDefaults.cardElevation(0.dp),
                        value = uiState.inputAsText,
                        onValueChange = { value ->
                            vm.updateInputAsText(value)
                        },
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

            uiState.asLinkResultType?.let { type ->
                AnalysisVideoCardList(
                    uiState.downloadInfo,
                    type,
                    uiState.isBILILogin,
                    vm, goToUser
                )
            }

        }


    }
}

@Composable
fun ColumnScope.AnalysisVideoCardList(
    downloadInfo: DownloadViewInfo?,
    asLinkResultType: ASLinkResultType,
    isBILILogin: Boolean,
    viewModel: AnalysisViewModel,
    goToUser: (Long) -> Unit
) {
    val donghuaPlayerInfo by viewModel.donghuaPlayerInfo.collectAsState()
    val videoPlayerInfo by viewModel.videoPlayerInfo.collectAsState()

    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 15.dp)
    ) {
        item {
            AnalysisVideoCard(asLinkResultType, isBILILogin, goToUser)
        }
        item {
            when (asLinkResultType) {
                is ASLinkResultType.BILI.Donghua -> {
                    DongmhuaDownloadScreen(
                        downloadInfo,
                        donghuaPlayerInfo,
                        asLinkResultType.currentEpId,
                        asLinkResultType.donghuaViewInfo,
                        onSelectSeason = {
                            viewModel.updateSelectSeason(it)
                        },
                        onUpdateSelectedEpId = {
                            viewModel.updateSelectedEpIdList(it)
                        },
                        onVideoQualityChange = {
                            viewModel.updateVideoQualityId(it)
                        },
                        onVideoCodeChange = {
                            viewModel.updateVideoCode(it)
                        },
                        onAudioQualityChange = {
                            viewModel.updateAudioQualityId(it)
                        }
                    )
                }

                is ASLinkResultType.BILI.Video -> {
                    VideoDownloadScreen(
                        downloadInfo,
                        videoPlayerInfo,
                        asLinkResultType.currentBvId,
                        asLinkResultType.viewInfo,
                        onUpdateSelectedCid = {
                            viewModel.updateSelectedCidList(it)
                        },
                        onVideoQualityChange = {
                            viewModel.updateVideoQualityId(it)
                        },
                        onVideoCodeChange = {
                            viewModel.updateVideoCode(it)
                        },
                        onAudioQualityChange = {
                            viewModel.updateAudioQualityId(it)
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
                        donghuaPlayerInfo,
                        donghuaPlayerInfo.data?.dash,
                    ){
                        viewModel.updateDownloadMode(it)
                    }
                }
                is ASLinkResultType.BILI.Video ->
                    AdvancedSetting(
                        videoPlayerInfo,
                        videoPlayerInfo.data?.dash,
                    ){
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
 * 下载视频的高级设置
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSetting(
    playerInfo: NetWorkResult<Any?>,
    dash: BILIVideoDash?,
    onSelectDownloadMode: (DownloadMode)->Unit
) {
    var downloadModeExpanded by remember { mutableStateOf(false) }

    var selectDownloadMode by remember { mutableStateOf(DownloadMode.AUDIO_VIDEO) }


    // 选择是否合并下载
    AsAutoError(playerInfo, onSuccessContent = {
        if (dash != null){
            SurfaceColorCard {
                Column(
                    Modifier
                        .shimmer(playerInfo.status != ApiStatus.SUCCESS)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("缓存配置")
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
                            label = { Text("选择缓存模式", fontSize = 12.sp) },
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
                            expanded = downloadModeExpanded,
                            onDismissRequest = { downloadModeExpanded = false },
                        ) {
                            DownloadMode.entries.forEach {
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
                    AsWarringTip(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        enabledPadding = false
                    ) {
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
            }
        }
    })
}


@Composable
fun AnalysisVideoCard(
    asLinkResultType: ASLinkResultType,
    isBILILogin: Boolean,
    goToUser: (Long) -> Unit
) {
    when (asLinkResultType) {
        is ASLinkResultType.BILI.Video -> {
            BILIVideoCard(asLinkResultType.viewInfo, isBILILogin)
        }

        is ASLinkResultType.BILI.User -> {
            BILIUserSpaceCard(asLinkResultType.userInfo, goToUser)
        }

        is ASLinkResultType.BILI.Donghua -> {
            BILIDonghuaCard(
                asLinkResultType.donghuaViewInfo,
                asLinkResultType.currentEpId,
                isBILILogin
            )
        }
    }
}

@Composable
fun BILIDonghuaCard(
    donghuaViewInfo: NetWorkResult<BILIDonghuaSeasonInfo?>,
    currentEpId: Long,
    isBILILogin: Boolean
) {
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
                                Text("番剧", Modifier.padding(5.dp))
                            }

                            ExtendedFloatingActionButton(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(5.dp),
                                onClick = {},
                                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                                containerColor = Color(0xffD8FFC0)
                            ) {
                                Icon(Icons.Outlined.Image, contentDescription = "下载封面")
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("下载封面")
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            episodeInfo?.longTitle?.ifEmpty { episodeInfo.title } ?: "视频标题",
                            fontSize = 22.sp,
                            modifier = Modifier
                                .animateContentSize()
                                .shimmer(donghuaViewInfo.status != ApiStatus.SUCCESS),
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                if (!isBILILogin) {
                    AsWarringTip(
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
                            IconButton(onClick = {}) {
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

@Composable
fun BILIVideoCard(videoInfo: NetWorkResult<BILIVideoViewInfo?>, isBILILogin: Boolean) {
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
                                "${
                                    videoInfo.data?.pic?.toHttps()
                                }@672w_378h_1c.avif",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shimmer(videoInfo.status == ApiStatus.LOADING),
                                contentDescription = "视频封面",
                                shape = CardDefaults.shape
                            )

                            ExtendedFloatingActionButton(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(5.dp),
                                onClick = {},
                                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                                containerColor = Color(0xffD8FFC0)
                            ) {
                                Icon(Icons.Outlined.Image, contentDescription = "下载封面")
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("下载封面")
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            videoInfo.data?.title ?: "视频标题",
                            fontSize = 22.sp,
                            modifier = Modifier.animateContentSize()
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                if (!isBILILogin) {
                    AsWarringTip(
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
                            IconButton(onClick = {}) {
                                Icon(Icons.Outlined.NorthEast, contentDescription = "去登录")
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScaffold(
    asResultType: ASLinkResultType?,
    downloadInfo: DownloadViewInfo?,
    onToBack: () -> Unit,
    onDownload:()->Unit,
    content: @Composable (PaddingValues) -> Unit
) {

    var showDownloadInfo by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text("解析视频")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            onToBack.invoke()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    },
                    actions = {}
                )
            }
        },
        floatingActionButton = {
            val visible = when (asResultType) {
                is ASLinkResultType.BILI.Donghua -> {
                    asResultType.donghuaViewInfo.status == ApiStatus.SUCCESS
                }

                is ASLinkResultType.BILI.Video -> {
                    asResultType.viewInfo.status == ApiStatus.SUCCESS
                }

                is ASLinkResultType.BILI.User, null -> false
            }

            AnimatedVisibility(
                visible = visible,
            ) {
                FloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = {
                        onDownload()
                    },
                ) {
                    Icon(Icons.Outlined.Download, "下载视频")
                }
            }
        }
    ) {
        content.invoke(it)
    }

    // 内测代码，后期移除
    if (showDownloadInfo) {
        AlertDialog(
            onDismissRequest = { showDownloadInfo = false },
            title = { Text(text = "下载预览") },
            text = {
                LazyColumn(
                    Modifier
                        .sizeIn(maxHeight = 300.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        Text("优先视频质量：${convertVideoQualityIdValue(downloadInfo?.selectVideoQualityId ?: 0)}")
                    }
                    item {
                        Text("优先视频编码：${downloadInfo?.selectVideoCode}")
                    }
                    item {
                        Text("优先音频质量：${convertAudioQualityIdValue(downloadInfo?.selectAudioQualityId ?: 0)}")
                    }
                    item {
                        Text(
                            "总选择缓存数量：${
                                when (asResultType) {
                                    is ASLinkResultType.BILI.Donghua -> downloadInfo?.selectedEpId?.size
                                    is ASLinkResultType.BILI.Video -> downloadInfo?.selectedCid?.size
                                    else -> {}
                                }
                            }"
                        )
                    }
                    item {
                        Text("选择缓存剧集ID：")
                    }

                    when (asResultType) {
                        is ASLinkResultType.BILI.Donghua -> downloadInfo?.selectedEpId?.forEach {
                            item {
                                Text("$it")
                            }
                        }

                        is ASLinkResultType.BILI.Video -> downloadInfo?.selectedCid?.forEach {
                            item {
                                Text("$it")
                            }
                        }

                        else -> {}
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDownloadInfo = false }) {
                    Text(text = "确认")
                }
            }
        )
    }

}

@Preview
@Composable
fun AsDownloadVideoBottomDialog() {

}