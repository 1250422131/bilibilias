package com.sockmagic.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.imcys.bilibilias.core.designsystem.component.AsButton
import io.github.alexzhirkevich.qrose.toImageBitmap

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = getViewModel()
        val model by viewModel.models.collectAsState()
        LoginContent(model, viewModel::take)
    }
}

@Composable
private fun LoginContent(model: LoginModel, onEvent: (Unit) -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    .padding(top = 100.dp)
                    .size(130.dp)
            )
            Text(text = model.message)
            val modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .height(60.dp)
            AsButton(onClick = { /*TODO*/ }, modifier = modifier) {
                Text(text = "下载二维码")
            }
            AsButton(onClick = { /*TODO*/ }, modifier = modifier) {
                Text(text = "跳转扫码")
            }
        }
    }
}
