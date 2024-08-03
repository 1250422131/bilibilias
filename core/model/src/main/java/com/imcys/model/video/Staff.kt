package com.imcys.model.video

import com.imcys.model.Label
import com.imcys.model.Official
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Staff(
    @SerialName("face")
    val face: String = "",
    @SerialName("follower")
    val follower: Int = 0,
    @SerialName("label_style")
    val labelStyle: Int = 0,
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("official")
    val official: Official = Official(),
    @SerialName("title")
    val title: String = "",
    @SerialName("vip")
    val vip: Vip = Vip()
)
@Serializable
data class Vip(
    @SerialName("avatar_subscript")
    val avatarSubscript: Int = 0,
    @SerialName("avatar_subscript_url")
    val avatarSubscriptUrl: String = "",
    @SerialName("due_date")
    val dueDate: Long = 0,
    @SerialName("label")
    val label: Label = Label(),
    @SerialName("nickname_color")
    val nicknameColor: String = "",
    @SerialName("role")
    val role: Int = 0,
    @SerialName("status")
    val status: Int = 0,
    @SerialName("theme_type")
    val themeType: Int = 0,
    @SerialName("tv_vip_pay_type")
    val tvVipPayType: Int = 0,
    @SerialName("tv_vip_status")
    val tvVipStatus: Int = 0,
    @SerialName("type")
    val type: Int = 0,
    @SerialName("vip_pay_type")
    val vipPayType: Int = 0
)