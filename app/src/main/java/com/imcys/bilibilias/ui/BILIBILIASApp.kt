package com.imcys.bilibilias.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.event.appErrorHandleChannel
import com.imcys.bilibilias.common.event.loginErrorChannel
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Agreed
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Refuse
import com.imcys.bilibilias.navigation.BILIBILIASNavHost
import com.imcys.bilibilias.ui.BILIBILIASAppViewModel.UIState.*
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.Konfetti
import com.imcys.bilibilias.weight.rememberKonfettiState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.system.exitProcess


@Composable
fun BILIBILIASApp() {
    BILIBILIASAppScreen()
}

@Composable
internal fun BILIBILIASAppScreen() {
    MainScaffold()
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold() {
    // 参数注册区域
    val appSettingsRepository: AppSettingsRepository = koinInject()
    val appSettings by appSettingsRepository.appSettingsFlow.collectAsState(
        initial = AppSettings.getDefaultInstance()
    )
    val navigatorController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val konfettiState = rememberKonfettiState(false)
    val vm = koinViewModel<BILIBILIASAppViewModel>()
    val uiState by vm.uiState.collectAsState()


    // 监听注册区域
    LaunchedEffect(Unit) {
        loginErrorChannel.collect {
            vm.accountLoginStateError()
        }
    }

    LaunchedEffect(Unit) {
        appErrorHandleChannel.collect {
            vm.appError(it)
        }
    }

    // 页面注册区域
    Box {
        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                // 进入动画：淡入
                fadeIn(
                    animationSpec = tween(durationMillis = 300)
                ) togetherWith fadeOut(
                    animationSpec = tween(durationMillis = 300)
                )
            },
        ) { targetUiState ->
            when (targetUiState) {
                Default -> {
                    BILIBILIASNavHost(
                        navController = navigatorController
                    )

                    // Dialog注册区域
                    PrivacyPolicyDialog(
                        showState = appSettings.agreePrivacyPolicyValue <= 1 && appSettings.knowAboutAppValue == 1, // 如果未同意则显示对话框
                        onClickConfirm = {
                            // 同意
                            coroutineScope.launch {
                                konfettiState.value = true
                                appSettingsRepository.updatePrivacyPolicyAgreement(Agreed)
                                Firebase.app.isDataCollectionDefaultEnabled = true

                            }
                        },
                        onClickDismiss = {
                            // 拒绝
                            coroutineScope.launch {
                                appSettingsRepository.updatePrivacyPolicyAgreement(Refuse)
                                Firebase.app.isDataCollectionDefaultEnabled = false
                            }
                        }
                    )

                }

                is AccountCheck -> {
                    AccountCheckPage(targetUiState)
                }

                is KnowAboutApp -> {
                    InstructionsPage(onClickKnowAbout = {
                        vm.onKnowAboutApp()
                    })
                }

                is AppError -> {
                    AppErrorPage(targetUiState)
                }
            }
        }

        Konfetti(konfettiState)
    }


}

@Composable
fun AppErrorPage(appError: AppError) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "发生错误：${appError.appException?.message}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            exitProcess(0)
        }) {
            Text("退出软件")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun InstructionsPage(onClickKnowAbout: () -> Unit = {}) {
    val content = LocalContext.current
    Scaffold(
        topBar = {
            ASTopAppBar(
                style = BILIBILIASTopAppBarStyle.Small,
                title = {
                    Text("使用须知")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {}
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CardDefaults.shape
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            """
                您正在使用的软件并非哔哩哔哩/bilibili，而是辅助其的第三方工具软件，与哔哩哔哩没有任何关联。
            """.trimIndent(),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        Text(
                            """
                                此软件未得到哔哩哔哩许可，哔哩哔哩对此使用软件而造成的一切后果概不负责。
                                """.trimIndent(),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        Text(
                            """
                                    BILIBILIAS是一款第三方的B站（哔哩哔哩）视频缓存工具，旨在帮助需要离线播放或者剪辑原创视频的自媒体博主。
                                """.trimIndent(),
                        )
                    }

                    item {
                        Text(
                            """
                            在BILIBILIAS缓存的任何内容都不得进行二次传播，仅允许在您自己的终端设备播放或者制作剪辑视频（未经作者允许不得直接搬运）。
                        """.trimIndent(),

                            )
                    }
                    item {
                        Text(
                            """
                            如果您违反了规定或者用作了非法用途，那么一切后果将由您自行承担，同时BILIBILIAS可能将禁止您继续使用。
                        """.trimIndent(),
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    content.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://bilibili.com".toUri()
                        )
                    )
                },
                Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {

                Text("不，我找的是哔哩哔哩")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    Icons.AutoMirrored.Outlined.OpenInNew,
                    contentDescription = "前往哔哩哔哩",
                )

            }
            Button(onClick = onClickKnowAbout, Modifier.fillMaxWidth()) {
                Text("我知晓且同意")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AccountCheckPage(targetUiState: AccountCheck) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (targetUiState.isCheckLoading) {
            ContainedLoadingIndicator()
            Spacer(Modifier.height(5.dp))
            Text("登录状态失效，正在为你检查剩余可用登录。")
        } else {
            if (targetUiState.newCurrentUser == null) {
                Text("所有账户已失效，请重新登录。")
            }
        }
    }
}

/**
 * 隐私政策对话框
 */
@Composable
fun PrivacyPolicyDialog(
    showState: Boolean,
    onClickConfirm: () -> Unit,
    onClickDismiss: () -> Unit,
) {
    ASAlertDialog(
        showState = showState,
        title = { Text(text = stringResource(R.string.common_privacy_policy)) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.PrivacyTip,
                contentDescription = stringResource(R.string.common_privacy_policy)
            )
        },
        text = {
            val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
            AndroidView(
                factory = { TextView(it) },
                update = {
                    val tip = it.context.getString(R.string.app_privacy_policy_tip).trimIndent()
                    it.apply {
                        it.setTextColor(textColor)
                        text = HtmlCompat.fromHtml(tip, HtmlCompat.FROM_HTML_MODE_COMPACT)
                        movementMethod = LinkMovementMethod.getInstance()
                    }
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onClickConfirm) {
                Text(text = stringResource(R.string.common_agree))
            }
        },
        dismissButton = {
            TextButton(onClick = onClickDismiss) {
                Text(text = stringResource(R.string.common_refuse))
            }
        }
    )
}
