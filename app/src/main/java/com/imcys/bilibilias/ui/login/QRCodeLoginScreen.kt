package com.imcys.bilibilias.ui.login


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.*
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.di.ProvideKoinApplication
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.ui.login.navigation.QRCodeLoginRoute
import com.imcys.bilibilias.ui.utils.rememberWidthSizeClass
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
    QRCodeLoginScreen(QRCodeLoginRoute(), onToBack, onBackHomePage, {})
}


@Preview
@Composable
fun QRCodeLoginScreenPreview() {
    ProvideKoinApplication {
        QRCodeLoginScreen(QRCodeLoginRoute(), {}, {}) {}
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
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
    val windowWidthSizeClass = rememberWidthSizeClass()
    var agreePrivacyPolicy by rememberSaveable { mutableStateOf(false) }

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
                    if (route.isFromRoam || route.isFromAnalysis) {
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

    QRLoginScaffold(onToBack, onToCookieLogin) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            SharedTransitionLayout {
                AnimatedContent(windowWidthSizeClass,label = stringResource(R.string.login_sao_ma_deng_lu_nei_rong_q)) { size ->
                    when (size) {
                        WindowWidthSizeClass.Compact -> {
                            QRCodeLoginContentWidthCompact(
                                vm,
                                route,
                                uiState,
                                qrCodeInfoState,
                                agreePrivacyPolicy,
                                updateAgreePrivacyPolicy = { state ->
                                    agreePrivacyPolicy = state
                                },
                                animatedVisibilityScope = this@AnimatedContent,
                                sharedTransitionScope = this@SharedTransitionLayout
                            )
                        }

                        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                            QRCodeLoginContentWidthMediumAndExpanded(
                                vm,
                                route,
                                uiState,
                                qrCodeInfoState,
                                agreePrivacyPolicy,
                                updateAgreePrivacyPolicy = { state ->
                                    agreePrivacyPolicy = state
                                },
                                animatedVisibilityScope = this@AnimatedContent,
                                sharedTransitionScope = this@SharedTransitionLayout
                            )
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QRCodeLoginContentWidthMediumAndExpanded(
    vm: QRCodeLoginViewModel,
    route: QRCodeLoginRoute,
    uiState: QRCodeLoginViewModel.UIState,
    qrCodeInfoState: NetWorkResult<QRCodeInfo?>,
    agreePrivacyPolicy: Boolean,
    updateAgreePrivacyPolicy: (Boolean) -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth()) {
        with(sharedTransitionScope) {
            // QR内容区域
            QRCodeContent(
                Modifier
                    .weight(6f)
                    .fillMaxHeight()
                    .sharedElement(
                        rememberSharedContentState(key = "qrCodeContent"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                route,
                uiState.selectedLoginPlatform,
                qrCodeInfoState,
                agreePrivacyPolicy,
                updateLoginPlatform = vm::updateLoginPlatform,
                updateQrCode = vm::getLoadLoginQRCodeInfo,
                updateAgreePrivacyPolicy = { state -> updateAgreePrivacyPolicy(state) }
            )
            // 操作区域
            Box(
                Modifier
                    .weight(4f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                ActionButton(
                    Modifier.fillMaxWidth().sharedElement(
                        rememberSharedContentState(key = "actionButton"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                    agreePrivacyPolicy,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    saveQRCodeImage = {
                        vm.saveQRCodeImageToGallery(context)
                    },
                    goScanQR = {
                        vm.goToScanQR(context)
                    },
                )
            }

        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun QRCodeLoginContentWidthCompact(
    vm: QRCodeLoginViewModel,
    route: QRCodeLoginRoute,
    uiState: QRCodeLoginViewModel.UIState,
    qrCodeInfoState: NetWorkResult<QRCodeInfo?>,
    agreePrivacyPolicy: Boolean,
    updateAgreePrivacyPolicy: (Boolean) -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val context = LocalContext.current
    Column(Modifier.fillMaxWidth()) {
        with(sharedTransitionScope) {
            // QR内容区域
            QRCodeContent(
                Modifier
                    .weight(1f)
                    .sharedElement(
                        rememberSharedContentState(key = "qrCodeContent"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                route,
                uiState.selectedLoginPlatform,
                qrCodeInfoState,
                agreePrivacyPolicy,
                updateLoginPlatform = vm::updateLoginPlatform,
                updateQrCode = vm::getLoadLoginQRCodeInfo,
                updateAgreePrivacyPolicy = { state -> updateAgreePrivacyPolicy(state) }
            )
            // 操作区域
            ActionButton(
                Modifier
                   .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState(key = "actionButton"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                agreePrivacyPolicy,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope= sharedTransitionScope,
                saveQRCodeImage = {
                    vm.saveQRCodeImageToGallery(context)
                },
                goScanQR = {
                    vm.goToScanQR(context)
                }
            )
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
                        Text(stringResource(R.string.login_b_zhan_sao_ma_deng_lu))
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
                                contentDescription = stringResource(R.string.common_fan_hui)
                            )
                        }
                    },
                    actions = {
                        ASIconButton(onClick = {
                            expandedMenu = !expandedMenu
                        }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                contentDescription = stringResource(R.string.login_cao_zuo)
                            )
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false },
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.login_shi_yong_cookie_deng_lu)) },
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ActionButton(
    modifier: Modifier,
    agreePrivacyPolicy: Boolean,
    saveQRCodeImage: () -> Unit,
    goScanQR: () -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val windowWidthSizeClass = rememberWidthSizeClass()
    with(sharedTransitionScope){
        AnimatedContent(windowWidthSizeClass) { size->
            when(size){
                WindowWidthSizeClass.Compact -> {
                    Row(modifier) {
                        Button(
                            enabled = agreePrivacyPolicy,
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "downloadQRButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                            shape = CardDefaults.shape,
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                            onClick = { saveQRCodeImage() }
                        ) {
                            Icon(
                                Icons.Outlined.Download,
                                contentDescription = stringResource(R.string.login_xia_zai_er_wei_ma),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                stringResource(R.string.login_xia_zai_er_wei_ma),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(Modifier.width(24.dp))
                        Button(
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "goToScanQRButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .weight(1f)
                                .defaultMinSize(minHeight = 48.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                            shape = CardDefaults.shape,
                            onClick = { goScanQR() },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.OpenInNew,
                                contentDescription = stringResource(R.string.login_qu_sao_miao),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                stringResource(R.string.login_qu_sao_miao),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded ->{
                    Column (modifier) {
                        Button(
                            enabled = agreePrivacyPolicy,
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "downloadQRButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                            shape = CardDefaults.shape,
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                            onClick = { saveQRCodeImage() }
                        ) {
                            Icon(
                                Icons.Outlined.Download,
                                contentDescription = stringResource(R.string.login_xia_zai_er_wei_ma),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                stringResource(R.string.login_xia_zai_er_wei_ma),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Button(
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "goToScanQRButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                            shape = CardDefaults.shape,
                            onClick = { goScanQR() },
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.OpenInNew,
                                contentDescription = stringResource(R.string.login_qu_sao_miao),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                stringResource(R.string.login_qu_sao_miao),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PlatformToggleButton(
    route: QRCodeLoginRoute,
    selectedLoginPlatform: LoginPlatform,
    updateLoginPlatform: (LoginPlatform) -> Unit,
) {
    val haptics = LocalHapticFeedback.current
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
            contentDescription = stringResource(R.string.login_web_sao_ma),
            modifier = Modifier.size(FilterChipDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Web")
    }

    BadgedBox(
        badge = {
            Badge {
                Text(stringResource(R.string.login_shen_qing))
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
                contentDescription = stringResource(R.string.login_tv_sao_ma),
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("TV")
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun QRCodeContent(
    modifier: Modifier = Modifier,
    route: QRCodeLoginRoute,
    selectedLoginPlatform: LoginPlatform,
    qrCodeInfoState: NetWorkResult<QRCodeInfo?>,
    agreePrivacyPolicy: Boolean,
    updateLoginPlatform: (LoginPlatform) -> Unit,
    updateQrCode: () -> Unit,
    updateAgreePrivacyPolicy: (Boolean) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val widthSizeClass = rememberWidthSizeClass()
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            val modifier = when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> Modifier.fillMaxWidth(0.6f)
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> Modifier.fillMaxHeight(0.6f)
                else -> Modifier
            }.aspectRatio(1f)

            Surface(
                modifier = modifier,
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
                            when (qrCodeInfoState.status) {
                                ApiStatus.ERROR -> {
                                    ASIconButton(onClick = {
                                        updateQrCode()
                                    }) {
                                        Icon(Icons.Outlined.Replay, contentDescription = stringResource(R.string.login_zhong_shi))
                                    }
                                    Text(stringResource(R.string.login_wang_luo_yi_chang__dian_j))
                                }

                                ApiStatus.DEFAULT,
                                ApiStatus.LOADING
                                    -> {
                                    ContainedLoadingIndicator()
                                }
                                ApiStatus.SUCCESS -> {
                                    ASAsyncImage(
                                        "https://pan.misakamoe.com/qrcode/?url=" + URLEncoder.encode(
                                            qrCodeInfoState.data?.url,
                                            "UTF-8"
                                        ),
                                        contentDescription = stringResource(R.string.login_deng_lu_er_wei_ma),
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
                                Icon(
                                    Icons.Outlined.Warning,
                                    contentDescription = stringResource(R.string.login_wei_gou_xuan_tong_yi_yin)
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(stringResource(R.string.login_qing_gou_xuan_xia_fang_yi))
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                PlatformToggleButton(route, selectedLoginPlatform, updateLoginPlatform)
            }
            ASAgreePrivacyPolicy(agreePrivacyPolicy, onClick = {
                updateAgreePrivacyPolicy(!agreePrivacyPolicy)
            })
        }
    }
}
