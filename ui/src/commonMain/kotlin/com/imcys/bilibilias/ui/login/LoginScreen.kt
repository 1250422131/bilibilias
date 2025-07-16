package com.imcys.bilibilias.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.core.context.LocalKmpContext
import com.imcys.bilibilias.core.context.Platform
import com.imcys.bilibilias.logic.login.LoginComponent
import com.imcys.bilibilias.logic.login.LoginResultUiState
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import kotlinx.coroutines.launch
import kotlin.time.Clock

@Composable
fun LoginScreen(component: LoginComponent) {
    val url by component.qrCodeUrl.collectAsState()
    val uiState by component.uiState.collectAsState()
    LaunchedEffect(Unit) {
        component.initiateLogin()
    }
    LoginContent(uiState, url)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(uiState: LoginResultUiState, url: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("扫码登录") },
                navigationIcon = {
                    IconButton({}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter = rememberQrCodePainter(data = url)
            Spacer(modifier = Modifier.height(30.dp))
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            val kmpContext = LocalKmpContext.current
            if (kmpContext.platform == Platform.ANDROID) {
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {
                        scope.launch {
                            savePainterToGallery(
                                kmpContext,
                                painter,
                                Clock.System.now().toEpochMilliseconds().toString()
                            )
                        }
                    }
                ) {
                    Text("保存二维码")
                }
            }
        }
    }
}

