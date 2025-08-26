package com.imcys.bilibilias.weight.copyright

import androidx.compose.foundation.layout.Arrangement
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
                title = { Text("视频缓存使用申请") },
                navigationIcon = {
                    ASIconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
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
                        title = "重要通知:",
                        description = "我们可能会在APP内通知您删除部分或全部已缓存的视频。这可能出于保护内容创作者权益或应用服务调整的考虑，会在APP内向您阐述原因。"
                    )
                    InfoItem(
                        title = "配合删除:",
                        description = "收到删除通知后，请您在 24 小时内删除指定的缓存视频及其所有副本（不包括已获授权的转载和符合规定的二次创作视频）。"
                    )
                    InfoItem(
                        title = "操作确认:",
                        description = "删除操作完成后，您需要在应用内进行确认。所有指定操作确认完毕后，方可继续使用本应用。"
                    )
                    InfoItem(
                        title = "账户冻结:",
                        description = "若您在 15 天内未能完成确认操作，您的账户将被冻结，届时将无法继续使用本应用。"
                    )
                }
            }


            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {}
            ) {
                Text("我已阅读并同意相关条款")
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

