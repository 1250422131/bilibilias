package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class TokenSave internal constructor(
    val refreshToken: String? = null,
) {
    companion object {
        val INIT = TokenSave()
    }
}