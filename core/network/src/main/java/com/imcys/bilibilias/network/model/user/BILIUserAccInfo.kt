package com.imcys.bilibilias.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 用户主页数据：仅Web接口，TV共用这个接口
 */
@Serializable
data class BILIUserAccInfo(
    @SerialName("birthday")
    val birthday: String,
    @SerialName("coins")
    val coins: Double,
    @SerialName("face")
    val face: String,
    @SerialName("face_nft")
    val faceNft: Long,
    @SerialName("face_nft_type")
    val faceNftType: Long,
    @SerialName("fans_badge")
    val fansBadge: Boolean,
    @SerialName("is_followed")
    val isFollowed: Boolean,
    @SerialName("jointime")
    val jointime: Long,
    @SerialName("level")
    val level: Long,
    @SerialName("mid")
    val mid: Long,
    @SerialName("moral")
    val moral: Long,
    @SerialName("name")
    val name: String,
    @SerialName("pendant")
    val pendant: Pendant,
    @SerialName("rank")
    val rank: Long,
    @SerialName("sex")
    val sex: String,
    @SerialName("sign")
    val sign: String,
    @SerialName("silence")
    val silence: Long,
    @SerialName("top_photo")
    val topPhoto: String,
    @SerialName("top_photo_v2")
    val topPhotoV2: TopPhotoV2,
    @SerialName("vip")
    val vip: Vip
) {
    @Serializable
    data class Pendant(
        @SerialName("expire")
        val expire: Long,
        @SerialName("image")
        val image: String,
        @SerialName("image_enhance")
        val imageEnhance: String,
        @SerialName("image_enhance_frame")
        val imageEnhanceFrame: String,
        @SerialName("n_pid")
        val nPid: Long,
        @SerialName("name")
        val name: String,
        @SerialName("pid")
        val pid: Long
    )

    @Serializable
    data class TopPhotoV2(
        @SerialName("l_200h_img")
        val l200hImg: String,
        @SerialName("l_img")
        val lImg: String,
        @SerialName("sid")
        val sid: Long
    )

    @Serializable
    data class Vip(
        @SerialName("avatar_icon")
        val avatarIcon: AvatarIcon,
        @SerialName("avatar_subscript")
        val avatarSubscript: Long,
        @SerialName("avatar_subscript_url")
        val avatarSubscriptUrl: String,
        @SerialName("due_date")
        val dueDate: Long,
        @SerialName("label")
        val label: Label,
        @SerialName("nickname_color")
        val nicknameColor: String,
        @SerialName("role")
        val role: Long,
        @SerialName("status")
        val status: Long,
        @SerialName("theme_type")
        val themeType: Long,
        @SerialName("tv_due_date")
        val tvDueDate: Long,
        @SerialName("tv_vip_pay_type")
        val tvVipPayType: Long,
        @SerialName("tv_vip_status")
        val tvVipStatus: Long,
        @SerialName("type")
        val type: Long,
        @SerialName("vip_pay_type")
        val vipPayType: Long
    ) {
        @Serializable
        data class AvatarIcon(
            @SerialName("icon_resource")
            val iconResource: IconResource,
            @SerialName("icon_type")
            val iconType: Long
        ) {
            @Serializable
            class IconResource
        }

        @Serializable
        data class Label(
            @SerialName("bg_color")
            val bgColor: String,
            @SerialName("bg_style")
            val bgStyle: Long,
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
            @SerialName("label_goto")
            val labelGoto: LabelGoto,
            @SerialName("label_id")
            val labelId: Long,
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
        ) {
            @Serializable
            data class LabelGoto(
                @SerialName("mobile")
                val mobile: String,
                @SerialName("pc_web")
                val pcWeb: String
            )
        }
    }
}