package com.imcys.bilibilias.logic.login

import com.freeletics.flowredux2.FlowReduxStateMachineFactory
import com.freeletics.flowredux2.initializeWith
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.logging.logger
import kotlinx.io.IOException

class CookieStateMachine(
    private val api: BilibiliLoginApi,
) : FlowReduxStateMachineFactory<LoginState, CookieAction>() {
    private val logger = logger<CookieStateMachine>()

    init {
        initializeWith { InputtingCookie("") }
        spec {
            inState<InputtingCookie> {
                on<CookieAction.Changed> {
                    mutate { InputtingCookie(it.text) }
                }
                on<CookieAction.TryLogin> {
                    try {
                        val profile = api.getUserProfile()
                        if (profile.mid != 0L) {
                            override { LoginState.Success }
                        } else {
                            override { LoginState.Error("无法获取用户信息，请检查Cookie") }
                        }
                    } catch (e: IOException) {
                        logger.warn(e) { "Network issue during login" }
                        override { LoginState.Error("网络连接失败，请检查网络后重试") }
                    } catch (e: Exception) {
                        logger.error(e) { "Login failed with unexpected error" }
                        override { LoginState.Error(e.message ?: "登录时发生未知错误") }
                    }
                }
            }
        }
    }
}

data class InputtingCookie(val text: String) : LoginState
sealed interface CookieAction {
    data class Changed(val text: String) : CookieAction
    data class TryLogin(val text: String) : CookieAction
}