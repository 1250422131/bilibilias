package com.imcys.bilibilias.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.VideoCameraBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.utils.ASConstant.QQ_CHANNEL_URL
import com.imcys.bilibilias.common.utils.ASConstant.QQ_GROUP_URL
import com.imcys.bilibilias.common.utils.openLink
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.tools.donate.DonateRoute
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorRoute
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASTextButton

@Composable
fun ToolsScreen(vm: HomeViewModel, onToPage: (NavKey) -> Unit) {
    Column(
        Modifier
            .padding(horizontal = 15.dp)
            .padding(top = 10.dp),
    ) {
        ToolsContent(vm, onToPage)
    }
}


data class ToolInfo(
    val name: String,
    val desc: String,
    val icon: ImageVector? = null,
    val iconRes: Int? = null,
    val navKey: NavKey = HomeRoute(),
    val onClick: (() -> Unit)? = null,
)

@Composable
private fun ToolsContent(vm: HomeViewModel, onToPage: (NavKey) -> Unit) {

    var showFeedbackDialog by remember { mutableStateOf(false) }

    val videoTools = listOf(
        ToolInfo(
            name = "逐帧提取",
            desc = "从视频中逐帧提取图片，画手书的好帮手！",
            icon = Icons.Outlined.VideoCameraBack,
            navKey = FrameExtractorRoute
        )
    )
    val otherTools = mutableListOf(
        ToolInfo(
            name = "反馈问题",
            desc = "🐞帮助我们改进程序，这对本项目的发展有重大意义！",
            icon = Icons.Outlined.BugReport,
            onClick = { showFeedbackDialog = true }
        )
    ).apply {
        if (!BuildConfig.ENABLE_PLAY_APP_MODE) {
            add(
                ToolInfo(
                    name = "捐助我们",
                    desc = "☕请我们喝一杯奶茶吧！",
                    iconRes = R.drawable.ic_credit_card_heart_24px,
                    navKey = DonateRoute
                )
            )
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = Modifier.padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Text("视频处理")
        }
        items(videoTools) {
            ToolCard(it, onClick = {
                onToPage.invoke(it.navKey)
            })
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Text("其他")
        }

        items(otherTools) {
            ToolCard(it, onClick = {
                onToPage.invoke(it.navKey)
            })
        }

    }

    FeedbackDialog(showFeedbackDialog, onDismiss = {
        showFeedbackDialog = false
    })

}

@Composable
fun FeedbackDialog(showFeedbackDialog: Boolean, onDismiss: () -> Unit) {

    val context = LocalContext.current

    ASAlertDialog(
        showState = showFeedbackDialog,
        title = {
            Text("问题反馈")
        },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Spacer(Modifier)

                BadgedBox(badge = {
                    Badge { Text("推荐") }
                }) {
                    Surface(
                        shape = CardDefaults.shape,
                        onClick = {
                            context.openLink("https://github.com/1250422131/bilibilias/issues")
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_github_24px),
                                contentDescription = "图标",
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "前往Github反馈，由开发者和社区贡献者处理你的问题。",
                            )
                        }
                    }
                }

                if (!BuildConfig.ENABLE_PLAY_APP_MODE) {
                    Surface(
                        shape = CardDefaults.shape,
                        onClick = {
                            context.openLink(QQ_CHANNEL_URL)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_qq_channel_2px),
                                contentDescription = "图标",
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "前往QQ频道反馈，由用户和开发者解答你的问题，并获得最新的通知。",
                            )
                        }
                    }


                    Surface(
                        shape = CardDefaults.shape,
                        onClick = {
                            context.openLink(QQ_GROUP_URL)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_qq_24px),
                                contentDescription = "图标",
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "二次元爱好者交流群，欢迎加入讨论与交流！",
                            )
                        }
                    }
                }


            }
        },
        onDismiss = onDismiss,
        confirmButton = {
            ASTextButton(onClick = {
                onDismiss.invoke()
            }) {
                Text("好的")
            }
        }

    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun ToolCard(
    toolInfo: ToolInfo = ToolInfo(
        name = "逐帧提取",
        desc = "从视频中逐帧提取图片，画手书的好帮手！",
        icon = Icons.Outlined.VideoCameraBack,
        navKey = FrameExtractorRoute
    ),
    onClick: () -> Unit = { }
) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = CardDefaults.shape, onClick = {
        if (toolInfo.onClick != null) toolInfo.onClick.invoke() else onClick.invoke()
    }) {
        Column(
            Modifier
                .padding(10.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialShapes.Circle.toShape()
            ) {
                toolInfo.icon?.let {
                    Icon(
                        it,
                        contentDescription = "图标",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                    )
                } ?: run {
                    Icon(
                        painter = painterResource(toolInfo.iconRes!!),
                        contentDescription = "图标",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                    )
                }

            }
            Spacer(Modifier.height(2.dp))
            Text(toolInfo.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(
                toolInfo.desc,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                minLines = 2,
            )
        }
    }
}