package com.imcys.bilibilias.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle


@Composable
internal fun LoginRoute(
    onToBack: () -> Unit,
    goToQRCodeLogin: () -> Unit,
) {
    LoginScreen(onToBack, goToQRCodeLogin)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onToBack: () -> Unit, goToQRCodeLogin: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text("登录")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            onToBack.invoke()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    }
                )
            }

        },

        ) {

        Box(Modifier.padding(it)) {
            Column(Modifier.padding(24.dp)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        OutlinedCard(
                            Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp)
                            ) {
                                Text("登录后你可以：", fontSize = 14.sp)
                                Text(
                                    """
                                ·支持更高清、更高码率的视频
                                ·支持番剧缓存
                                ·支持使用大会员权益
                                ·支持享受漫游服务
                            """.trimIndent(), fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        OutlinedCard(
                            Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp)
                            ) {
                                Text("登录即代表你同意：", fontSize = 14.sp)
                                Text(
                                    "·哔哩哔哩账户和社区相关协议", fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "·《BILIBILIAS 用户协议》和《BILIBILIAS 隐私协议》",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "·使用造成的一切后果 BILIBILIAS 概不负责", fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = CardDefaults.shape,
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = contentColorFor(MaterialTheme.colorScheme.tertiaryContainer),
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp)
                            ) {
                                Text(
                                    "特别的：\n本程序未得到哔哩哔哩授权，使用造成的一切后果哔哩哔哩概不负责",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                    shape = CardDefaults.shape,
                    onClick = { goToQRCodeLogin.invoke() }
                ) {
                    Text(
                        "B站 扫码登录",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 16.sp
                    )
                }
            }

        }
    }
}
