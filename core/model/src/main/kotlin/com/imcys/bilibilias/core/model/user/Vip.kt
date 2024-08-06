package com.imcys.bilibilias.core.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vip(
    @SerialName("avatar_icon")
    val avatarIcon: AvatarIcon = AvatarIcon(),
    @SerialName("avatar_subscript")
    val avatarSubscript: Int = 0,
    @SerialName("avatar_subscript_url")
    val avatarSubscriptUrl: String = "",
    @SerialName("due_date")
    val dueDate: Long = 0,
    @SerialName("nickname_color")
    val nicknameColor: String = "",
    @SerialName("role")
    val role: Int = 0,
    @SerialName("status")
    val status: Int = 0,
    @SerialName("theme_type")
    val themeType: Int = 0,
    @SerialName("tv_due_date")
    val tvDueDate: Int = 0,
    @SerialName("tv_vip_pay_type")
    val tvVipPayType: Int = 0,
    @SerialName("tv_vip_status")
    val tvVipStatus: Int = 0,
    @SerialName("type")
    val type: Int = 0,
    @SerialName("vip_pay_type")
    val vipPayType: Int = 0,
) {
    @Serializable
    data class AvatarIcon(
        @SerialName("icon_type")
        val iconType: Int = 0,
    )
}
