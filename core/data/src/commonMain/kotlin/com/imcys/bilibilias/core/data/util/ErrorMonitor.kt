package com.imcys.bilibilias.core.data.util

import com.imcys.bilibilias.core.data.model.MessageData
import kotlinx.coroutines.flow.Flow

/**
 * Interface for handling messages.
 */
interface ErrorMonitor {
    val messages: Flow<List<MessageData?>>

    fun addMessageByString(message: String): MessageData

    fun addMessageByData(message: MessageData)

    fun clearMessage(message: MessageData)

    fun clearAllMessages()
}