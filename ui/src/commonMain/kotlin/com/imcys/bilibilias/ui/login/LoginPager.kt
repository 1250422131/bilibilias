package com.imcys.bilibilias.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.outlined.Cookie
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.logic.login.CookieAction
import com.imcys.bilibilias.logic.login.CookieLoginState
import com.imcys.bilibilias.logic.login.QrCodeLoginAction
import com.imcys.bilibilias.logic.login.QrCodeLoginState
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
internal fun PagerScope.QrContent(
    state: QrCodeLoginState,
    dispatch: (QrCodeLoginAction) -> Unit,
) {
    when (state) {
        is QrCodeLoginState.Error -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(state.errorMessage)
            }
        }

        QrCodeLoginState.LoginSuccess -> {}
        is QrCodeLoginState.QRCodeReady -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("正在生成二维码。。。")
            }
        }

        is QrCodeLoginState.Content ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "使用 bilibili 官方 App 扫码登录",
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "剩余有效时间: ${state.lifeTime} 秒",
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.clickable { dispatch(QrCodeLoginAction.GenerateQRCode) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh QR Code",
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "刷新二维码", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    Row(
                        modifier = Modifier.clickable { },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SaveAlt,
                            contentDescription = "Save to Album",
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "保存至相册", fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val painter = rememberQrCodePainter(data = state.url)
                Spacer(modifier = Modifier.height(30.dp))
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(150.dp).clickable(null, null) {
                        dispatch(QrCodeLoginAction.GenerateQRCode)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = state.message, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Spacer to push the final text to the bottom
                Spacer(modifier = Modifier.weight(1f))

                // 4. Bottom Warning Message
                Text(
                    text = "请务必在 BilibiliAs 开源仓库等可信渠道下载安装。",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

        else -> {}
    }
}

@Composable
internal fun PagerScope.CookieContent(
    cookieLoginState: CookieLoginState,
    dispatch: (CookieAction) -> Unit,
    onBack: () -> Unit,
) {
    SideEffect {
        if (cookieLoginState.success) {
//            onBack()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "使用Cookie登录",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = cookieLoginState.text,
            onValueChange = { dispatch((CookieAction.Changed(it))) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cookie") },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Cookie,
                    contentDescription = "Cookie Icon"
                )
            },
            trailingIcon = {
                if (cookieLoginState.text.isNotEmpty()) {
                    IconButton(onClick = { dispatch(CookieAction.Changed("")) }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear text"
                        )
                    }
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedButton(
            onClick = {
                if (cookieLoginState.text.isNotBlank()) {
                    dispatch(CookieAction.TryLogin)
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(180.dp),
            shape = RoundedCornerShape(50)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Login Arrow"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "登录", fontSize = 16.sp)
            }
        }
    }
}