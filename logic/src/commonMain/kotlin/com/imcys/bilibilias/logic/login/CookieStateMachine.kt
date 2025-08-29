package com.imcys.bilibilias.logic.login

import com.freeletics.flowredux2.FlowReduxStateMachineFactory
import com.freeletics.flowredux2.initializeWith
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.logging.logger

class CookieStateMachine(
    private val api: BilibiliApi
) : FlowReduxStateMachineFactory<CookieLoginState, CookieAction>() {
    private val logger = logger<CookieStateMachine>()

    init {
        initializeWith { CookieLoginState("") }
        spec {
            inState<CookieLoginState> {
                on<CookieAction.Changed> {
                    mutate { CookieLoginState(it.text) }
                }
                on<CookieAction.TryLogin> {
                    try {
                        val profile = api.getUserProfile(snapshot.text)
                        if (profile.mid != 0L) {
                            api.setCookieFromSetCookieHeader(snapshot.text)
                            mutate { copy(success = true) }
                        } else {
                            mutate {
                                copy(
                                    success = false,
                                    message = "无法获取用户信息，请检查Cookie"
                                )
                            }
                        }
                    } catch (e: Exception) {
                        logger.error(e) { "Login failed with unexpected error" }
                        mutate {
                            copy(
                                success = false,
                                message = e.message ?: "登录时发生未知错误"
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CookieLoginState(
    val text: String,
    val success: Boolean = false,
    val message: String? = null
)

sealed interface CookieAction {
    data class Changed(val text: String) : CookieAction
    data object TryLogin : CookieAction
}