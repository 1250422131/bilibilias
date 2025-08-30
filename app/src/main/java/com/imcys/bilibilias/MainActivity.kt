package com.imcys.bilibilias

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.app
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.common.event.sendAnalysisEvent
import com.imcys.bilibilias.common.utils.createDownloadNotificationChannel
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.BILIBILIASAppScreen
import com.imcys.bilibilias.ui.theme.BILIBILIASTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val appSettingsRepository: AppSettingsRepository by inject()

    private val appSettingsFlow: Flow<AppSettings> = appSettingsRepository.appSettingsFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var enabledDynamicColor by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                appSettingsFlow.collect {
                    enabledDynamicColor = it.enabledDynamicColor
                }
            }

            BILIBILIASTheme(dynamicColor = enabledDynamicColor) {
                BILIBILIASAppScreen()
            }
        }

        // 初始化设置
        initAppSetting()
        // 初始化通知渠道
        initNotificationChannel()

        handleShareInfo(intent)
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
        if (BuildConfig.DEBUG){
            Firebase.app.isDataCollectionDefaultEnabled = false
            return
        }
        Firebase.app.isDataCollectionDefaultEnabled = state == AppSettings.AgreePrivacyPolicyState.Agreed
    }


    private fun initNotificationChannel() {
        // 创建文件下载进度渠道
        createDownloadNotificationChannel()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BILIBILIASTheme {
        BILIBILIASAppScreen()
    }
}