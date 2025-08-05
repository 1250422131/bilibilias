package com.imcys.bilibilias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.app
import com.imcys.bilibilias.common.utils.createDownloadNotificationChannel
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.ui.BILIBILIASAppScreen
import com.imcys.bilibilias.ui.theme.BILIBILIASTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val appSettingsRepository: AppSettingsRepository by inject()

    private val appSettingsFlow: Flow<AppSettings> = appSettingsRepository.appSettingsFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BILIBILIASTheme {
                BILIBILIASAppScreen()
            }
        }

        // 初始化设置
        initAppSetting()
        // 初始化通知渠道
        initNotificationChannel()
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