package com.imcys.bilibilias.ui.user

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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.ui.user.navigation.UserRoute
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsCardGroups
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.SurfaceColorCard


@Preview
@Composable
@PreviewScreenSizes
fun UserScreenPreview() {
    UserScreen(UserRoute(), {})
}

@Composable
internal fun UserScreen(userRoute: UserRoute, onToBack: () -> Unit) {
    UserScaffold(onToBack) {
        LazyVerticalGrid(
            GridCells.Fixed(2),
            Modifier
                .fillMaxWidth()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            fullWidthItem { TopUserInfo() }
            fullWidthItem {
                PlatformList()
            }
            fullWidthItem { ActionRow() }
            fullWidthItem { VideoHeader() }

        }
    }
}

@Composable
private fun VideoCard() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)

    ) {
        Surface(
            Modifier
                .weight(0.4f)
                .aspectRatio(16f / 9f),
            color = MaterialTheme.colorScheme.primary,
            shape = CardDefaults.shape
        ) {
            ASAsyncImage(
                "",
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
            Text("视频标题",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            Row {
                Text("1.6万次播放", fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
private fun VideoHeader() {
    SurfaceColorCard {
        Column(
            Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
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

                IconButton(onClick = {}) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = "更多投稿",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VideoCard()
                VideoCard()
            }

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
fun ActionRow() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = CardDefaults.shape,
            color = MaterialTheme.colorScheme.primaryContainer
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
            color = MaterialTheme.colorScheme.primaryContainer
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
            color = MaterialTheme.colorScheme.primaryContainer
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
            color = MaterialTheme.colorScheme.primaryContainer
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
fun PlatformList() {
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

                Row {
                    Icon(
                        painterResource(R.drawable.ic_mini_bili_logo_24px),
                        contentDescription = "B站Logo",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "萌新杰少",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(8.dp))
                    ASAsyncImage(
                        "",
                        modifier = Modifier.size(24.dp),
                        contentDescription = "关联账户头像"
                    )
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
fun UserDataInfo() {
    AsCardGroups(
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
                    "0",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
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
                    "0",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
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
                    "0",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
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
                    "0",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text("播放", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopUserInfo() {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = MaterialShapes.Circle.toShape()) {
                ASAsyncImage(
                    "",
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
                        "萌新杰少",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                            text = "Lv.3",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400,
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "个性签名",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        UserDataInfo()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScaffold(onToBack: () -> Unit, content: @Composable (PaddingValues) -> Unit) {
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
                        IconButton(onClick = {
                            onToBack.invoke()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onToBack.invoke()
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