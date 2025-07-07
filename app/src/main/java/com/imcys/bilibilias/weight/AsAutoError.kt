package com.imcys.bilibilias.weight

import android.content.ClipData
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.ClipMetadata
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun <T> AsAutoError(
    netWorkResult: NetWorkResult<T>,
    onLoadingContent: (@Composable () -> Unit)? = null,
    onDefaultContent: (@Composable () -> Unit)? = onLoadingContent,
    onSuccessContent: @Composable () -> Unit = {},
    onErrorContent: (@Composable () -> Unit)? = null,
    onRetry: () -> Unit = {},
) = AnimatedContent(
    targetState = netWorkResult.status,
    transitionSpec = {
        fadeIn(
            animationSpec = tween(durationMillis = 300)
        ) togetherWith fadeOut(
            animationSpec = tween(durationMillis = 300)
        )
    },
) { targetUiState ->
    when (targetUiState) {
        ApiStatus.SUCCESS -> onSuccessContent()
        ApiStatus.ERROR -> onErrorContent?.invoke() ?: CommonError(
            netWorkResult.errorMsg ?: "",
            onRetry
        )

        ApiStatus.LOADING -> onLoadingContent?.invoke() ?: onSuccessContent()
        ApiStatus.DEFAULT -> onDefaultContent?.invoke() ?: onSuccessContent()
    }
}


@Composable
@Preview
private fun PreviewCommonError() {
    CommonError("接口异常") { }
}

@Composable
private fun CommonError(errorMsg: String, onRetry: () -> Unit) {
    val clipboardManager = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

    var copyFinish by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (copyFinish) 360f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = CardDefaults.shape,
            color = MaterialTheme.colorScheme.errorContainer
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier
                        .sizeIn(maxHeight = 100.dp)
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text("Error：${errorMsg}")
                }
                IconButton(onClick = {
                    val clipData = ClipData.newPlainText("BILIBILAIS异常", "$errorMsg")
                    val clipEntry = ClipEntry(clipData)
                    coroutineScope.launch(Dispatchers.IO) {
                        copyFinish = true
                        clipboardManager.setClipEntry(clipEntry)
                        delay(2000)
                        copyFinish = false
                    }
                }) {

                    Icon(
                        imageVector = if (copyFinish) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                        contentDescription = "复制报错",
                        modifier = Modifier.rotate(rotation)
                    )

                }
            }
        }

        Spacer(Modifier.height(5.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = CardDefaults.shape,
                onClick = onRetry,
            ) {
                Text("点击重试")
            }

        }
    }

}