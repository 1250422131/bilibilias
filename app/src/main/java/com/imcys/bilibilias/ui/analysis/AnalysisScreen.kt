package com.imcys.bilibilias.ui.analysis

import androidx.compose.animation.AnimatedContentScope
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
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


    AnalysisScaffold(onToBack) {
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
                AnalysisVideoCardList(type, uiState.isBILILogin, goToUser)
            }

        }


    }
}

@Composable
fun ColumnScope.AnalysisVideoCardList(
    asLinkResultType: ASLinkResultType,
    isBILILogin: Boolean,
    goToUser: (Long) -> Unit
) {
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
    }
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
                                    videoInfo.data?.pic?.replace(
                                        "http://",
                                        "https://"
                                    )
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
fun AnalysisScaffold(onToBack: () -> Unit, content: @Composable (PaddingValues) -> Unit) {
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
            FloatingActionButton(
                modifier = Modifier.imePadding(),
                onClick = { },
            ) {
                Icon(Icons.Outlined.Download, "下载视频")
            }
        }
    ) {
        content.invoke(it)
    }
}