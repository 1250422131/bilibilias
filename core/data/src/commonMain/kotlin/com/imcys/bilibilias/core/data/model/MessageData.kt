package com.imcys.bilibilias.core.data.model

/**
 * [MessageData]
 * Class to hold messages type objects with actions
 */
data class MessageData(
    val type: MessageType,
    val label: String? = null,
    val onConfirm: (() -> Unit)? = null,
    val onDelay: (() -> Unit)? = null,
)

/**
 * Specified Errors
 */
sealed class MessageType {
    data object OFFLINE : MessageType()
    data class MESSAGE(val value: String) : MessageType()
    data object UNKNOWN : MessageType()
}