package com.imcys.bilibilias.common.event

import com.imcys.bilibilias.common.base.crash.AppException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object LoginError

// Channel 可以重复推送
private val _loginErrorChannel = Channel<LoginError>(Channel.UNLIMITED)
val loginErrorChannel = _loginErrorChannel.receiveAsFlow()

fun sendLoginErrorEvent() {
    _loginErrorChannel.trySend(LoginError)
}


private val _appErrorHandleChannel = Channel<AppException>(Channel.UNLIMITED)
val appErrorHandleChannel = _appErrorHandleChannel.receiveAsFlow()

fun sendAppErrorEvent(appException: AppException) {
    _appErrorHandleChannel.trySend(appException)
}


data class AnalysisEvent(
    val analysisText: String,
)

private val _analysisHandleChannel = Channel<AnalysisEvent>(Channel.UNLIMITED)
val analysisHandleChannel = _analysisHandleChannel.receiveAsFlow()

fun sendAnalysisEvent(analysisEvent: AnalysisEvent) {
    _analysisHandleChannel.trySend(analysisEvent)
}