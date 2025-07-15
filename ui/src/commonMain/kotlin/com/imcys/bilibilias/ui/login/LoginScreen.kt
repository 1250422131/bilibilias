package com.imcys.bilibilias.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imcys.bilibilias.logic.login.LoginComponent
import com.imcys.bilibilias.logic.login.LoginResultUiState
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
fun LoginScreen(component: LoginComponent) {
    val url by component.qrCodeUrl.collectAsState()
    val uiState by component.uiState.collectAsState()
    LaunchedEffect(Unit) {
        component.initiateLogin()
    }
    LoginContent(uiState, url)
}

@Composable
fun LoginContent(uiState: LoginResultUiState, url: String) {
    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            val painter = rememberQrCodePainter(data = url)

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}