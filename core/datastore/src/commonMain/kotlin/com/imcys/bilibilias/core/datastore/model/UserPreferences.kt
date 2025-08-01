package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val selfInfo: SelfInfo?,
) {
    companion object {
        val INIT = UserPreferences(selfInfo = null)
    }
}
