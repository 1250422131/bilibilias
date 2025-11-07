package com.imcys.bilibilias.ui.setting.roam

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.utils.switchHapticFeedback
import com.imcys.bilibilias.ui.weight.ASCheckThumbSwitch
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.BannerItem
import com.imcys.bilibilias.ui.weight.TipSettingsItem
import com.imcys.bilibilias.ui.weight.tip.ASErrorTip
import com.imcys.bilibilias.ui.weight.tip.ASInfoTip
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.ASCommonLoadingScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
@Preview
fun PreviewRoamScreen() {
    RoamScreen({})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoamScreen(
    onToBack: () -> Unit,
    onGoToQRCodeLogin: (loginPlatform: LoginPlatform) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lifecycleOwner = LocalLifecycleOwner.current
    val vm = koinViewModel<RoamViewModel>()
    val uiState by vm.uiState.collectAsState()

    val appSettings by vm.appSettings.collectAsState(initial = AppSettings.getDefaultInstance())
    val haptics = LocalHapticFeedback.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                vm.updateLoginTVStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        vm.loadRoamUIState()
    }

    RoamSettingScaffold(
        scrollBehavior = scrollBehavior,
        onToBack
    ) { paddingValues ->
        RoamSettingContent(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues),
            uiState,
            appSettings,
            haptics,
            vm,
            onGoToQRCodeLogin
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoamSettingContent(
    modifier: Modifier = Modifier,
    uiState: RoamUIState,
    appSettings: AppSettings,
    haptics: HapticFeedback,
    vm: RoamViewModel,
    onGoToQRCodeLogin: (LoginPlatform) -> Unit
) {
    Column {
        RoamSettingStateContent(modifier, uiState, appSettings, haptics, vm, onGoToQRCodeLogin)

        RoamSettingDescription()
    }
}

@Composable
private fun RoamSettingDescription() {
    TipSettingsItem(
        """
        漫游服务并非会使用VPN方式提供代理，而是中间服务器白名单请求转发。
        
        开启后您的账户的部分网络请求和身份信息会被带到我们的服务器上，通过我们的服务代理请求。
    """.trimIndent()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoamSettingStateContent(
    modifier: Modifier,
    uiState: RoamUIState,
    appSettings: AppSettings,
    haptics: HapticFeedback,
    vm: RoamViewModel,
    onGoToQRCodeLogin: (LoginPlatform) -> Unit
) {
    AnimatedContent(uiState) { state ->
        when (state) {
            is RoamUIState.Error -> RoamSettingErrorScreen(state.errorMsg)
            RoamUIState.Loading -> ASCommonLoadingScreen()
            is RoamUIState.Success -> Column(modifier = modifier) {
                RoamSettingSuccessScreen(
                    state,
                    appSettings,
                    haptics,
                    vm,
                    onGoToQRCodeLogin
                )
            }

            is RoamUIState.Apply -> Column(modifier = modifier) {
                RoamSettingApplyScreen(state, haptics, vm)
            }

            RoamUIState.NoLogin -> Column(modifier = modifier) {
                RoamSettingNoLoginTip(
                    onGoToQRCodeLogin
                )
            }
        }
    }
}

/**
 * 网络异常
 */
@Composable
private fun RoamSettingErrorScreen(errorMsg: String) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.CloudOff,
            contentDescription = "图标",
            Modifier.size(100.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("加载失败：$errorMsg，请稍后重试。", fontSize = 16.sp)
        Button(
            shape = CardDefaults.shape,
            onClick = {}
        ) {
            Text("重试")
        }
    }
}

/**
 * 登录提示
 */
@Composable
private fun RoamSettingNoLoginTip(onGoToQRCodeLogin: (LoginPlatform) -> Unit) {
    Column(Modifier.padding(10.dp)) {
        ASWarringTip(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            enabledPadding = false
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "当前还未登录，登录后检测漫游权限。",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                ASIconButton(onClick = {
                    onGoToQRCodeLogin.invoke(LoginPlatform.WEB)
                }) {
                    Icon(Icons.Outlined.NorthEast, contentDescription = "去登录")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoamSettingApplyScreen(
    uiState: RoamUIState.Apply,
    haptics: HapticFeedback,
    vm: RoamViewModel
) {
    RoamApplyContainer(uiState, haptics, vm)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun RoamApplyContainer(
    uiState: RoamUIState.Apply,
    haptics: HapticFeedback,
    vm: RoamViewModel
) {
    var applyRoamContent by remember { mutableStateOf(uiState.applyRoamBean.reason ?: "") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    AnimatedContent(uiState.applyRoamBean.code) { code ->
        RoamApplyBody(
            code = code,
            applyRoamContent = applyRoamContent,
            isLoading = isLoading,
            onContentChange = { newValue -> applyRoamContent = newValue.take(400) },
            onSubmit = {
                haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                scope.launch(Dispatchers.IO) {
                    isLoading = true
                    val result = vm.applyRoam(applyRoamContent)
                    launch(Dispatchers.Main) {
                        if (result.isFailure) {
                            Toast.makeText(
                                context,
                                "网络异常，请稍后重试",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        result.getOrNull()?.let { bean ->
                            Toast.makeText(context, bean.msg, Toast.LENGTH_SHORT).show()
                        }
                        vm.loadRoamUIState()
                    }
                    isLoading = false
                }
            },
            isRejected = (code == 400),
            rejectReason = uiState.applyRoamBean.rejectReason
        )
    }
}

@Composable
private fun RoamApplyBody(
    code: Int,
    applyRoamContent: String,
    isLoading: Boolean,
    onContentChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isRejected: Boolean,
    rejectReason: String?
) {
    Column(modifier = Modifier.padding(10.dp)) {
        when (code) {
            // 待审核
            302 -> {
                ApplyPendingTip(applyRoamContent)
            }
            // 拒绝, 未申请
            404, 400 -> {
                ApplyForm(
                    applyRoamContent = applyRoamContent,
                    isLoading = isLoading,
                    onContentChange = onContentChange,
                    onSubmit = onSubmit,
                    isRejected = isRejected,
                    rejectReason = rejectReason
                )
            }
        }
    }
}

// 展示正在审核时的 UI
@Composable
private fun ApplyPendingTip(applyRoamContent: String) {
    ASInfoTip {
        Text("当前您的审核正在进行中，请耐心等待，我们会尽快处理您的申请，通常会在7天内完成审核，有时会更长。")
    }
    Spacer(Modifier.height(6.dp))
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardDefaults.shape
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text("您的申请内容如下：")
            Text(applyRoamContent)
        }
    }
}

// 申请表单
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ApplyForm(
    applyRoamContent: String,
    isLoading: Boolean,
    onContentChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isRejected: Boolean,
    rejectReason: String?
) {
    if (isRejected) {
        ASErrorTip {
            Text("您的漫游服务申请被拒绝，拒绝理由如下：${rejectReason ?: "暂无理由"}，您可以修改申请理由后重新提交申请。")
        }
    } else {
        ASWarringTip {
            Text("当前您还未申请漫游服务，请填写申请理由后提交，你需要告知我们你将如何使用漫游服务，只有合理的申请我们才会通过。")
        }
    }

    Spacer(Modifier.height(6.dp))

    ApplyFormEditor(value = applyRoamContent, onValueChange = onContentChange)

    Spacer(Modifier.height(12.dp))

    ApplyFormSubmitButton(
        isLoading = isLoading,
        enabled = applyRoamContent.isNotBlank(),
        onClick = onSubmit
    )
}

@Composable
private fun ApplyFormEditor(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        enabled = true,
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        supportingText = {
            Text(
                text = "${value.length} / 400",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall
            )
        },
        label = { Text("申请内容") },
        minLines = 4,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ApplyFormSubmitButton(isLoading: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Button(
        enabled = !isLoading && enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = CardDefaults.shape,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                // 加载进度
                CircularWavyProgressIndicator()
            } else {
                Text("提交申请")
            }
        }
    }

}


@Composable
private fun RoamSettingSuccessScreen(
    uiState: RoamUIState.Success,
    appSettings: AppSettings,
    haptics: HapticFeedback,
    vm: RoamViewModel,
    onGoToQRCodeLogin: (LoginPlatform) -> Unit
) {
    BannerItem {
        Row(
            Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("启用漫游", fontSize = 18.sp)
            Spacer(Modifier.weight(1f))
            ASCheckThumbSwitch(
                enabled = (uiState.isLoginTV || appSettings.enabledRoam),
                checked = appSettings.enabledRoam,
                onCheckedChange = {
                    if (!uiState.isLogin) return@ASCheckThumbSwitch
                    haptics.switchHapticFeedback(it)
                    vm.updateRoamEnabledState(it)
                }
            )
        }
    }

    if ((!uiState.isLoginTV || !uiState.isLogin)) {
        Column(
            Modifier.padding(vertical = 5.dp, horizontal = 12.dp),
        ) {
            ASWarringTip(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                enabledPadding = false
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "你当前还未登录漫游身份，点击登录后才可开启哦。",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    ASIconButton(onClick = {
                        if (!uiState.isLogin) {
                            // 未登录登录
                            onGoToQRCodeLogin.invoke(LoginPlatform.WEB)
                        } else {
                            // 未登录TV登录
                            onGoToQRCodeLogin.invoke(LoginPlatform.TV)
                        }
                    }) {
                        Icon(Icons.Outlined.NorthEast, contentDescription = "去登录")
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoamSettingScaffold(
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
                title = {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text("Beta")
                            }
                        }
                    ) {
                        Text(text = "漫游服务")
                    }
                },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                },
                alwaysDisplay = false
            )
        },
    ) {
        content(it)
    }

}