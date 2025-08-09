package com.imcys.bilibilias.logic.login

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.freeletics.flowredux2.FlowReduxStateMachineFactory
import com.freeletics.flowredux2.initializeWith
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.Expired
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.Success
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.WaitingConfirmation
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.WaitingScanned
import com.imcys.bilibilias.core.datasource.model.PollResponse
import com.imcys.bilibilias.core.datasource.persistent.TokenPersistent
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.core.logging.logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class LoginStateMachine(
    private val loginApi: BilibiliLoginApi,
    private val tokenPersistent: TokenPersistent,
    private val preferences: AsPreferencesDataSource,
) : FlowReduxStateMachineFactory<LoginState, LoginAction>(), Lifecycle.Callbacks {
    private var interrupt = false
    private val logger = logger<LoginStateMachine>()
    init {
        initializeWith { LoginState.GeneratingQrCode }
        spec {
            inState<LoginState.GeneratingQrCode> {
                onEnter {
                    try {
                        val (key, url) = loginApi.getQrcode()
                        override { LoginState.QrCodeReady(url, key) }
                    } catch (e: Exception) {
                        override {
                            LoginState.LoginFailed("Failed to generate QR code: ${e.message}")
                        }
                    }
                }
            }
            inState<LoginState.QrCodeReady> {
                onEnter {
                    override { LoginState.AwaitingConfirmation(qrKey) }
                }
            }
            inState<LoginState.AwaitingConfirmation> {
                collectWhileInState(
                    { timerThatEmitsEverySecond(it.key) }
                ) {
                    when (it.status) {
                        WaitingScanned,
                        WaitingConfirmation -> noChange()

                        Success -> {
                            tokenPersistent.setRefreshToken(it.refreshToken)
                            override { LoginState.LoginSuccessful }
                        }

                        Expired -> override { LoginState.GeneratingQrCode }
                        else -> override { LoginState.LoginFailed("Unexpected polling status.") }
                    }
                }
                on<LoginAction.RequestNewQrCode> {
                    override { LoginState.GeneratingQrCode }
                }
            }
            inState<LoginState.LoginSuccessful> {
                onEnterEffect {
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
            inState<LoginState.LoginFailed> {
                on<LoginAction.RequestNewQrCode> {
                    override { LoginState.GeneratingQrCode }
                }
            }
        }
    }

    override fun onCreate() {
        interrupt = false
    }

    override fun onDestroy() {
        interrupt = true
        loginApi.close()
    }

    private fun timerThatEmitsEverySecond(key: String): Flow<PollResponse> = flow {
        while (!interrupt) {
            val response = loginApi.pollRequest(key)
            emit(response)
            delay(1_000)
        }
    }
}

// --- States ---
sealed interface LoginState {
    data object GeneratingQrCode : LoginState
    data class QrCodeReady(val qrCodeUrl: String, val qrKey: String) : LoginState
    data class AwaitingConfirmation(val key: String) : LoginState
    data object LoginSuccessful : LoginState
    data class LoginFailed(val message: String) : LoginState
}

sealed interface LoginAction {
    data object RequestNewQrCode : LoginAction
}