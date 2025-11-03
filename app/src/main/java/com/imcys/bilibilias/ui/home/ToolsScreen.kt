package com.imcys.bilibilias.ui.home


import androidx.compose.ui.res.stringResource
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.outlined.NorthEast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.utils.ASConstant.QQ_CHANNEL_URL
import com.imcys.bilibilias.common.utils.ASConstant.QQ_GROUP_URL
import com.imcys.bilibilias.common.utils.DeviceInfoUtils
import com.imcys.bilibilias.common.utils.openLink
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.tools.donate.DonateRoute
import com.imcys.bilibilias.ui.tools.frame.FrameExtractorRoute
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.tip.ASInfoTip
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import kotlinx.coroutines.launch

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
            name = stringResource(R.string.tools_frame_extraction),
            desc = stringResource(R.string.tools_video),
            icon = Icons.Outlined.VideoCameraBack,
            navKey = FrameExtractorRoute
        )
    )
    val otherTools = mutableListOf(
        ToolInfo(
            name = stringResource(R.string.tools_feedback),
            desc = stringResource(R.string.tools_help_improve),
            icon = Icons.Outlined.BugReport,
            onClick = { showFeedbackDialog = true }
        )
    ).apply {
        if (!BuildConfig.ENABLED_PLAY_APP_MODE) {
            add(
                ToolInfo(
                    name = stringResource(R.string.tools_donate),
                    desc = stringResource(R.string.tools_buy_us_coffee),
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
            Text(stringResource(R.string.tools_video_1))
        }
        items(videoTools) {
            ToolCard(it, onClick = {
                onToPage.invoke(it.navKey)
            })
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(stringResource(R.string.tools_other))
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
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    ASAlertDialog(
        showState = showFeedbackDialog,
        title = {
            Text(stringResource(R.string.tools_feedback_2))
        },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Spacer(Modifier)

                ASInfoTip {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.tools_copy),
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        ASIconButton(onClick = {
                            scope.launch {
                                val copyText = DeviceInfoUtils.getDeviceInfoCopyString(context)
                                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                                val clipboard =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText(stringResource(R.string.tools_version_info), copyText)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, stringResource(R.string.tools_copied_to_clipboard), Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Outlined.CopyAll, contentDescription = stringResource(R.string.tools_copy_1))
                        }
                    }
                }

                BadgedBox(badge = {
                    Badge { Text(stringResource(R.string.tools_recommend)) }
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
                                contentDescription = stringResource(R.string.tools_icon),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                stringResource(R.string.tools_feedback_1),
                            )
                        }
                    }
                }

                if (!BuildConfig.ENABLED_PLAY_APP_MODE) {
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
                                contentDescription = stringResource(R.string.tools_icon),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                stringResource(R.string.tools_user),
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
                                contentDescription = stringResource(R.string.tools_icon),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                stringResource(R.string.tools_group),
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
                Text(stringResource(R.string.tools_ok))
            }
        }

    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun ToolCard(
    toolInfo: ToolInfo = ToolInfo(
        name = stringResource(R.string.tools_frame_extraction),
        desc = stringResource(R.string.tools_video),
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
                        contentDescription = stringResource(R.string.tools_icon),
                        modifier = Modifier
                            .padding(8.dp)
                            .size(22.dp)
                    )
                } ?: run {
                    Icon(
                        painter = painterResource(toolInfo.iconRes!!),
                        contentDescription = stringResource(R.string.tools_icon),
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