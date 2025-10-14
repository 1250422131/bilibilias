package com.imcys.bilibilias.ui.setting

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.AirplaneTicket
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.R
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.download.DownloadContent
import com.imcys.bilibilias.ui.download.navigation.DownloadRoute
import com.imcys.bilibilias.ui.setting.about.AboutContent
import com.imcys.bilibilias.ui.setting.about.AboutRouter
import com.imcys.bilibilias.ui.setting.layout.LayoutTypesetContent
import com.imcys.bilibilias.ui.setting.layout.LayoutTypesetRoute
import com.imcys.bilibilias.ui.setting.layout.LayoutTypesetViewModel
import com.imcys.bilibilias.ui.setting.navigation.RoamRoute
import com.imcys.bilibilias.ui.setting.storage.StorageManagementContent
import com.imcys.bilibilias.ui.setting.storage.StorageManagementRoute
import com.imcys.bilibilias.ui.setting.version.AppVersionInfoRoute
import com.imcys.bilibilias.ui.setting.version.VersionInfoContent
import com.imcys.bilibilias.ui.utils.rememberWidthSizeClass
import com.imcys.bilibilias.ui.utils.switchHapticFeedback
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.BaseSettingsItem
import com.imcys.bilibilias.ui.weight.CategorySettingsItem
import com.imcys.bilibilias.ui.weight.SwitchSettingsItem
import com.imcys.bilibilias.weight.dialog.PermissionRequestTipDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Preview
@Composable
fun SettingScreenPreview() {
    SettingScreen(
        onToRoam = {},
        onToBack = {},
        onToComplaint = {},
        onToLayoutTypeset = {})
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun SettingScreen(
    onToRoam: () -> Unit,
    onToComplaint: () -> Unit,
    onToLayoutTypeset: () -> Unit,
    onToBack: () -> Unit,
    onToAbout: () -> Unit = {},
    onToVersionInfo: () -> Unit = {},
    onToSystemExpand: () -> Unit = {},
    onToStorageManagement: () -> Unit = {},
    onLogoutFinish: (Long) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val vm = koinViewModel<SettingViewModel>()
    val appSettings by vm.appSettings.collectAsState(initial = AppSettings.getDefaultInstance())
    val haptics = LocalHapticFeedback.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val uiState by vm.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showLogoutLoading by remember { mutableStateOf(false) }
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<NavKey>()
    val windowWidthSizeClass = rememberWidthSizeClass()

    val onBackClick: () -> Unit = {
        if ( windowWidthSizeClass == WindowWidthSizeClass.Compact &&
            scaffoldNavigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail
        ) {
            // 竖屏模式导航回列表页面
            coroutineScope.launch {
                scaffoldNavigator.navigateBack()
            }
        } else {
            // 其他情况（横屏或列表页面），调用外部的 onToBack
            onToBack()
        }
    }


    SettingScaffold(scrollBehavior, onBackClick) {
        val scope = rememberCoroutineScope()
        NavigableListDetailPaneScaffold(
            navigator = scaffoldNavigator,
            listPane = {
                AnimatedPane() {
                    SettingList(vm,{ showLogoutDialog = true },it,scrollBehavior.nestedScrollConnection,
                        onItemClick = { item ->
                            // Navigate to the detail pane with the passed item
                            scope.launch {
                                scaffoldNavigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    item
                                )
                            }
                        },
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    val detailPanePadding = it
                    // Show the detail pane content if selected item is available
                    scaffoldNavigator.currentDestination?.contentKey?.let {
                        SettingDetails(it,scaffoldNavigator,detailPanePadding,scrollBehavior,onBackClick)
                    }
                }
            },
        )
       /* LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        )
        {

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

//            item {
//                BaseSettingsItem(
//                    painter = rememberVectorPainter(Icons.Outlined.DriveFileRenameOutline),
//                    text = "命名规则",
//                    descriptionText = "使用自定义规则进行视频命名",
//                    onClick = {
//                    }
//                )
//            }

            item {
                BaseSettingsItem(
                    painter = painterResource(R.drawable.ic_save_24px),
                    text = "存储管理",
                    descriptionText = "管理APP内存占用",
                    onClick = onToStorageManagement
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
                    text = "主题设置"
                )
            }
            item {
                SwitchSettingsItem(
                    imageVector = Icons.Outlined.Palette,
                    text = "动态主题",
                    description = "使用桌面壁纸颜色作为主题",
                    checked = appSettings.enabledDynamicColor,
                ) { check ->
                    haptics.switchHapticFeedback(check)
                    vm.updateEnabledDynamicColor(check)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                item {
                    CategorySettingsItem(
                        text = "权限设置"
                    )
                }
            }

            item {
                DownloadPostNotifications()
            }


            item {
                CategorySettingsItem(
                    text = "布局配置"
                )
            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ListAlt),
                    text = "首页排版",
                    description = {},
                    onClick = onToLayoutTypeset
                )
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
                    painter = rememberVectorPainter(Icons.Outlined.Group),
                    text = "关于",
                    descriptionText = "作为依赖平台的程序，我们有责任和义务维护平台生态的健康发展！",
                    onClick = onToAbout
                )

            }


            item {
                BaseSettingsItem(
                    painter = painterResource(R.drawable.ic_github_24px),
                    text = "Github仓库",
                    description = {},
                    onClick = {
                        val intent = Intent().apply {
                            action = "android.intent.action.VIEW"
                            data = "https://github.com/1250422131/bilibilias".toUri()
                        }
                        context.startActivity(intent)
                    }
                )
            }

//            item {
//                BaseSettingsItem(
//                    painter = painterResource(R.drawable.ic_licens_24px),
//                    text = "第三方开源许可",
//                    description = {},
//                    onClick = {
//
//                    }
//                )
//            }

//            item {
//                CategorySettingsItem(
//                    text = "投诉与反馈"
//                )
//            }
//
//            item {
//                BaseSettingsItem(
//                    painter = rememberVectorPainter(Icons.Outlined.MoodBad),
//                    text = "投诉",
//                    descriptionText = "向BILIBILIAS投诉违规行为",
//                    onClick = onToComplaint
//                )
//            }


            if (uiState.isLogin) {

                item {
                    CategorySettingsItem(
                        text = "账户"
                    )
                }

                item {
                    BaseSettingsItem(
                        painter = rememberVectorPainter(Icons.Outlined.Android),
                        text = "设备信息",
                        descriptionText = "提交反馈时记得带上这个！",
                        onClick = onToVersionInfo
                    )

                }

                item {
                    BaseSettingsItem(
                        painter = rememberVectorPainter(Icons.AutoMirrored.Default.Logout),
                        text = "退出登录",
                        descriptionText = "清空登录信息，解除登录占用。",
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

//            item {
//                BaseSettingsItem(
//                    painter = rememberVectorPainter(Icons.Outlined.Extension),
//                    text = "扩展能力",
//                    descriptionText = "提交反馈时记得带上这个！",
//                    onClick = onToSystemExpand
//                )
//            }

        }*/

        // 退出登录对话框
        ASAlertDialog(
            showState = showLogoutDialog,
            title = { Text("退出登录") },
            text = {
                Column(
                    Modifier
                        .animateContentSize()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showLogoutLoading) {
                        ContainedLoadingIndicator()
                        Text("退出中....")
                    } else {
                        Text("是否退出登录？退出登录将清除登录信息，解除登录占用。")
                    }
                }
            },
            onDismiss = {
                showLogoutDialog = false
            },
            confirmButton = {
                ASTextButton(onClick = {
                    showLogoutLoading = true
                    coroutineScope.launch(Dispatchers.IO) {
                        vm.logout()
                        showLogoutLoading = false
                        showLogoutDialog = false
                        onLogoutFinish(uiState.currentMid)
                    }
                }) {
                    Text("退出登录")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                }) {
                    Text("取消")
                }
            }

        )
    }

}
@Composable
fun SettingList(vm: SettingViewModel,showLogoutDialog:  () -> Unit,paddingValues: PaddingValues, connection: NestedScrollConnection, onItemClick: (NavKey) -> Unit){
    val appSettings by vm.appSettings.collectAsState(initial = AppSettings.getDefaultInstance())
    val haptics = LocalHapticFeedback.current
    val context = LocalContext.current
    val uiState by vm.uiState.collectAsState()
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .nestedScroll(connection),
    )
    {
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

//            item {
//                BaseSettingsItem(
//                    painter = rememberVectorPainter(Icons.Outlined.DriveFileRenameOutline),
//                    text = "命名规则",
//                    descriptionText = "使用自定义规则进行视频命名",
//                    onClick = {
//                    }
//                )
//            }
        item {
            BaseSettingsItem(
                painter = painterResource(R.drawable.ic_save_24px),
                text = "存储管理",
                descriptionText = "管理APP内存占用",
                onClick = { onItemClick(StorageManagementRoute) }
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
                text = "主题设置"
            )
        }
        item {
            SwitchSettingsItem(
                imageVector = Icons.Outlined.Palette,
                text = "动态主题",
                description = "使用桌面壁纸颜色作为主题",
                checked = appSettings.enabledDynamicColor,
            ) { check ->
                haptics.switchHapticFeedback(check)
                vm.updateEnabledDynamicColor(check)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            item {
                CategorySettingsItem(
                    text = "权限设置"
                )
            }
        }
        item {
            DownloadPostNotifications()
        }


        item {
            CategorySettingsItem(
                text = "布局配置"
            )
        }

        item {
            BaseSettingsItem(
                painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ListAlt),
                text = "首页排版",
                description = {},
                onClick = { onItemClick(LayoutTypesetRoute) }
            )
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
                onClick = {onItemClick(RoamRoute)}
            )
        }



        item {
            CategorySettingsItem(
                text = "关于程序"
            )
        }


        item {
            BaseSettingsItem(
                painter = rememberVectorPainter(Icons.Outlined.Group),
                text = "关于",
                descriptionText = "作为依赖平台的程序，我们有责任和义务维护平台生态的健康发展！",
                onClick = {onItemClick(AboutRouter)}
            )

        }


        item {
            BaseSettingsItem(
                painter = painterResource(R.drawable.ic_github_24px),
                text = "Github仓库",
                description = {},
                onClick = {
                    val intent = Intent().apply {
                        action = "android.intent.action.VIEW"
                        data = "https://github.com/1250422131/bilibilias".toUri()
                    }
                    context.startActivity(intent)
                }
            )
        }

//            item {
//                BaseSettingsItem(
//                    painter = painterResource(R.drawable.ic_licens_24px),
//                    text = "第三方开源许可",
//                    description = {},
//                    onClick = {
//
//                    }
//                )
//            }

//            item {
//                CategorySettingsItem(
//                    text = "投诉与反馈"
//                )
//            }
//
//            item {
//                BaseSettingsItem(
//                    painter = rememberVectorPainter(Icons.Outlined.MoodBad),
//                    text = "投诉",
//                    descriptionText = "向BILIBILIAS投诉违规行为",
//                    onClick = onToComplaint
//                )
//            }


        if (uiState.isLogin) {

            item {
                CategorySettingsItem(
                    text = "账户"
                )
            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.Android),
                    text = "设备信息",
                    descriptionText = "提交反馈时记得带上这个！",
                    onClick = {onItemClick(AppVersionInfoRoute) }
                )

            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Default.Logout),
                    text = "退出登录",
                    descriptionText = "清空登录信息，解除登录占用。",
                    onClick = showLogoutDialog
                )
            }
        }

//            item {
//                BaseSettingsItem(
//                    painter = rememberVectorPainter(Icons.Outlined.Extension),
//                    text = "扩展能力",
//                    descriptionText = "提交反馈时记得带上这个！",
//                    onClick = onToSystemExpand
//                )
//            }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingDetails(navKey: NavKey,scaffoldNavigator: ThreePaneScaffoldNavigator<NavKey>, paddingValues: PaddingValues,scrollBehavior: TopAppBarScrollBehavior,onToBack: () -> Unit){
    val scope = rememberCoroutineScope()
    when(navKey) {
        is StorageManagementRoute -> {
            StorageManagementContent(paddingValues, {
                scope.launch {
                    scaffoldNavigator.navigateTo(
                        ListDetailPaneScaffoldRole.Detail,
                        DownloadRoute(1)
                    )
                }
            })
        }
        is LayoutTypesetRoute ->{
            val vm = koinViewModel<LayoutTypesetViewModel>()
            val homeLayoutList by vm.homeLayoutTypesetList.collectAsState()
            LayoutTypesetContent(vm = vm, paddingValues = paddingValues, homeLayoutList, onMove = { from, to ->
                vm.moveLayoutItem(from.index, to.index)
            })
        }
        is DownloadRoute -> {DownloadContent(navKey,onToBack,paddingValues)}
        is RoamRoute -> {SettingNullDetails()}
        is AboutRouter -> {
            AboutContent(paddingValues,scrollBehavior.nestedScrollConnection)
        }
        is AppVersionInfoRoute->{VersionInfoContent(paddingValues)}
        else -> {SettingNullDetails()}
    }
}

@Composable
fun SettingNullDetails(){
    Spacer(modifier = Modifier)
}

@Composable
fun DownloadPostNotifications() {
    val haptics = LocalHapticFeedback.current

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
            haptics.switchHapticFeedback(it)
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showRequestForegroundServiceTip = true
            }
        }

        if (showRequestForegroundServiceTip) {
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
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
                style = when (rememberWidthSizeClass()) {
                        WindowWidthSizeClass.Compact -> BILIBILIASTopAppBarStyle.Large
                        else -> BILIBILIASTopAppBarStyle.Small
                },
                title = { Text(text = "设置") },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                }
            )
        },
    ) {
        content(it)
    }

}