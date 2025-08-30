package com.imcys.bilibilias.core.data.util

import com.imcys.bilibilias.core.data.model.MessageData
import com.imcys.bilibilias.core.data.model.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Interface implementation for handling general errors.
 */

class StateErrorMonitor : ErrorMonitor {
    /**
     * List of [MessageData] to be shown
     */
    override val messages = MutableStateFlow<List<MessageData>>(emptyList())

    /**
     * Creates a [MessageData] and adds it to the list.
     * @param message: String value for message to add.
     */
    override fun addMessageByString(message: String): MessageData {
        val data = MessageData(type = MessageType.MESSAGE(message))
        messages.update { it + data }

        return data
    }

    /**
     * Add a [MessageData] to the list.
     * @param message: [MessageData] to add.
     */
    override fun addMessageByData(message: MessageData) {
        messages.update { it + message }
    }

    /**
     * Removes the [MessageData] from the list.
     */
    override fun clearMessage(message: MessageData) {
        messages.update { list -> list.filterNot { it == message } }
    }

    /**
     * Remove all from list, reset to empty list
     */
    override fun clearAllMessages() {
        messages.update { emptyList() }
    }
}