package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable

/**
 * User preferences.
 *
 * @property enableTryLook 免登录查看1080P视频
 */
@Serializable
data class UserPreferences(
    val selfInfo: SelfInfo?,
    val enableTryLook: Boolean,
    val codecPriorityList: List<Codecs>
) {
    companion object {
        val DEFAULT = UserPreferences(
            selfInfo = null,
            enableTryLook = false,
            codecPriorityList = Codecs.entries
        )
    }
}
