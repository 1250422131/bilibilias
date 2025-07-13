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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.NorthEast
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
import androidx.compose.material3.FilterChip
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
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurls
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoSupportFormat
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
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
import kotlin.math.ceil

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


    AnalysisScaffold(uiState.asLinkResultType, onToBack) {
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
                AnalysisVideoCardList(type, uiState.isBILILogin, vm, goToUser)
            }

        }


    }
}

@Composable
fun ColumnScope.AnalysisVideoCardList(
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
                        donghuaPlayerInfo,
                        asLinkResultType.currentEpId,
                        asLinkResultType.donghuaViewInfo
                    ) {
                        viewModel.updateSelectSeason(it)
                    }
                }

                is ASLinkResultType.BILI.Video -> {
                    VideoDownloadScreen(
                        videoPlayerInfo,
                        asLinkResultType.currentBvId,
                        asLinkResultType.viewInfo
                    )
                }

                else -> {}
            }
        }
    }

}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun VideoDownloadScreen(
    videoPlayerInfo: NetWorkResult<BILIVideoPlayerInfo?>,
    currentBvId: String,
    viewInfo: NetWorkResult<BILIVideoViewInfo?>
) {

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
            Text("缓存倾向")
            AsAutoError(videoPlayerInfo, onSuccessContent = {
                Column {
                    VideoSupportFormatsSelectScreen(
                        Modifier
                            .fillMaxWidth()
                            .shimmer(videoPlayerInfo.status != ApiStatus.SUCCESS),
                        videoPlayerInfo.data?.supportFormats,
                        videoPlayerInfo.data?.dash?.video,
                        videoPlayerInfo.data?.durls,
                    )

                    Spacer(Modifier.height(6.dp))
                    AudioQualitySelectScreen(
                        Modifier.fillMaxWidth(),
                        videoPlayerInfo.data?.dash?.audio
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
                                                selectSectionId = info.id
                                            }
                                        },
                                    ) {
                                        Text(info.title)
                                    }
                                }
                            }

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
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
                                Surface(
                                    shape = CardDefaults.shape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                ) {
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
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
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
                                Surface(
                                    shape = CardDefaults.shape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                ) {
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
                            }
                        }
                    }
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DongmhuaDownloadScreen(
    donghuaPlayerInfo: NetWorkResult<BILIDonghuaPlayerInfo?>,
    currentEpId: Long,
    donghuaViewInfo: NetWorkResult<BILIDonghuaSeasonInfo?>,
    onSelectSeason: (Long) -> Unit
) {
    var selectSeasonsId by remember {
        mutableStateOf<Long?>(null)
    }
    var selectSectionId by remember {
        mutableStateOf<Long?>(null)
    }
    var currentEpListIndex by remember { mutableIntStateOf(0) }


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
            Text("缓存倾向")
            AsAutoError(donghuaPlayerInfo, onSuccessContent = {
                Column {
                    VideoSupportFormatsSelectScreen(
                        Modifier
                            .fillMaxWidth()
                            .shimmer(donghuaPlayerInfo.status != ApiStatus.SUCCESS),
                        donghuaPlayerInfo.data?.supportFormats,
                        donghuaPlayerInfo.data?.dash?.video,
                        donghuaPlayerInfo.data?.durls,
                    )

                    Spacer(Modifier.height(6.dp))
                    AudioQualitySelectScreen(
                        Modifier.fillMaxWidth(),
                        donghuaPlayerInfo.data?.dash?.audio
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
                                                    selectSeasonsId = info.seasonId
                                                    onSelectSeason(info.seasonId)
                                                }

                                            },
                                        ) {
                                            Text(info.seasonTitle)
                                        }
                                    }
                                }

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
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
                                items(
                                    episodeList.subList(startIndex, endIndex)
                                ) {
                                    Surface(
                                        shape = CardDefaults.shape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                    ) {
                                        Column(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(60.dp)
                                                .padding(8.dp),
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
                                                selectSectionId = info.id
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
                                    Surface(
                                        shape = CardDefaults.shape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                    ) {
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
                                }
                            }
                        }
                    }
                })

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AudioQualitySelectScreen(
    modifier: Modifier,
    audioList: List<BILIVideoDash.Audio>?,
) {
    var modelExpanded by remember { mutableStateOf(false) }
    var selectValue: String by remember { mutableStateOf("") }

    LaunchedEffect(audioList) {
        selectValue = audioList?.firstOrNull()?.id?.toString() ?: ""
    }

    if (audioList != null) {
        ExposedDropdownMenuBox(
            expanded = modelExpanded,
            onExpandedChange = {
                modelExpanded = it
            },
            modifier = modifier,
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 12.sp
                ),
                value = selectValue,
                onValueChange = {},
                readOnly = true,
                singleLine = false,
                label = { Text("选择优先音频质量", fontSize = 12.sp) },
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
                expanded = modelExpanded,
                onDismissRequest = { modelExpanded = false },
            ) {
                audioList.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                it.id.toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            modelExpanded = false
                            selectValue = it.id.toString()
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoSupportFormatsSelectScreen(
    modifier: Modifier,
    mSupportFormats: List<BILIVideoSupportFormat>?,
    dashVideoList: List<BILIVideoDash.Video>?,
    durlVideoList: List<BILIVideoDurls>?,
) {
    var videoModelExpanded by remember { mutableStateOf(false) }
    var videoCodeModelExpanded by remember { mutableStateOf(false) }
    var selectVideoFormatValue: String by remember { mutableStateOf("") }
    var selectVideoCodeValue: String by remember { mutableStateOf("") }

    var supportFormats by remember { mutableStateOf(listOf<BILIVideoSupportFormat>()) }
    var videoCodingList by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(mSupportFormats, dashVideoList, durlVideoList) {
        supportFormats = if (dashVideoList != null) {
            // Dash模式


            val mVideoCodingList = mutableSetOf<String>()
            mSupportFormats?.forEach {
                it.codecs.forEach { code ->
                    mVideoCodingList.add(code.split(".")[0])
                }
            }
            videoCodingList = mVideoCodingList
            selectVideoCodeValue = mVideoCodingList.firstOrNull() ?: ""

            mSupportFormats?.filter { supportFormat ->
                dashVideoList.any { item -> item.id == supportFormat.quality }
            } ?: emptyList()
        } else {
            // FLV模式
            mSupportFormats?.filter { supportFormat ->
                durlVideoList?.any { item -> item.quality == supportFormat.quality } == true
            } ?: emptyList()
        }

        selectVideoFormatValue = supportFormats.firstOrNull()?.run {
            description.ifBlank { newDescription }
        } ?: ""
    }



    Row(
        modifier.animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = videoModelExpanded,
            onExpandedChange = {
                videoModelExpanded = it
            },
            modifier = Modifier.weight(1f),
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 12.sp
                ),
                value = selectVideoFormatValue,
                onValueChange = {

                },
                readOnly = true,
                singleLine = false,
                label = { Text("选择优先分辨率", fontSize = 12.sp) },
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
                expanded = videoModelExpanded,
                onDismissRequest = { videoModelExpanded = false },
            ) {
                supportFormats.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                it.description.ifBlank { it.newDescription },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            videoModelExpanded = false
                            selectVideoFormatValue = it.description.ifBlank { it.newDescription }
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }

        }

        if (videoCodingList.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = videoCodeModelExpanded,
                onExpandedChange = {
                    videoCodeModelExpanded = it
                },
                modifier = Modifier.weight(1f),
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 12.sp
                    ),
                    value = selectVideoCodeValue,
                    onValueChange = {

                    },
                    readOnly = true,
                    singleLine = false,
                    label = { Text("选择优先编码", fontSize = 12.sp) },
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
                    expanded = videoCodeModelExpanded,
                    onDismissRequest = { videoCodeModelExpanded = false },
                ) {
                    videoCodingList.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                videoModelExpanded = false
                                selectVideoFormatValue = it
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }

            }
        }
    }
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
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
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
            val visible = asResultType != null && when (asResultType) {
                is ASLinkResultType.BILI.Donghua -> {
                    asResultType.donghuaViewInfo.status == ApiStatus.SUCCESS
                }

                is ASLinkResultType.BILI.User -> false
                is ASLinkResultType.BILI.Video -> {
                    asResultType.viewInfo.status == ApiStatus.SUCCESS
                }
            }

            AnimatedVisibility(
                visible = visible,
            ) {
                FloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = {

                    },
                ) {
                    Icon(Icons.Outlined.Download, "下载视频")
                }
            }
        }
    ) {
        content.invoke(it)
    }
}

@Preview
@Composable
fun AsDownloadVideoBottomDialog() {

}