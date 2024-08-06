package com.imcys.bilibilias.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Interface implementation for handling general errors.
 */

class SnackbarErrorMonitor @Inject constructor(val networkMonitor: NetworkMonitor) : ErrorMonitor {
    /**
     * List of [ErrorMessage] to be shown to the user, via Snackbar.
     */
    private val errorMessages = MutableStateFlow<List<ErrorMessage>>(emptyList())

    override val isOffline = networkMonitor.isOnline
        .map(Boolean::not)

    override var offlineMessage: String? = null

    override val errorMessage: Flow<ErrorMessage?> =
        combine(errorMessages, isOffline) { messages, isOffline ->
            // Offline Error Message takes precedence over other messages
            if (isOffline) {
                ErrorMessage(
                    offlineMessage ?: "You are offline",
                    duration = MessageDuration.Indefinite,
                )
            } else {
                messages.firstOrNull()
            }
        }

    /**
     * Creates an [ErrorMessage] from String value and adds it to the list.
     *
     * @param error: String value of the error message.
     *
     * Returns the ID of the new [ErrorMessage] if success
     * Returns null if [error] is Blank
     */
    private fun addErrorMessage(
        error: String,
        type: MessageType,
        duration: Duration,
        actionPerformed: (() -> Unit)?,
    ): String? {
        if (error.isNotBlank()) {
            val newError = ErrorMessage(
                message = error,
                messageType = type,
                duration = duration,
                actionPerformed = actionPerformed,
            )
            errorMessages.update { it + newError }
            return newError.id
        }
        return null
    }

    override fun addShortErrorMessage(
        error: String,
        type: MessageType,
        label: String?,
        action: (() -> Unit)?,
    ): String? = addErrorMessage(error, type, MessageDuration.Short, action)

    override fun addLongErrorMessage(
        error: String,
        type: MessageType,
        label: String?,
        action: (() -> Unit)?,
    ): String? = addErrorMessage(error, type, MessageDuration.Long, action)

    override fun addIndefiniteErrorMessage(
        error: String,
        type: MessageType,
        label: String?,
        action: (() -> Unit)?,
    ): String? = addErrorMessage(error, type, MessageDuration.Indefinite, action)

    /**
     * Removes the [ErrorMessage] with the specified [id] from the list.
     */
    override fun clearErrorMessage(id: String) {
        errorMessages.update { it.filter { item -> item.id != id } }
    }
}

/**
 * Models the data needed for an error message to be displayed and tracked.
 */
data class ErrorMessage(
    val message: String,
    val id: String = UUID.randomUUID().toString(),
    val duration: Duration = MessageDuration.Default,
    val messageType: MessageType = MessageType.Normal,
    val actionPerformed: (() -> Unit)? = null,
)

enum class MessageType {
    Normal,
    Success,
    Info,
    Warning,
    Error,
}

object MessageDuration {
    val Short = 2000.milliseconds

    val Default = 4000.milliseconds

    val Long = 8000.milliseconds

    val Indefinite = kotlin.Long.MAX_VALUE.milliseconds
}
