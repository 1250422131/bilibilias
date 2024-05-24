package com.imcys.bilibilias.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imcys.bilibilias.core.designsystem.component.AsButton
import io.github.alexzhirkevich.qrose.toImageBitmap

@Composable
fun LoginContent(
    component: LoginComponent,
    onNavigationToRoot: () -> Unit
) {
//    val model by component.models.collectAsStateWithLifecycle()
//    LoginContent(model = model, onNavigationToRoot = onNavigationToRoot, onEvent = component::take)
}

@Composable
private fun LoginContent(
    model: LoginModel,
    onEvent: (LoginEvent) -> Unit,
    onNavigationToRoot: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SideEffect {
                if (model.isSuccess) {
                    onNavigationToRoot()
                }
            }
            val context = LocalContext.current
            LaunchedEffect(model.qrCodePainter) {
                val bitmap = model.qrCodePainter.toImageBitmap(2000, 2000)
                    .asAndroidBitmap()
                    .addWhiteBorder(200)
                QRUtil.saveQRCode(bitmap, context)
            }
            Image(
                painter = model.qrCodePainter,
                contentDescription = "登录二维码",
                modifier = Modifier
                    .size(130.dp)
                    .clickable { onEvent(LoginEvent.RefreshQrCode) }
            )
            Text(text = model.message)
            val modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .height(60.dp)
            AsButton(onClick = { QRUtil.goToQRScan(context) }, modifier = modifier) {
                Text(text = "跳转扫码")
            }
            LoginNotice()
        }
    }
}

@Composable
fun LoginNotice() {
    Column(modifier = Modifier.padding(20.dp)) {
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
        Text(text = "使用则代表你同意", modifier = Modifier.align(Alignment.CenterHorizontally))
        val color = MaterialTheme.colorScheme.primary
        val view = LocalView.current
        Text(
            text = "· 哔哩哔哩账户和社区相关协议",
            modifier = Modifier.clickable { LoginRequiresInfo.toBiliAgreement(view) },
            color = color
        )
        Text(
            text = "·《BILIBILIAS用户协议》和《BILIBILIAS隐私协议》",
            modifier = Modifier.clickable { LoginRequiresInfo.toBilibiliAsAgreement(view) },
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
