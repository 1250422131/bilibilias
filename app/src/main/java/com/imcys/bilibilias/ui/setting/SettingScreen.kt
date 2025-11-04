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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.AirplaneTicket
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.imcys.bilibilias.R
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Agreed
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Refuse
import com.imcys.bilibilias.ui.PrivacyPolicyDialog
import com.imcys.bilibilias.ui.PrivacyPolicyRefuseDialog
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
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
    onToNamingConvention: () -> Unit = {},
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
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    var showPrivacyPolicyRefuseTip by remember { mutableStateOf(false) }


    SettingScaffold(scrollBehavior, onToBack) {

        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {

            item {
                CategorySettingsItem(
                    text = stringResource(R.string.home_text_5901)
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
                    text = stringResource(R.string.home_text_6181),
                    descriptionText = stringResource(R.string.home_text_6344),
                    onClick = onToStorageManagement
                )
            }


            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.Save),
                    text = stringResource(R.string.home_text_9045),
                    descriptionText = "Download/BILIBILIAS",
                    onClick = {
                    }
                )
            }



            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.Edit),
                    text = stringResource(R.string.home_text_8165),
                    descriptionText = stringResource(R.string.home_text_549),
                    onClick = onToNamingConvention
                )
            }


            item {
                CategorySettingsItem(
                    text = stringResource(R.string.home_text_7803)
                )
            }
            item {
                SwitchSettingsItem(
                    imageVector = Icons.Outlined.Palette,
                    text = stringResource(R.string.home_text_8618),
                    description = stringResource(R.string.home_text_4868),
                    checked = appSettings.enabledDynamicColor,
                ) { check ->
                    haptics.switchHapticFeedback(check)
                    vm.updateEnabledDynamicColor(check)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                item {
                    CategorySettingsItem(
                        text = stringResource(R.string.home_text_4600)
                    )
                }
            }

            item {
                DownloadPostNotifications()
            }


            item {
                CategorySettingsItem(
                    text = stringResource(R.string.home_text_9493)
                )
            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.ListAlt),
                    text = stringResource(R.string.home_text_8722),
                    description = {},
                    onClick = onToLayoutTypeset
                )
            }



            item {
                CategorySettingsItem(
                    text = stringResource(R.string.home_text_5146)
                )
            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.AutoMirrored.Outlined.AirplaneTicket),
                    text = stringResource(R.string.home_text_8163),
                    descriptionText = stringResource(R.string.video_analysis_1),
                    onClick = onToRoam
                )
            }



            item {
                CategorySettingsItem(
                    text = stringResource(R.string.home_text_993)
                )
            }


            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.Group),
                    text = stringResource(R.string.home_text_5679),
                    descriptionText = stringResource(R.string.home_program),
                    onClick = onToAbout
                )

            }


            item {
                BaseSettingsItem(
                    painter = painterResource(R.drawable.ic_github_24px),
                    text = stringResource(R.string.home_text_2021),
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


            item {
                CategorySettingsItem(
                    text = stringResource(R.string.home_text_3393)
                )
            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.Android),
                    text = stringResource(R.string.home_text_9295),
                    descriptionText = stringResource(R.string.home_text_5900),
                    onClick = onToVersionInfo
                )

            }

            item {
                BaseSettingsItem(
                    painter = rememberVectorPainter(Icons.Outlined.Policy),
                    text = stringResource(R.string.privacy_policy_2),
                    descriptionText = "当前状态：${
                        when (appSettings.agreePrivacyPolicy) {
                            Agreed -> stringResource(R.string.home_text_849)
                            Refuse -> stringResource(R.string.home_text_8202)
                            else -> stringResource(R.string.home_text_4821)
                        }
                    }，可在这里拒绝或同意我们的隐私政策。",
                    onClick = { showPrivacyPolicy = true }
                )
            }

            if (uiState.isLogin) {
                item {
                    BaseSettingsItem(
                        painter = rememberVectorPainter(Icons.AutoMirrored.Default.Logout),
                        text = stringResource(R.string.home_text_3124),
                        descriptionText = stringResource(R.string.home_text_2151),
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

        }

        // Dialog注册区域
        PrivacyPolicyDialog(
            showState = showPrivacyPolicy,
            onClickConfirm = {
                showPrivacyPolicy = false
                vm.updatePrivacyPolicyAgreement(Agreed)
            },
            onClickDismiss = {
                showPrivacyPolicy = false
                showPrivacyPolicyRefuseTip = true
                vm.updatePrivacyPolicyAgreement(Refuse)
            }
        )

        /**
         * 拒绝隐私政策后提示弹窗
         */
        PrivacyPolicyRefuseDialog(
            showState = showPrivacyPolicyRefuseTip,
            onClickConfirm = {
                showPrivacyPolicyRefuseTip = false
            }
        )

        // 退出登录对话框
        ASAlertDialog(
            showState = showLogoutDialog,
            title = { Text(stringResource(R.string.home_text_3124)) },
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
                        Text(stringResource(R.string.home_text_1519))
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
                    Text(stringResource(R.string.home_text_3124))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }

        )
    }

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
            text = stringResource(R.string.home_text_9602),
            description = stringResource(R.string.home_text_2800),
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
        message = stringResource(R.string.home_cache_permission),
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
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                },
            )
        },
    ) {
        content(it)
    }

}