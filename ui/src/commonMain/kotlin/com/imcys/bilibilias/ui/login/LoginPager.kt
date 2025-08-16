package com.imcys.bilibilias.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imcys.bilibilias.logic.login.CookieAction
import com.imcys.bilibilias.logic.login.CookieLoginState
import com.imcys.bilibilias.logic.login.LoginAction
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
internal fun PagerScope.LoginByQr(
    url: String,
    timeLeftInSeconds: Int,
    qrStatusMessage: String,
    authCodeUrl: String, // The descriptive URL below the QR
    onRefreshQr: () -> Unit,
    onSaveQr: () -> Unit,
    onQrCodeScanned: () -> Unit, // Example action
    dispatch: (LoginAction) -> Unit
) {
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
            text = "剩余有效时间: 170 秒",
            fontSize = 14.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh QR Code",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "刷新二维码", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SaveAlt,
                    contentDescription = "Save to Album",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "保存至相册", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val painter = rememberQrCodePainter(data = url)
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(150.dp).clickable(null, null) {
//                dispatch(LoginAction.RequestNewQrCode)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "二维码尚未确认",
            fontSize = 16.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "https://passport.bilibili.com/x/passport-tv-login/h5/qrcode/auth?auth_code=6b9d9624b2fb37a7373289b934145a31&mobi_app=android_hd",
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

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
}

@Composable
internal fun PagerScope.CookieContent(
    cookieLoginState: CookieLoginState,
    dispatch: (CookieAction) -> Unit
) {
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