package com.imcys.bilibilias.ui.event.playvoucher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import org.koin.compose.viewmodel.koinViewModel


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayVoucherErrorPage(onBlack:()-> Unit = {}) {
    val vm = koinViewModel<PlayVoucherErrorViewModel>()
    Scaffold(
        topBar = {
            ASTopAppBar(
                style = BILIBILIASTopAppBarStyle.Small,
                title = { Text("风控须知") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "账户风险提示",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                val points = listOf(
                    "您的账号可能正处于哔哩哔哩平台风控监测，可能与曾使用违规第三方程序、异常使用行为或其他违规操作有关，因此被要求进行身份验证。",
                    "检测到您的 TV 端身份验证状态存在异常。BILIBILIAS 目前可尝试为您移除 TV 端的身份信息，以协助恢复基本使用。",
                    "该功能为临时辅助能力，可能随时被取消或收回。",
                    "继续使用具有违规风险的第三方工具，可能加重账号风险（功能受限、封禁等）。",
                    "我们可能对异常账号进行标记；必要时可冻结其使用 BILIBILIAS 的权限。",
                    "请在操作前充分理解上述风险，所有后果由您自行承担。"
                )
                items(points.size) { index ->
                    Text(
                        text = "• ${points[index]}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    Text(
                        text = "点击下方按钮即表示您已阅读并知晓以上内容。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Button(
                onClick = {
                    vm.ontUseTVVoucherInfo()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("我已知晓")
            }
        }
    }
}