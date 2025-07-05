package com.imcys.bilibilias.common.event

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object LoginError

// Channel 可以重复推送
private val _loginErrorChannel = Channel<LoginError>(Channel.UNLIMITED)
val loginErrorChannel = _loginErrorChannel.receiveAsFlow()

fun sendLoginErrorEvent() {
    _loginErrorChannel.trySend(LoginError)
}