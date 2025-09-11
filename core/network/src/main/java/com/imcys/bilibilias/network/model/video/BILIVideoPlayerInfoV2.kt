package com.imcys.bilibilias.network.model.video

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class BILIVideoPlayerInfoV2(
    @SerialName("aid")
    val aid: Long,
    @SerialName("answer_status")
    val answerStatus: Long,
//    @SerialName("bgm_info")
//    val bgmInfo: BgmInfo,
    @SerialName("bvid")
    val bvid: String,
    @SerialName("cid")
    val cid: Long,
//    @SerialName("disable_show_up_info")
//    val disableShowUpInfo: Boolean,
//    @SerialName("elec_high_level")
//    val elecHighLevel: ElecHighLevel,
//    @SerialName("fawkes")
//    val fawkes: Fawkes,
//    @SerialName("level_info")
//    val levelInfo: LevelInfo,
//    @SerialName("online_switch")
//    val onlineSwitch: OnlineSwitch,
//    @SerialName("options")
//    val options: Options,
//    @SerialName("show_switch")
//    val showSwitch: ShowSwitch,
    @SerialName("subtitle")
    val subtitle: Subtitle,
//    @SerialName("view_poLongs")
//    val vip: Vip
) {
    @Serializable
    data class BgmInfo(
        @SerialName("jump_url")
        val jumpUrl: String,
        @SerialName("music_id")
        val musicId: String,
        @SerialName("music_title")
        val musicTitle: String
    )

    @Serializable
    data class ElecHighLevel(
        @SerialName("button_text")
        val buttonText: String,
        @SerialName("Longro")
        val Longro: String,
        @SerialName("jump_url")
        val jumpUrl: String,
        @SerialName("new")
        val new: Boolean,
        @SerialName("privilege_type")
        val privilegeType: Long,
        @SerialName("qa_title")
        val qaTitle: String,
        @SerialName("question_text")
        val questionText: String,
        @SerialName("show_button")
        val showButton: Boolean,
        @SerialName("sub_title")
        val subTitle: String,
        @SerialName("title")
        val title: String
    )

    @Serializable
    data class Fawkes(
        @SerialName("config_version")
        val configVersion: Long,
        @SerialName("ff_version")
        val ffVersion: Long
    )

    @Serializable
    data class LevelInfo(
        @SerialName("current_exp")
        val currentExp: Long,
        @SerialName("current_level")
        val currentLevel: Long,
        @SerialName("current_min")
        val currentMin: Long,
        @SerialName("level_up")
        val levelUp: Long,
        @SerialName("next_exp")
        val nextExp: Long
    )

    @Serializable
    data class OnlineSwitch(
        @SerialName("enable_gray_dash_playback")
        val enableGrayDashPlayback: String,
        @SerialName("new_broadcast")
        val newBroadcast: String,
        @SerialName("realtime_dm")
        val realtimeDm: String,
        @SerialName("subtitle_submit_switch")
        val subtitleSubmitSwitch: String
    )

    @Serializable
    data class Options(
        @SerialName("is_360")
        val is360: Boolean,
        @SerialName("without_vip")
        val withoutVip: Boolean
    )

    @Serializable
    data class ShowSwitch(
        @SerialName("long_progress")
        val longProgress: Boolean
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

