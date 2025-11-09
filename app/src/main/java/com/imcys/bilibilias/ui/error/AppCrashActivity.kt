package com.imcys.bilibilias.ui.error

import android.content.ClipData
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.ui.theme.BILIBILIASTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

/**
 * APP未捕获异常崩溃页面
 * 未启用
 */
class AppCrashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appErrorMsg = intent.getStringExtra("appErrorMsg") ?: "未知错误"
        setContent {
            BILIBILIASTheme {
                Scaffold {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
                        AppErrorPage(appErrorMsg)
                    }
                }
            }
        }
    }

    @Composable
    fun AppErrorPage(appErrorMsg: String) {
        val clipboardManager = LocalClipboard.current
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "发生错误：${appErrorMsg}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    exitProcess(0)
                }) {
                    Text(stringResource(R.string.error_exit_app))
                }
                Spacer(Modifier.width(10.dp))
                Button(onClick = {
                    val clipData = ClipData.newPlainText("BILIBILAIS异常", appErrorMsg)
                    val clipEntry = ClipEntry(clipData)
                    coroutineScope.launch(Dispatchers.IO) {
                        clipboardManager.setClipEntry(clipEntry)
                        delay(2000)
                    }
                }) {
                    Text(stringResource(R.string.error_copy_error))
                }


            }
        }
    }
}

