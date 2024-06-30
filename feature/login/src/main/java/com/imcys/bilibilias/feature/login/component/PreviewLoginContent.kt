package com.imcys.bilibilias.feature.login.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import com.imcys.bilibilias.feature.login.LoginContent
import com.imcys.bilibilias.feature.login.LoginEvent
import com.imcys.bilibilias.feature.login.LoginModel
import io.github.alexzhirkevich.qrose.QrCodePainter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Preview
@Composable
private fun PreviewLoginContent() {
    LoginContent(component = PreviewComponent()) {
    }
}

private class PreviewComponent : LoginComponent {
    override val models: StateFlow<LoginModel> = MutableStateFlow(
        LoginModel(
            true,
            "hello",
            "https://github.com/alexzhirkevich/qrose",
        )
    )

    override fun take(event: LoginEvent) {
    }
}
