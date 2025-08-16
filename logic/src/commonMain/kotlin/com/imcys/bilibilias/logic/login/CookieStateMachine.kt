package com.imcys.bilibilias.logic.login

import com.freeletics.flowredux2.FlowReduxStateMachineFactory
import com.freeletics.flowredux2.initializeWith
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.CookieJarDataSource
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.core.logging.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class CookieStateMachine(
    private val cookieJarDataSource: CookieJarDataSource,
    private val preferences: AsPreferencesDataSource,
    private val applicationScope: CoroutineScope,
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
                        val profile = BilibiliApi.getUserProfile(snapshot.text)
                        if (profile.mid != 0L) {
                            cookieJarDataSource.add(snapshot.text)
                            override { copy(success = true) }
                        } else {
                            override {
                                copy(
                                    success = false,
                                    message = "无法获取用户信息，请检查Cookie"
                                )
                            }
                        }
                    } catch (e: Exception) {
                        logger.error(e) { "Login failed with unexpected error" }
                        override {
                            copy(
                                success = false,
                                message = e.message ?: "登录时发生未知错误"
                            )
                        }
                    }
                }
                onActionEffect<CookieAction.TryLogin> {
                    applicationScope.launch {
                        val data = BilibiliApi.getNavigationData()
                        preferences.setSelfInfo(
                            SelfInfo(
                                Uuid.random(),
                                data.mid,
                                data.uname,
                                data.face
                            )
                        )
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