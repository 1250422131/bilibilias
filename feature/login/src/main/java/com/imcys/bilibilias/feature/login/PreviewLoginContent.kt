package com.imcys.bilibilias.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import com.imcys.bilibilias.feature.login.component.LoginComponent
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
            ColorPainter(Color.Yellow)
        )
    )

    override fun take(event: LoginEvent) {
    }
}
