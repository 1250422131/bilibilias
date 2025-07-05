package com.imcys.bilibilias.ui.login

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.WebAsset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder


@Composable
internal fun QRCodeLoginRoute(
    onToBack: () -> Unit,
    onBackHomePage: () -> Unit,
) {
    QRCodeLoginScreen(onToBack, onBackHomePage)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeLoginScreen(
    onToBack: () -> Unit,
    onBackHomePage: () -> Unit,
) {
    val vm = koinViewModel<QRCodeLoginViewModel>()
    val uiState = remember { vm.uiState }
    val qrCodeInfoState by vm.qrCodeInfoState.collectAsState()
    val qrCodeScanInfoState by vm.qrCodeScanInfoState.collectAsState()
    val loginUserInfoState by vm.loginUserInfoState.collectAsState()
    val context = LocalContext.current


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
                    onBackHomePage()
                }
            }

            else -> {}
        }

    }


    QRLoginScaffold(onToBack) {
        Box(Modifier.padding(it)) {
            Column(
                Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            ) {

                // QR内容区域
                QRCodeContent(
                    uiState.selectedLoginPlatform,
                    qrCodeInfoState,
                    updateLoginPlatform = vm::updateLoginPlatform,
                    updateQrCode = vm::getLoadLoginQRCodeInfo
                )

                // 操作区域
                ActionButton(
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QRLoginScaffold(onToBack: () -> Unit, content: @Composable (PaddingValues) -> Unit) {
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
                        IconButton(onClick = {
                            onToBack.invoke()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "返回"
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
    saveQRCodeImage: () -> Unit,
    goScanQR: () -> Unit,
) {
    Row(Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 48.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
            shape = CardDefaults.shape,
            onClick = { saveQRCodeImage() }
        ) {
            Icon(
                Icons.Outlined.Download,
                contentDescription = "下载二维码",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(Modifier.width(14.dp))
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
            onClick = { goScanQR() }
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.OpenInNew,
                contentDescription = "去扫描",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(Modifier.width(14.dp))
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
    selectedLoginPlatform: LoginPlatform,
    qrCodeInfoState: NetWorkResult<QRCodeInfo?>,
    updateLoginPlatform: (LoginPlatform) -> Unit,
    updateQrCode: () -> Unit
) {
    Column(
        Modifier
            .weight(1f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            FilterChip(
                onClick = { updateLoginPlatform(LoginPlatform.WEB) },
                label = {
                    Text("Web")
                },
                selected = selectedLoginPlatform == LoginPlatform.WEB,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.WebAsset,
                        contentDescription = "Web扫码",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                },
            )

            FilterChip(
                onClick = { updateLoginPlatform(LoginPlatform.TV) },
                label = {
                    Text("TV")
                },
                selected = selectedLoginPlatform == LoginPlatform.TV,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Tv,
                        contentDescription = "TV扫码",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                },
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
            contentColor = MaterialTheme.colorScheme.surfaceContainer,
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
                when (qrCodeInfoState) {
                    is NetWorkResult.Error<*> -> {
                        IconButton(onClick = {
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
                        AsyncImage(
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
            }

        }

    }
}
