package com.imcys.bilibilias.weight.copyright

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.ui.weight.ASIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCopyrightApplyScreen(
    onNavigateBack: () -> Unit = {},
    onSubmit: (email: String, verificationCode: String) -> Unit = { _, _ -> }
) {
    var email by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_text_6296)) },
                navigationIcon = {
                    ASIconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState), // 使内容可滚动
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
//                    InfoItem(
//                        title = "邮箱验证:",
//                        description = "您需要提供有效的邮箱地址以接收重要通知。为确保您能持续收到通知，我们将每隔一个月对您的邮箱进行验证，一个月后缓存将提示您重新验证邮箱。"
//                    )
                    InfoItem(
                        title = stringResource(R.string.home_text_8843),
                        description = stringResource(R.string.home_cache_benefits)
                    )
                    InfoItem(
                        title = stringResource(R.string.home_text_7171),
                        description = stringResource(R.string.home_cache_2)
                    )
                    InfoItem(
                        title = stringResource(R.string.home_text_9026),
                        description = stringResource(R.string.home_text_9232)
                    )
                    InfoItem(
                        title = stringResource(R.string.home_text_4780),
                        description = stringResource(R.string.home_freeze)
                    )
                }
            }


            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            ) {
                Text(stringResource(R.string.home_text_6001))
            }
        }
    }
}

@Composable
fun InfoItem(title: String, description: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp // 增加行高以便阅读
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVideoCopyrightApplyScreen() {
    MaterialTheme { // 确保在预览中使用 MaterialTheme
        VideoCopyrightApplyScreen(onNavigateBack = {}, onSubmit = { _, _ -> })
    }
}

