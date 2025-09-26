package com.imcys.bilibilias.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WebAsset
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.common.utils.ASConstant.PRIVACY_POLICY_URL
import com.imcys.bilibilias.common.utils.openLink
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.di.ProvideKoinApplication
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.ui.login.navigation.QRCodeLoginRoute
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.ASAgreePrivacyPolicy
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder


@Composable
internal fun QRCodeLoginRoute(
    onToBack: () -> Unit,
    onBackHomePage: () -> Unit,
) {
    QRCodeLoginScreen(QRCodeLoginRoute(), onToBack, onBackHomePage,{})
}


@Preview
@Composable
fun QRCodeLoginScreenPreview() {
    ProvideKoinApplication {
        QRCodeLoginScreen(QRCodeLoginRoute(), {},{}) {}
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeLoginScreen(
    route: QRCodeLoginRoute,
    onToBack: () -> Unit,
    onBackHomePage: () -> Unit,
    onToCookieLogin: () -> Unit
) {
    val vm = koinViewModel<QRCodeLoginViewModel>()
    val uiState = vm.uiState
    val qrCodeInfoState by vm.qrCodeInfoState.collectAsState()
    val qrCodeScanInfoState by vm.qrCodeScanInfoState.collectAsState()
    val loginUserInfoState by vm.loginUserInfoState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(route.defaultLoginPlatform) {
        vm.updateLoginPlatform(route.defaultLoginPlatform)
    }

    // 监听扫码结果
    LaunchedEffect(qrCodeScanInfoState) {
        when (qrCodeScanInfoState) {
            is NetWorkResult.Success<*> -> {
                when (qrCodeScanInfoState.data?.code) {
                    0 -> {
                        vm.getLoginUserInfo(
                            qrCodeScanInfoState.data
                        )
                    }

                    86038 -> {
                        // 二维码失效
                        vm.getLoadLoginQRCodeInfo()
                    }

                    else -> {
                    }
                }
            }

            else -> {}
        }
    }

    // 监听登录信息获取
    LaunchedEffect(loginUserInfoState) {
        when (loginUserInfoState) {
            is NetWorkResult.Success<*> -> {
                vm.saveLoginInfo(loginUserInfoState.data) {
                    if (route.isFromRoam) {
                        // 如果是从漫游页面进入的，登录成功后直接返回
                        onToBack()
                    } else {
                        // 否则返回首页
                        onBackHomePage()
                    }
                }
            }

            else -> {}
        }

    }

    var agreePrivacyPolicy by rememberSaveable { mutableStateOf(false) }

    QRLoginScaffold(onToBack,onToCookieLogin) {
        Box(Modifier.padding(it)) {
            Column(
                Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            ) {

                // QR内容区域
                QRCodeContent(
                    route,
                    uiState.selectedLoginPlatform,
                    qrCodeInfoState,
                    agreePrivacyPolicy,
                    updateLoginPlatform = vm::updateLoginPlatform,
                    updateQrCode = vm::getLoadLoginQRCodeInfo,
                    updateAgreePrivacyPolicy = { state -> agreePrivacyPolicy = state }
                )

                // 操作区域
                ActionButton(
                    agreePrivacyPolicy,
                    saveQRCodeImage = {
                        vm.saveQRCodeImageToGallery(context)
                    },
                    goScanQR = {
                        vm.goToScanQR(context)
                    }
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QRLoginScaffold(
    onToBack: () -> Unit,
    onToCookieLogin: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text("B站扫码登录")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        ASIconButton(onClick = {
                            onToBack.invoke()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    },
                    actions = {
                        ASIconButton(onClick = {
                            expandedMenu = !expandedMenu
                        }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                contentDescription = "操作"
                            )
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false },
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            DropdownMenuItem(
                                text = { Text("使用Cookie登录") },
                                onClick = {
                                    expandedMenu = false
                                    onToCookieLogin.invoke()
                                }
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

@Composable
private fun ActionButton(
    agreePrivacyPolicy: Boolean,
    saveQRCodeImage: () -> Unit,
    goScanQR: () -> Unit,
) {
    Row(Modifier.fillMaxWidth()) {
        Button(
            enabled = agreePrivacyPolicy,
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 48.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
            shape = CardDefaults.shape,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            onClick = { saveQRCodeImage() }
        ) {
            Icon(
                Icons.Outlined.Download,
                contentDescription = "下载二维码",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                "下载二维码",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.width(24.dp))
        Button(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 48.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
            shape = CardDefaults.shape,
            onClick = { goScanQR() },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.OpenInNew,
                contentDescription = "去扫描",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                "去扫描",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ColumnScope.QRCodeContent(
    route: QRCodeLoginRoute,
    selectedLoginPlatform: LoginPlatform,
    qrCodeInfoState: NetWorkResult<QRCodeInfo?>,
    agreePrivacyPolicy: Boolean,
    updateLoginPlatform: (LoginPlatform) -> Unit,
    updateQrCode: () -> Unit,
    updateAgreePrivacyPolicy: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current

    Column(
        Modifier
            .weight(1f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CardDefaults.shape,
            border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(agreePrivacyPolicy) {
                    if (it) {
                        when (qrCodeInfoState) {
                            is NetWorkResult.Error<*> -> {
                                ASIconButton(onClick = {
                                    updateQrCode()
                                }) {
                                    Icon(Icons.Outlined.Replay, contentDescription = "重试")
                                }
                                Text("网络异常，点击按钮重试。")
                            }

                            is NetWorkResult.Default<*>,
                            is NetWorkResult.Loading<*> -> {
                                ContainedLoadingIndicator()
                            }

                            is NetWorkResult.Success<*> -> {
                                ASAsyncImage(
                                    "https://pan.misakamoe.com/qrcode/?url=" + URLEncoder.encode(
                                        qrCodeInfoState.data?.url,
                                        "UTF-8"
                                    ),
                                    contentDescription = "登录二维码",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            updateQrCode()
                                        })
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Outlined.Warning, contentDescription = "未勾选同意隐私政策")
                            Spacer(Modifier.height(4.dp))
                            Text("请勾选下方隐私政策")
                        }
                    }
                }
            }

        }
        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        ) {
            ToggleButton(
                checked = selectedLoginPlatform == LoginPlatform.WEB,
                onCheckedChange = {
                    if (it) {
                        haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    }
                    updateLoginPlatform(LoginPlatform.WEB)
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    imageVector = Icons.Outlined.WebAsset,
                    contentDescription = "Web扫码",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Web")
            }

            BadgedBox(
                badge = {
                    Badge {
                        Text("申请")
                    }
                }
            ) {
                ToggleButton(
                    enabled = route.isFromRoam,
                    checked = selectedLoginPlatform == LoginPlatform.TV,
                    onCheckedChange = {
                        if (it) {
                            haptics.performHapticFeedback(HapticFeedbackType.SegmentTick)
                        }
                        updateLoginPlatform(LoginPlatform.TV)
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WebAsset,
                        contentDescription = "TV扫码",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("TV")
                }
            }

        }
        Spacer(Modifier.height(10.dp))
        ASAgreePrivacyPolicy(agreePrivacyPolicy, onClick = {
            updateAgreePrivacyPolicy(!agreePrivacyPolicy)
        })
    }
}
