package com.imcys.bilibilias.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.rememberBottomSheetState
import com.imcys.bilibilias.core.designsystem.component.AsButton
import com.imcys.bilibilias.core.designsystem.component.AsCard
import com.imcys.bilibilias.feature.login.component.LoginComponent
import io.github.alexzhirkevich.qrose.toImageBitmap
import kotlinx.coroutines.launch

@Composable
fun LoginContent(
    component: LoginComponent,
    navigationToTool: () -> Unit
) {
    val model by component.models.collectAsStateWithLifecycle()
    LoginContent(model = model, navigationToTool = navigationToTool, onEvent = component::take)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    model: LoginModel,
    onEvent: (LoginEvent) -> Unit,
    navigationToTool: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bilibili\n" + "扫码登录",
                        fontSize = 30.sp
                    )
                },
                modifier = Modifier
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .scrollable(rememberScrollState(), Orientation.Vertical),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SideEffect {
                if (model.isSuccess) {
                    navigationToTool()
                }
            }
            val context = LocalContext.current
            LaunchedEffect(model.qrCodePainter) {
                val bitmap = model.qrCodePainter.toImageBitmap(2000, 2000)
                    .asAndroidBitmap()
                    .addWhiteBorder(200)
                QRUtil.saveQRCode(bitmap, context)
            }
            AsCard(
                modifier = Modifier
                    .size(300.dp)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Image(
                    painter = model.qrCodePainter,
                    contentDescription = "登录二维码",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clickable { onEvent(LoginEvent.RefreshQrCode) },
                )
            }
            Text(
                text = model.message,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(16.dp)
            )

            Row {
                val sheetState = rememberBottomSheetState()
                val sheetState2 = rememberBottomSheetState()
                val coroutineScope = rememberCoroutineScope()
                AsButton(
                    onClick = {
                        coroutineScope.launch {
                            sheetState.expand()
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                ) {
                    Text(text = "登录详情")
                }
                LoginDetail(sheetState)
                AsButton(
                    onClick = {
                        coroutineScope.launch {
                            sheetState2.expand()
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                ) {
                    Text(text = "用户协议")
                }
                ProtocolBottomSheet(sheetState2)
            }
            AsButton(
                onClick = { QRUtil.goToQRScan(context) },
                modifier = Modifier
                    .padding(16.dp)
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "跳转扫码")
            }
        }
    }
}

@Composable
internal fun LoginDetail(state: BottomSheetState) {
    if (state.visible) {
        BottomSheet(
            state = state,
            skipPeeked = true,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AsCard {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "登录后，你可以：",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "· 为了良好体验，此版本必须完成登录")
                        Text(text = "· 云端账号加密保存，切换设备无需重复登录")
                        Text(text = "· 下载更加高清，更高码率的视频")
                        Text(text = "· 享受大会员权限视频和画质（如果你有）")
                        Text(text = "· 缓存番剧，并且将番剧导入Bilibili，实现携带弹幕的离线播放")
                    }
                }
            }
        }
    }
}

@Composable
internal fun ProtocolBottomSheet(state: BottomSheetState) {
    if (state.visible) {
        BottomSheet(
            state = state,
            skipPeeked = true,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AsCard {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "使用则代表你同意",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        val color = MaterialTheme.colorScheme.primary
                        val view = LocalView.current
                        Text(
                            text = "· 哔哩哔哩账户和社区相关协议",
                            modifier = Modifier.clickable {
                                LoginRequiresInfo.toBiliAgreement(
                                    view
                                )
                            },
                            color = color
                        )
                        Text(
                            text = "·《BILIBILIAS用户协议》和《BILIBILIAS隐私协议》",
                            modifier = Modifier.clickable {
                                LoginRequiresInfo.toBilibiliAsAgreement(
                                    view
                                )
                            },
                            color = color
                        )
                        Text(
                            text = "· 此APP没有得到Bilibili许可，Bilibili对此概不负责",
                            color = color
                        )
                        Text(
                            text = "· APP功能仅供学习使用参考",
                            color = color
                        )
                        Text(
                            text = "· 使用造成的任何后果BILIBILIAS概不负责",
                            color = color
                        )
                    }
                }
            }
        }
    }
}
