package com.imcys.bilibilias.ui.user

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.common.event.sendAnalysisEvent
import com.imcys.bilibilias.common.utils.NumberUtils
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.BILISpaceArchiveModel
import com.imcys.bilibilias.data.model.BILIUserStatModel
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.ui.user.navigation.UserRoute
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASCardGroups
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.SurfaceColorCard
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.weight.AsAutoError
import org.koin.androidx.compose.koinViewModel


@Composable
internal fun UserScreen(
    userRoute: UserRoute,
    onToBack: () -> Unit,
    onToSettings: () -> Unit,
    onToWorkList: (mid: Long) -> Unit,
    onToBangumiFollow: (mid: Long) -> Unit,
    onToUserFolder: (mid: Long) -> Unit,
    onToLikeVideo: (mid: Long) -> Unit,
    onToCoinVide: (mid: Long) -> Unit,
) {
    val vm = koinViewModel<UserViewModel>()
    val pageInfoState by vm.userPageInfoState.collectAsState()
    val userStatInfoState by vm.userStatInfoState.collectAsState()
    val spaceArchiveInfoState by vm.spaceArchiveInfoState.collectAsState()
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(userRoute.mid) {
        vm.getUserPageIno(userRoute.mid)
    }



    UserScaffold(onToBack, onToSettings) {
        LazyVerticalGrid(
            GridCells.Fixed(2),
            Modifier
                .fillMaxWidth()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            fullWidthItem {
                TopUserInfo(pageInfoState, userStatInfoState) {
                    vm.getUserPageIno(userRoute.mid)
                }
            }

            if (!userRoute.isAnalysisUser) {
                fullWidthItem {
                    PlatformList(uiState.biliUsersEntity)
                }
                fullWidthItem {
                    ActionRow(
                        onToBangumiFollow = {
                            onToBangumiFollow.invoke(userRoute.mid)
                        }, onToUserFolder = {
                            onToUserFolder.invoke(userRoute.mid)
                        }, onToLikeVideo = {
                            onToLikeVideo.invoke(userRoute.mid)
                        }, onToCoinVide = {
                            onToCoinVide.invoke(userRoute.mid)
                        })
                }
            }

            fullWidthItem {
                VideoHeader(spaceArchiveInfoState, onRetry = {
                    vm.getUserPageIno(userRoute.mid)
                }, onToWorkList = {
                    onToWorkList.invoke(userRoute.mid)
                })
            }

        }
    }


}

@Composable
private fun VideoCard(item: BILISpaceArchiveModel.Item?, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable {
                onClick.invoke()
            }
    ) {
        Surface(
            Modifier
                .weight(0.4f)
                .aspectRatio(16f / 9f)
                .shimmer(item == null),
            shape = CardDefaults.shape
        ) {
            ASAsyncImage(
                "${item?.pic?.toHttps()}@672w_378h_1c",
                modifier = Modifier.fillMaxSize(),
                contentDescription = "视频封面"
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(
            Modifier
                .fillMaxHeight()
                .weight(0.6f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                item?.title ?: "",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .animateContentSize()
                    .shimmer(item == null),
            )
            Row {
                Text(
                    "${NumberUtils.formatLargeNumber(item?.play)}次播放",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.shimmer(item == null),
                )
            }
        }
    }
}

@Composable
private fun VideoHeader(
    spaceArchiveInfoState: NetWorkResult<BILISpaceArchiveModel?>,
    onRetry: () -> Unit = {},
    onToWorkList: () -> Unit = {}
) {
    SurfaceColorCard {
        Column(
            Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Movie,
                    contentDescription = "投稿视频",
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(Modifier.width(8.dp))
                Text("投稿视频", color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.weight(1f))

                ASIconButton(onClick = { onToWorkList.invoke() }) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = "更多投稿",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            AsAutoError(
                netWorkResult = spaceArchiveInfoState,
                onSuccessContent = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        spaceArchiveInfoState.data?.list?.forEach {
                            VideoCard(it, onClick = {
                                sendAnalysisEvent(AnalysisEvent(it.bvid))
                            })
                        }
                    }
                },
                onLoadingContent = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        VideoCard(null)
                    }
                },
                onRetry = onRetry
            )
        }


    }
}


private fun LazyGridScope.fullWidthItem(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(
        key = key,
        span = { GridItemSpan(2) },
        contentType = contentType,
        content = content
    )
}

@Composable
fun ActionRow(
    onToBangumiFollow: () -> Unit = {},
    onToUserFolder: () -> Unit = {},
    onToLikeVideo: () -> Unit,
    onToCoinVide: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = CardDefaults.shape,
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = onToLikeVideo
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.ThumbUp, contentDescription = "最近点赞")
                Spacer(Modifier.height(4.dp))
                Text("已点赞", fontSize = 14.sp)
            }
        }

        Surface(
            modifier = Modifier.weight(1f),
            shape = CardDefaults.shape,
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = onToCoinVide
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.Paid, contentDescription = "最近投币")
                Spacer(Modifier.height(4.dp))
                Text("已投币", fontSize = 14.sp)
            }
        }

        Surface(
            modifier = Modifier.weight(1f),
            shape = CardDefaults.shape,
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = onToUserFolder
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.Star, contentDescription = "收藏")
                Spacer(Modifier.height(4.dp))
                Text("收藏", fontSize = 14.sp)
            }
        }

        Surface(
            modifier = Modifier.weight(1f),
            shape = CardDefaults.shape,
            color = MaterialTheme.colorScheme.primaryContainer,
            onClick = onToBangumiFollow
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.Subscriptions, contentDescription = "追番")
                Spacer(Modifier.height(4.dp))
                Text("追番", fontSize = 14.sp)
            }
        }

    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlatformList(biliUsersEntity: BILIUsersEntity?) {
    Column {
        Spacer(Modifier.height(16.dp))
        SurfaceColorCard(
            Modifier
                .fillMaxWidth(),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Link,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = "关联平台"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("已关联账户", color = MaterialTheme.colorScheme.outline)

                }
                Spacer(Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.ic_mini_bili_logo_24px),
                        contentDescription = "B站Logo",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "${biliUsersEntity?.name}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.animateContentSize()
                    )
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialShapes.Circle.toShape(),
                    ) {
                        AsyncImage(
                            "${biliUsersEntity?.face}",
                            modifier = Modifier.size(24.dp),
                            contentDescription = "关联账户头像",
                        )
                    }
                }

                HorizontalDivider(
                    Modifier.padding(vertical = 16.dp),
                    DividerDefaults.Thickness,
                    MaterialTheme.colorScheme.outlineVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.ic_mini_acfun_logo_24px),
                        contentDescription = "AcFunLogo",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "暂未开放",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Outlined.Build,
                        modifier = Modifier.size(20.dp),
                        contentDescription = "维护图标",
                        tint = MaterialTheme.colorScheme.outline
                    )

                }

            }
        }
    }
}

@Composable
fun UserDataInfo(userStatInfoState: BILIUserStatModel) {
    ASCardGroups(
        Modifier
            .fillMaxWidth(),
    ) {
        item {
            Column(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    NumberUtils.formatLargeNumber(userStatInfoState.biliUserRelationStatInfo.data?.following),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.shimmer(
                        userStatInfoState.biliUserRelationStatInfo.status == ApiStatus.LOADING
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text("关注", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            }
        }

        item {
            Column(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    NumberUtils.formatLargeNumber(userStatInfoState.biliUserRelationStatInfo.data?.follower),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.shimmer(
                        userStatInfoState.biliUserRelationStatInfo.status == ApiStatus.LOADING
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text("粉丝", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            }
        }

        item {
            Column(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    NumberUtils.formatLargeNumber(userStatInfoState.biliUserSpaceUpStat.data?.likes),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.shimmer(
                        userStatInfoState.biliUserSpaceUpStat.status == ApiStatus.LOADING
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text("获赞", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            }
        }
        item {
            Column(
                modifier = Modifier.padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    NumberUtils.formatLargeNumber(userStatInfoState.biliUserSpaceUpStat.data?.archive?.view),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.shimmer(
                        userStatInfoState.biliUserSpaceUpStat.status == ApiStatus.LOADING
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text("播放", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopUserInfo(
    pageInfoState: NetWorkResult<BILIUserSpaceAccInfo?>,
    userStatInfoState: BILIUserStatModel,
    onRetry: () -> Unit = {},
) {
    Column {
        AsAutoError(
            pageInfoState,
            onSuccessContent = {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = MaterialShapes.Circle.toShape(),
                        modifier = Modifier.shimmer(
                            visible = pageInfoState.status == ApiStatus.LOADING
                        )
                    ) {
                        ASAsyncImage(
                            pageInfoState.data?.face ?: "",
                            contentDescription = "头像",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                pageInfoState.data?.name ?: "用户名",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f, fill = false)
                                    .shimmer(
                                        visible = pageInfoState.status == ApiStatus.LOADING
                                    )
                            )
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.shimmer(
                                    visible = pageInfoState.status == ApiStatus.LOADING
                                )
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                                    text = "LV ${pageInfoState.data?.level}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W400,
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            pageInfoState.data?.sign ?: "个性签名",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .animateContentSize()
                                .shimmer(
                                    visible = pageInfoState.status == ApiStatus.LOADING
                                )
                        )
                    }
                }
            },
            onRetry = onRetry
        )
        Spacer(Modifier.height(32.dp))
        UserDataInfo(userStatInfoState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScaffold(
    onToBack: () -> Unit,
    onToSettings: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {},
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
                            onToSettings.invoke()
                        }) {
                            Icon(
                                Icons.Outlined.Settings,
                                contentDescription = "设置"
                            )
                        }
                    }
                )
            }
        },
    ) {
        content.invoke(it)
    }
}