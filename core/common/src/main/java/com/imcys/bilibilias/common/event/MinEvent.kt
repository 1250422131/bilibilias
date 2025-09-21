package com.imcys.bilibilias.common.event

import com.imcys.bilibilias.common.base.crash.AppException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object LoginError

// 登录校验异常
private val _loginErrorChannel = Channel<LoginError>(Channel.UNLIMITED)
val loginErrorChannel = _loginErrorChannel.receiveAsFlow()

fun sendLoginErrorEvent() {
    _loginErrorChannel.trySend(LoginError)
}

// 应用异常处理
private val _appErrorHandleChannel = Channel<AppException>(Channel.UNLIMITED)
val appErrorHandleChannel = _appErrorHandleChannel.receiveAsFlow()

fun sendAppErrorEvent(appException: AppException) {
    _appErrorHandleChannel.trySend(appException)
}


data class AnalysisEvent(
    val analysisText: String,
)

// 分析事件处理
private val _analysisHandleChannel = Channel<AnalysisEvent>(Channel.UNLIMITED)
val analysisHandleChannel = _analysisHandleChannel.receiveAsFlow()

fun sendAnalysisEvent(analysisEvent: AnalysisEvent) {
    _analysisHandleChannel.trySend(analysisEvent)
}


object PlayVoucherError
// 播放接口风控异常
private val _playVoucherErrorChannel = Channel<PlayVoucherError>(Channel.UNLIMITED)
val playVoucherErrorChannel = _playVoucherErrorChannel.receiveAsFlow()
fun sendPlayVoucherErrorEvent() {
    _playVoucherErrorChannel.trySend(PlayVoucherError)
}