package com.imcys.bilibilias.logic.login

import com.freeletics.flowredux2.FlowReduxStateMachineFactory
import com.freeletics.flowredux2.initializeWith
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.Expired
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.Success
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.WaitingConfirmation
import com.imcys.bilibilias.core.datasource.model.OauthCode.Companion.WaitingScanned
import com.imcys.bilibilias.core.datasource.model.PollResponse
import com.imcys.bilibilias.core.datastore.TokenRepository
import com.imcys.bilibilias.core.logging.logger
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.saveImageToGallery
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QrCodeLoginStateMachine(
    private val loginApi: BilibiliLoginApi,
    private val tokenRepository: TokenRepository,
) : FlowReduxStateMachineFactory<QrCodeLoginState, QrCodeLoginAction>() {
    private val logger = logger<QrCodeLoginStateMachine>()
    private var shouldContinuePolling = true

    init {
        initializeWith { QrCodeLoginState.GeneratingQRCode }
        spec {
            inState<QrCodeLoginState.GeneratingQRCode> {
                onEnterEffect {
                    shouldContinuePolling = true
                }
                onEnter {
                    try {
                        val response = loginApi.getQrcode()
                        override { QrCodeLoginState.QRCodeReady(response.url, response.qrcodeKey) }
                    } catch (e: Exception) {
                        override { QrCodeLoginState.Error("Failed to generate QR code.", e) }
                    }
                }
            }

            inState<QrCodeLoginState.QRCodeReady> {
                onEnter {
                    override { QrCodeLoginState.Content(url, key, "") }
                }
            }
            inState<QrCodeLoginState.Content> {
                collectWhileInState({ poll(it.key) }) {
                    when (it.status) {
                        Success -> {
                            tokenRepository.setRefreshToken(it.refreshToken)
                            override { QrCodeLoginState.LoginSuccess }
                        }

                        WaitingScanned,
                        WaitingConfirmation -> mutate { copy(message = it.message) }

                        Expired -> {
                            shouldContinuePolling = false
                            mutate { copy(message = it.message, expired = true) }
                        }

                        else -> override { QrCodeLoginState.GeneratingQRCode }
                    }
                }
                collectWhileInState(tickerFlow(180)) {
                    mutate { copy(lifeTime = it.toInt()) }
                }
                on<QrCodeLoginAction.Generate> {
                    override { QrCodeLoginState.GeneratingQRCode }
                }
                onActionEffect<QrCodeLoginAction.SaveToAlbum> {
                    FileKit.saveImageToGallery(it.bytes, "BilibiliAs.png")
                }
            }
            inState<QrCodeLoginState.Error> {
                on<QrCodeLoginAction.Generate> {
                    override { QrCodeLoginState.GeneratingQRCode }
                }
            }
        }
    }

    private fun tickerFlow(start: Long, end: Long = 0L) = flow {
        for (i in start downTo end) {
            emit(i)
            delay(1_000)
        }
    }

    private fun poll(key: String): Flow<PollResponse> {
        return flow {
            while (shouldContinuePolling) {
                val response = loginApi.pollRequest(key)
                emit(response)
                delay(1_000)
            }
        }
    }
}

sealed interface QrCodeLoginState {
    // 正在生成二维码
    data object GeneratingQRCode : QrCodeLoginState

    // 二维码已就绪，可供扫描
    data class QRCodeReady(val url: String, val key: String) : QrCodeLoginState
    data class Content(
        val url: String,
        val key: String,
        val message: String,
        val expired: Boolean = false,
        val lifeTime: Int = 180,
    ) : QrCodeLoginState

    // 登录成功
    data object LoginSuccess : QrCodeLoginState

    // 发生错误
    data class Error(val errorMessage: String, val exception: Throwable? = null) : QrCodeLoginState
}

sealed interface QrCodeLoginAction {
    /**
     * 生成二维码
     */
    data object Generate : QrCodeLoginAction

    /**
     * 保存到相册
     */
    data class SaveToAlbum(val bytes: ByteArray) : QrCodeLoginAction {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SaveToAlbum

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }
}