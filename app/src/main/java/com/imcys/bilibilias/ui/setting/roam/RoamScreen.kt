package com.imcys.bilibilias.ui.setting.roam

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.utils.switchHapticFeedback
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.BannerItem
import com.imcys.bilibilias.ui.weight.TipSettingsItem
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
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


    RoamSettingScaffold(
        scrollBehavior = scrollBehavior,
        onToBack
    ) { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues)) {
            item {

                BannerItem {
                    Row(
                        Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("启用漫游", fontSize = 18.sp)
                        Spacer(Modifier.weight(1f))
                        Switch(
                            enabled = uiState.isLoginTV || appSettings.enabledRoam,
                            checked = appSettings.enabledRoam,
                            onCheckedChange = {
                                if (!uiState.isLogin) return@Switch
                                haptics.switchHapticFeedback(it)
                                vm.updateRoamEnabledState(it)
                            }
                        )
                    }
                }
            }
            item {
                if (!uiState.isLoginTV || !uiState.isLogin) {
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


            item {
                TipSettingsItem(
                    """
                    漫游服务并非会使用VPN方式提供代理，而是中间服务器白名单请求转发。
                    
                    开启后您的账户的部分网络请求和身份信息会被带到我们的服务器上，通过我们的服务代理请求。
                """.trimIndent()
                )
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
                }
            )
        },
    ) {
        content(it)
    }

}