package com.imcys.model.video

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
) {
    @Serializable
    data class Official(
        @SerialName("desc")
        val desc: String = "",
        @SerialName("role")
        val role: Int = 0,
        @SerialName("title")
        val title: String = "",
        @SerialName("type")
        val type: Int = 0
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
    ) {
        @Serializable
        data class Label(
            @SerialName("bg_color")
            val bgColor: String = "",
            @SerialName("bg_style")
            val bgStyle: Int = 0,
            @SerialName("border_color")
            val borderColor: String = "",
            @SerialName("img_label_uri_hans")
            val imgLabelUriHans: String = "",
            @SerialName("img_label_uri_hans_static")
            val imgLabelUriHansStatic: String = "",
            @SerialName("img_label_uri_hant")
            val imgLabelUriHant: String = "",
            @SerialName("img_label_uri_hant_static")
            val imgLabelUriHantStatic: String = "",
            @SerialName("label_theme")
            val labelTheme: String = "",
            @SerialName("path")
            val path: String = "",
            @SerialName("text")
            val text: String = "",
            @SerialName("text_color")
            val textColor: String = "",
            @SerialName("use_img_label")
            val useImgLabel: Boolean = false
        )
    }
}