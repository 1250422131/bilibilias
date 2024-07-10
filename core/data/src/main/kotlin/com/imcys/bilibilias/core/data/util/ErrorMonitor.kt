package com.imcys.bilibilias.core.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Interface for handling error messages.
 */
interface ErrorMonitor {
    fun addShortErrorMessage(
        error: String,
        type: MessageType = MessageType.Normal,
        label: String? = null,
        action: (() -> Unit)? = null,
    ): String?

    fun addLongErrorMessage(
        error: String,
        type: MessageType = MessageType.Normal,
        label: String? = null,
        action: (() -> Unit)? = null,
    ): String?

    fun addIndefiniteErrorMessage(
        error: String,
        type: MessageType = MessageType.Normal,
        label: String? = null,
        action: (() -> Unit)? = null,
    ): String?

    fun clearErrorMessage(id: String)
    val errorMessage: Flow<ErrorMessage?>
    val isOffline: Flow<Boolean>
    var offlineMessage: String?
}
