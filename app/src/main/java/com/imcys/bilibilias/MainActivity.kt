package com.imcys.bilibilias

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.install.model.InstallStatus
import com.google.firebase.Firebase
import com.google.firebase.app
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.common.event.sendAnalysisEvent
import com.imcys.bilibilias.common.update.GooglePlayAppUpdateManage
import com.imcys.bilibilias.common.utils.Manufacturers.XIAOMI
import com.imcys.bilibilias.common.utils.createDownloadNotificationChannel
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.BILIBILIASAppScreen
import com.imcys.bilibilias.ui.theme.BILIBILIASTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    private val appSettingsRepository: AppSettingsRepository by inject()

    private val appSettingsFlow: Flow<AppSettings> = appSettingsRepository.appSettingsFlow

    // 更新相关
    private var showUpdateSnackBar = MutableStateFlow(false)
    private var showSkipVersion = MutableStateFlow(false)
    private var googlePlaySkipVersionListener: () -> Unit = {}
    private var performedInstallListen = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var enabledDynamicColor by remember { mutableStateOf(false) }
            val updateSnackBarHostState = remember { SnackbarHostState() }
            val showSkipVersionState by showSkipVersion.collectAsState()

            LaunchedEffect(Unit) {
                appSettingsFlow.collect {
                    enabledDynamicColor = it.enabledDynamicColor
                }
            }

            LaunchedEffect(showUpdateSnackBar) {
                if (!showUpdateSnackBar.value) return@LaunchedEffect
                val result = updateSnackBarHostState.showSnackbar(
                    message = "新版本已经下载完成，可随时进行更新。",
                    actionLabel = "更新",
                    duration = SnackbarDuration.Short
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        showUpdateSnackBar.value = false
                        performedInstallListen.invoke()
                    }

                    SnackbarResult.Dismissed -> {
                        showUpdateSnackBar.value = false
                    }
                }
            }

            BILIBILIASTheme(dynamicColor = enabledDynamicColor) {
                Box {
                    BILIBILIASAppScreen()
                    SnackbarHost(
                        hostState = updateSnackBarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                    SkipVersionDialog(showSkipVersionState, onConfirm = {
                        lifecycleScope.launch {
                            googlePlaySkipVersionListener.invoke()
                            showSkipVersion.value = false
                        }
                    })
                }
            }
        }
        // 处理特殊厂商的适配选项
        specialManufacturersOption()
        // 初始化设置
        initAppSetting()
        // 初始化通知渠道
        initNotificationChannel()
        // 更新检查
        initUpdateCheck()
        handleShareInfo(intent)
    }

    @Composable
    fun SkipVersionDialog(value: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit = {}) {
        if (value) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("跳过更新") },
                text = { Text("如果您暂时不想更新，可以跳过此版本，等下个版本再进行更新。") },
                confirmButton = {
                    Button(onClick = onConfirm) {
                        Text("跳过此版本")
                    }
                },
                dismissButton = {
                    Button(onClick = onDismiss) {
                        Text("下次再说")
                    }
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleShareInfo(intent)
    }

    private fun handleShareInfo(incoming: Intent?) {
        if (incoming == null) return
        val action = incoming.action
        val type = incoming.type
        when (action) {
            Intent.ACTION_SEND -> {
                if (type?.startsWith("text/") == true) {
                    val sharedText = incoming.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
                    sendAnalysisEvent(AnalysisEvent(analysisText = sharedText))
                }
            }

            Intent.ACTION_MAIN -> {
                // 正常启动
            }
        }
    }


    private fun specialManufacturersOption() {
        val manufacturer = Build.MANUFACTURER.lowercase()
        when {
            manufacturer.contains(XIAOMI) -> {
                // 设置沉浸式虚拟键，在MIUI系统中，虚拟键背景透明。原生系统中，虚拟键背景半透明。
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    /**
     * 初始化APP设置
     */
    private fun initAppSetting() {
        lifecycleScope.launch(Dispatchers.IO) {
            appSettingsFlow.collect {
                initFirebase(it.agreePrivacyPolicy)
            }
        }
    }

    /**
     * 初始化Firebase
     */
    private fun initFirebase(state: AppSettings.AgreePrivacyPolicyState) {
        if (BuildConfig.DEBUG) {
            Firebase.app.isDataCollectionDefaultEnabled = false
            return
        }
        Firebase.app.isDataCollectionDefaultEnabled =
            state == AppSettings.AgreePrivacyPolicyState.Agreed
    }


    private fun initNotificationChannel() {
        // 创建文件下载进度渠道
        createDownloadNotificationChannel()
    }


    /**
     * 初始化更新检查
     */
    private fun initUpdateCheck() {
        // 暂时留个技术债，因为接口问题，无法直接使用多态，后续再优化
        if (BuildConfig.ENABLE_PLAY_APP_MODE) {
            with(GooglePlayAppUpdateManage(this, appSettingsRepository)) {
                // 跳过版本号监听
                googlePlaySkipVersionListener = {
                    lifecycleScope.launch(Dispatchers.IO) {
                       appSettingsRepository.updateLastSkipUpdateVersionCode(getUpdateVersion())
                    }
                }
                // 检查更新
                handleGooglePlayUpdate()
            }
        }
    }

    private val appUpdateLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            // handle callback
            if (result.resultCode != RESULT_OK) {
                // 详见：https://developer.android.google.cn/guide/playcore/in-app-updates/kotlin-java?hl=zh-cn#setup
            }
        }

    /**
     * 处理Google Play更新
     */
    private fun GooglePlayAppUpdateManage.handleGooglePlayUpdate() {
        lifecycleScope.launch(Dispatchers.IO) {
            // 灵活更新，非必须
            if (checkAppFlexibleUpdate()) {
                registerFlexibleUpdateListener { state ->
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        showGooglePlayUpdateSnackBar()
                    }
                }
                startUpdate(appUpdateLauncher, updateFinish = {
                    showGooglePlayUpdateSnackBar()
                })
                return@launch
            }
            if (checkAppImmediateUpdate()) {
                startUpdate(appUpdateLauncher, updateFinish = {
                    showGooglePlayUpdateSnackBar()
                })
            }
        }
    }

    /**
     * 显示Google Play更新完成提示
     */
    private fun GooglePlayAppUpdateManage.showGooglePlayUpdateSnackBar() {
        lifecycleScope.launch {
            // 提示更新完成
            showUpdateSnackBar.value = true
            performedInstallListen = {
                // 重启更新
                completeUpdate()
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BILIBILIASTheme {
        BILIBILIASAppScreen()
    }
}