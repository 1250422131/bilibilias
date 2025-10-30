package com.imcys.bilibilias.ui.event.requestFrequent

import android.os.Process.killProcess
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.R
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class RequestFrequentRoute(
    val url: String
) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestFrequentScreen(
    requestFrequentRoute: RequestFrequentRoute,
    onToBack: () -> Unit,
) {

    // 拦截返回
    BackHandler {}

    RequestFrequentScaffold {
        RequestFrequentContent(
            url = requestFrequentRoute.url,
            paddingValues = it,
            onToBack,
        )
    }
}

@Composable
fun RequestFrequentContent(url: String, paddingValues: PaddingValues, onToBack: () -> Unit) {

    val vm = koinViewModel<RequestFrequentViewModel>()
    val state by vm.uiState.collectAsState()

    AnimatedContent(state) {
        when (it) {
            RequestFrequentUIState.Default -> DefaultScreen(paddingValues, onRetry = {
                vm.retryRequest(url)
            })

            RequestFrequentUIState.Loading -> {
                Column(
                    Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("请求测试中...", modifier = Modifier.padding(top = 16.dp))
                }
            }

            RequestFrequentUIState.Success -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("请求成功，可以正常使用啦！", modifier = Modifier.padding(top = 16.dp))
                    Button(onClick = onToBack) {
                        Text("返回继续使用")
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultScreen(
    paddingValues: PaddingValues,
    onRetry: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Icon(
            painter = painterResource(R.drawable.ic_cloud_alert_24px),
            contentDescription = "服务器异常图标",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "突发事件",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 20.dp),
        )
        Text(
            text = "B站当前服务器繁忙或维护中，暂时无法继续提供服务，请10分钟后来尝试~",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 20.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = "但请注意，如果仅有您自己出现了这种情况，那很有可能是您本身触发了风控，请合理使用，避免为平台和账户带来风险~",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 20.dp),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onRetry,
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
            shape = CardDefaults.shape,
        ) {
            Text(text = "重试")
        }

        // 退出APP的按钮
        OutlinedButton(
            onClick = {
                killProcess(android.os.Process.myPid())
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            shape = CardDefaults.shape,
        ) {
            Text(text = "退出应用")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestFrequentScaffold(
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        content(it)
    }

}