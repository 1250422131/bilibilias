package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable

/**
 * User preferences.
 *
 * @property enableGuestHighQualityVideo Boolean
 */
@Serializable
data class UserPreferences(
    val selfInfo: SelfInfo?,
    val enableGuestHighQualityVideo: Boolean
) {
    companion object {
        val DEFAULT = UserPreferences(
            selfInfo = null,
            enableGuestHighQualityVideo = false
        )
    }
}
