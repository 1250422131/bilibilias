package com.imcys.bilibilias.ui

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.event.loginErrorChannel
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Agreed
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Refuse
import com.imcys.bilibilias.navigation.BILIBILIASNavHost
import com.imcys.bilibilias.ui.BILIBILIASAppViewModel.UIState.DEFAULT
import com.imcys.bilibilias.ui.BILIBILIASAppViewModel.UIState.ACCOUNTCHECK

import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.weight.rememberKonfettiState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Preview
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

    // 页面注册区域
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
            DEFAULT -> {
                BILIBILIASNavHost(
                    navController = navigatorController
                )
            }

            is ACCOUNTCHECK -> {
                AccountCheckPage()
            }
        }
    }

    // Dialog注册区域
    PrivacyPolicyDialog(
        showState = appSettings.agreePrivacyPolicyValue <= 1, // 如果未同意则显示对话框
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AccountCheckPage() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContainedLoadingIndicator()
        Spacer(Modifier.height(5.dp))
        Text("登录状态失效，正在为你检查剩余可用登录。")
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
