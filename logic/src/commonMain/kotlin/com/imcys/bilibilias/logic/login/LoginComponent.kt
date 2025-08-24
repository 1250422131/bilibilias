package com.imcys.bilibilias.logic.login

import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.logic.root.AppComponentContext

interface LoginComponent {
    val cookieStateMachine: CookieStateMachine
    val qrCodeStateMachine: QrCodeLoginStateMachine
}

class DefaultLoginComponent(
    componentContext: AppComponentContext,
    override val cookieStateMachine: CookieStateMachine,
    override val qrCodeStateMachine: QrCodeLoginStateMachine,
) : LoginComponent, AppComponentContext by componentContext {
    companion object {
        private val logger = logger<LoginComponent>()
    }
}