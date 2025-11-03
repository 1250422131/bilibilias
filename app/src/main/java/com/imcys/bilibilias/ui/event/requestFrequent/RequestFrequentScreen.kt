package com.imcys.bilibilias.ui.event.requestFrequent


import androidx.compose.ui.res.stringResource
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
                    Text(stringResource(R.string.app_testing_request), modifier = Modifier.padding(top = 16.dp))
                }
            }

            RequestFrequentUIState.Success -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.app_success), modifier = Modifier.padding(top = 16.dp))
                    Button(onClick = onToBack) {
                        Text(stringResource(R.string.app_back))
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
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(R.string.app_emergency_event),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 20.dp),
        )
        Text(
            text = stringResource(R.string.app_continue),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 20.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.app_yes_4),
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
            Text(text = stringResource(R.string.login_retry))
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
            Text(text = stringResource(R.string.app_exit_1))
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