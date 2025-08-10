package com.imcys.bilibilias.ui.setting

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.AirplaneTicket
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material.icons.outlined.EmojiObjects
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.imcys.bilibilias.R
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.BaseSettingsItem
import com.imcys.bilibilias.ui.weight.CategorySettingsItem
import com.imcys.bilibilias.ui.weight.SwitchSettingsItem
import com.imcys.bilibilias.weight.dialog.PermissionRequestTipDialog


@Preview
@Composable
fun SettingScreenPreview() {
    SettingScreen(
        onToRoam = {},
        onToBack = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onToRoam: () -> Unit, onToBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    SettingScaffold(scrollBehavior, onToBack) {

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {

            item {
                CategorySettingsItem(
                    text = "缓存配置"
                )
            }
//            item {
//                SwitchSettingsItem(
//                    imageVector = Icons.Outlined.EnergySavingsLeaf,
//                    text = "省电模式",
//                    description = "开启后将不使用FFmpeg进行视频处理，改用原生API处理。",
//                    checked = false,
//                ) {
//
//                }
//            }
//
//            item {
//                SwitchSettingsItem(
//                    painter = rememberVectorPainter(Icons.Outlined.AudioFile),
//                    text = "音频转码",
//                    description = "启用选择仅音频缓存可以得到mp3的音频文件",
//                    checked = false,
//                ) {
//                }
//            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.DriveFileRenameOutline),
                    text = "命名规则",
                    descriptionText = "使用自定义规则进行视频命名",
                    onClick = {
                    }
                )
            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.Save),
                    text = "缓存目录",
                    descriptionText = "Download/BILIBILIAS",
                    onClick = {
                    }
                )
            }

            item {
                CategorySettingsItem(
                    text = "权限设置"
                )
            }

            item {
                DownloadPostNotifications()
            }

            item {
                CategorySettingsItem(
                    text = "解析配置"
                )
            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.AirplaneTicket),
                    text = "漫游服务",
                    descriptionText = "可使视频解析流量出国",
                    onClick = onToRoam
                )
            }

            item {
                CategorySettingsItem(
                    text = "关于程序"
                )
            }


            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.EmojiObjects),
                    text = "我们的立场",
                    descriptionText = "作为依赖平台的程序，我们有责任和义务维护平台生态的健康发展",
                    onClick = onToRoam
                )
            }

            item {
                BaseSettingsItem(
                    painter =  painterResource(R.drawable.ic_licens_24px),
                    text = "第三方开源许可",
                    description = {},
                    onClick = onToRoam
                )
            }

            item {
                BaseSettingsItem(
                    painter = painterResource(R.drawable.ic_github_24px),
                    text = "Github仓库",
                    description = {},
                    onClick = onToRoam
                )
            }

        }
    }
}

@Composable
fun DownloadPostNotifications() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val context = LocalContext.current

        // 检查权限状态的函数
        val checkPermissionStatus = {
            ContextCompat.checkSelfPermission(
                context,
                permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        var hasForegroundServicePermission by remember {
            mutableStateOf(checkPermissionStatus())
        }

        var showRequestForegroundServiceTip by remember { mutableStateOf(false) }

        SwitchSettingsItem(
            imageVector = Icons.Outlined.Notifications,
            text = "前台通知",
            description = "开启后可以使得在后台的下载任务不会被系统回收",
            checked = hasForegroundServicePermission,
        ) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                showRequestForegroundServiceTip = true
            }
        }

        if (showRequestForegroundServiceTip){
            DownloadServicePermissionRequestTipDialog(
                onDismiss = {
                    showRequestForegroundServiceTip = false
                },
                onRequest = {
                    showRequestForegroundServiceTip = false
                    hasForegroundServicePermission = true
                },
                onPermissionCheckUpdate = { hasPermission ->
                    hasForegroundServicePermission = hasPermission
                }
            )
        }
    }
}

// 前台服务权限申请对话框
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DownloadServicePermissionRequestTipDialog(
    onDismiss: () -> Unit,
    onRequest: () -> Unit,
    onPermissionCheckUpdate: (Boolean) -> Unit
) {
    val context = LocalContext.current

    // 检查权限状态的函数
    val checkPermissionStatus = {
        ContextCompat.checkSelfPermission(
            context,
            permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 启动设置页面的launcher
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // 从设置页面返回时重新检查权限状态
        val hasPermission = checkPermissionStatus()
        onPermissionCheckUpdate(hasPermission)
        if (hasPermission) {
            onRequest()
        } else {
            onDismiss()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            onRequest()
        } else {
            // 跳转APP通知权限设置
            val intent = Intent().apply {
                action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = android.net.Uri.fromParts("package", context.packageName, null)
            }
            settingsLauncher.launch(intent)
        }
    }
    PermissionRequestTipDialog(
        show = true,
        message = "为了我们可以在后台缓存较长视频，接下来将向您申请通知服务权限。",
        onConfirm = {
            launcher.launch(permission.POST_NOTIFICATIONS)
        },
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                scrollBehavior = scrollBehavior,
                style = BILIBILIASTopAppBarStyle.Large,
                title = { Text(text = "设置") },
                navigationIcon = {
                    IconButton(onClick = {
                        onToBack.invoke()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "返回",
                        )
                    }
                }
            )
        },
    ) {
        content(it)
    }

}