package com.imcys.bilibilias.network.model


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class BILILoginUserInfo(
    @SerialName("face")
    val face: String?,
    @SerialName("isLogin")
    val isLogin: Boolean?,
    @SerialName("level_info")
    val levelInfo: LevelInfo?,
    @SerialName("mid")
    val mid: Long?,
    @SerialName("uname")
    val uname: String?,
    @SerialName("wbi_img")
    val wbiImg: WbiImg?
) {
    @Serializable
    data class LevelInfo(
        @SerialName("current_exp")
        val currentExp: Int,
        @SerialName("current_level")
        val currentLevel: Int,
        @SerialName("current_min")
        val currentMin: Int,
        @SerialName("next_exp")
        val nextExp: String
    )

    @Serializable
    data class Official(
        @SerialName("desc")
        val desc: String,
        @SerialName("role")
        val role: Int,
        @SerialName("title")
        val title: String,
        @SerialName("type")
        val type: Int
    )

    @Serializable
    data class OfficialVerify(
        @SerialName("desc")
        val desc: String,
        @SerialName("type")
        val type: Int
    )

    @Serializable
    data class Pendant(
        @SerialName("expire")
        val expire: Int,
        @SerialName("image")
        val image: String,
        @SerialName("image_enhance")
        val imageEnhance: String,
        @SerialName("image_enhance_frame")
        val imageEnhanceFrame: String,
        @SerialName("name")
        val name: String,
        @SerialName("pid")
        val pid: Int
    )

    @Serializable
    data class Vip(
        @SerialName("avatar_subscript")
        val avatarSubscript: Int,
        @SerialName("avatar_subscript_url")
        val avatarSubscriptUrl: String,
        @SerialName("due_date")
        val dueDate: Long,
        @SerialName("label")
        val label: Label,
        @SerialName("nickname_color")
        val nicknameColor: String,
        @SerialName("role")
        val role: Int,
        @SerialName("status")
        val status: Int,
        @SerialName("theme_type")
        val themeType: Int,
        @SerialName("tv_due_date")
        val tvDueDate: Int,
        @SerialName("tv_vip_pay_type")
        val tvVipPayType: Int,
        @SerialName("tv_vip_status")
        val tvVipStatus: Int,
        @SerialName("type")
        val type: Int,
        @SerialName("vip_pay_type")
        val vipPayType: Int
    ) {
        @Serializable
        data class Label(
            @SerialName("bg_color")
            val bgColor: String,
            @SerialName("bg_style")
            val bgStyle: Int,
            @SerialName("border_color")
            val borderColor: String,
            @SerialName("img_label_uri_hans")
            val imgLabelUriHans: String,
            @SerialName("img_label_uri_hans_static")
            val imgLabelUriHansStatic: String,
            @SerialName("img_label_uri_hant")
            val imgLabelUriHant: String,
            @SerialName("img_label_uri_hant_static")
            val imgLabelUriHantStatic: String,
            @SerialName("label_theme")
            val labelTheme: String,
            @SerialName("path")
            val path: String,
            @SerialName("text")
            val text: String,
            @SerialName("text_color")
            val textColor: String,
            @SerialName("use_img_label")
            val useImgLabel: Boolean
        )
    }

    @Serializable
    data class VipLabel(
        @SerialName("bg_color")
        val bgColor: String,
        @SerialName("bg_style")
        val bgStyle: Int,
        @SerialName("border_color")
        val borderColor: String,
        @SerialName("img_label_uri_hans")
        val imgLabelUriHans: String,
        @SerialName("img_label_uri_hans_static")
        val imgLabelUriHansStatic: String,
        @SerialName("img_label_uri_hant")
        val imgLabelUriHant: String,
        @SerialName("img_label_uri_hant_static")
        val imgLabelUriHantStatic: String,
        @SerialName("label_theme")
        val labelTheme: String,
        @SerialName("path")
        val path: String,
        @SerialName("text")
        val text: String,
        @SerialName("text_color")
        val textColor: String,
        @SerialName("use_img_label")
        val useImgLabel: Boolean
    )

    @Serializable
    data class Wallet(
        @SerialName("bcoin_balance")
        val bcoinBalance: Int,
        @SerialName("coupon_balance")
        val couponBalance: Int,
        @SerialName("coupon_due_time")
        val couponDueTime: Int,
        @SerialName("mid")
        val mid: Int
    )

    @Serializable
    data class WbiImg(
        @SerialName("img_url")
        val imgUrl: String,
        @SerialName("sub_url")
        val subUrl: String
    )
}

@Serializable
data class TVBILILoginUserInfo(
    @SerialName("face")
    val face: String?,
    @SerialName("level")
    val level: Int?,
    @SerialName("mid")
    val mid: Long?,
    @SerialName("name")
    val name: String?,
)
typealias APPBILILoginUserInfo = TVBILILoginUserInfo

