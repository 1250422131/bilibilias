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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.event.loginErrorChannel
import com.imcys.bilibilias.common.event.updateAccountChannel
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Agreed
import com.imcys.bilibilias.datastore.AppSettings.AgreePrivacyPolicyState.Refuse
import com.imcys.bilibilias.navigation.BILIBILAISNavDisplay
import com.imcys.bilibilias.ui.weight.ASAlertDialog
import com.imcys.bilibilias.ui.weight.ASAsyncImage
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.Konfetti
import com.imcys.bilibilias.weight.rememberKonfettiState
import org.koin.androidx.compose.koinViewModel


@Composable
internal fun BILIBILIASAppScreen() {
    MainScaffold()
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold() {
    val konfettiState = rememberKonfettiState(false)
    val vm = koinViewModel<BILIBILIASAppViewModel>()
    val appSettings by vm.appSettings.collectAsState()
    val uiState by vm.uiState.collectAsState()
    var showPrivacyPolicyRefuseTip by remember { mutableStateOf(false) }

    // 监听注册区域
    LaunchedEffect(Unit) {
        loginErrorChannel.collect {
            vm.accountLoginStateError()
        }
    }

    // 页面注册区域
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
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
                UIState.Default -> {
                    Surface(
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                    ) {
                        BILIBILAISNavDisplay()
                    }

                    // Dialog注册区域
                    PrivacyPolicyDialog(
                        showState = appSettings.agreePrivacyPolicyValue <= 1 && appSettings.knowAboutAppValue == 1, // 如果未同意则显示对话框
                        onClickConfirm = {
                            // 同意
                            konfettiState.value = true
                            vm.updatePrivacyPolicyAgreement(Agreed)
                        },
                        onClickDismiss = {
                            // 拒绝
                            showPrivacyPolicyRefuseTip = true
                            vm.updatePrivacyPolicyAgreement(Refuse)
                        }
                    )
                }

                is UIState.AccountCheck -> {
                    AccountCheckPage(targetUiState)
                }

                is UIState.KnowAboutApp -> {
                    InstructionsPage(onClickKnowAbout = vm::onKnowAboutApp)
                }
            }
        }
        Konfetti(konfettiState)
    }

    /**
     * 拒绝隐私政策后提示弹窗
     */
    PrivacyPolicyRefuseDialog(
        showState = showPrivacyPolicyRefuseTip,
        onClickConfirm = {
            showPrivacyPolicyRefuseTip = false
        }
    )
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
                    Text(stringResource(R.string.instructions_title))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                navigationIcon = {
                    ASIconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.cd_back)
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
            Column(
                Modifier.weight(1f)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CardDefaults.shape
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        item {
                            Text(
                                stringResource(R.string.instructions_not_bilibili),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        item {
                            Text(
                                stringResource(R.string.instructions_no_authorization),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        item {
                            Text(
                                stringResource(R.string.instructions_purpose),
                            )
                        }

                        item {
                            Text(
                                stringResource(R.string.instructions_no_redistribution),

                                )
                        }
                        item {
                            Text(
                                stringResource(R.string.instructions_self_responsibility),
                            )
                        }
                    }
                }
            }
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

                Text(stringResource(R.string.instructions_go_to_bilibili))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    Icons.AutoMirrored.Outlined.OpenInNew,
                    contentDescription = stringResource(R.string.cd_go_to_bilibili),
                )

            }
            Button(onClick = onClickKnowAbout, Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.instructions_i_agree))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AccountCheckPage(targetUiState: UIState.AccountCheck) {
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
            Text(stringResource(R.string.account_check_invalid))
        } else {
            if (targetUiState.newCurrentUser == null) {
                Text(stringResource(R.string.account_all_invalid))
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
            ASTextButton(onClick = onClickConfirm) {
                Text(text = stringResource(R.string.common_agree))
            }
        },
        dismissButton = {
            ASTextButton(onClick = onClickDismiss) {
                Text(text = stringResource(R.string.common_refuse))
            }
        }
    )
}


/**
 * 隐私政策拒绝后提示弹窗
 */
@Composable
fun PrivacyPolicyRefuseDialog(
    showState: Boolean,
    onClickConfirm: () -> Unit,
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
                    val tip = it.context.getString(R.string.privacy_refuse_content).trimIndent()
                    it.apply {
                        it.setTextColor(textColor)
                        text = HtmlCompat.fromHtml(tip, HtmlCompat.FROM_HTML_MODE_COMPACT)
                        movementMethod = LinkMovementMethod.getInstance()
                    }
                }
            )
        },
        confirmButton = {
            ASTextButton(onClick = onClickConfirm) {
                Text(text = stringResource(R.string.common_i_know))
            }
        },
    )
}
