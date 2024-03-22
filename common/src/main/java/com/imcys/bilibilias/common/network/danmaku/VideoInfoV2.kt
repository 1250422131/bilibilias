package com.imcys.bilibilias.common.network.danmaku

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoInfoV2(
    @SerialName("aid")
    val aid: Long = 0,
    @SerialName("allow_bp")
    val allowBp: Boolean = false,
    @SerialName("answer_status")
    val answerStatus: Long = 0,
    @SerialName("block_time")
    val blockTime: Long = 0,
    @SerialName("bvid")
    val bvid: String = "",
    @SerialName("cid")
    val cid: Long = 0,
    @SerialName("disable_show_up_info")
    val disableShowUpInfo: Boolean = false,
    @SerialName("elec_high_level")
    val elecHighLevel: ElecHighLevel = ElecHighLevel(),
    @SerialName("fawkes")
    val fawkes: Fawkes = Fawkes(),
    @SerialName("guide_attention")
    val guideAttention: List<GuideAttention> = listOf(),
    @SerialName("has_next")
    val hasNext: Boolean = false,
    @SerialName("ip_info")
    val ipInfo: IpInfo = IpInfo(),
    @SerialName("is_owner")
    val isOwner: Boolean = false,
    @SerialName("is_ugc_pay_preview")
    val isUgcPayPreview: Boolean = false,
    @SerialName("is_upower_exclusive")
    val isUpowerExclusive: Boolean = false,
    @SerialName("is_upower_play")
    val isUpowerPlay: Boolean = false,
    @SerialName("last_play_cid")
    val lastPlayCid: Long = 0,
    @SerialName("last_play_time")
    val lastPlayTime: Long = 0,
    @SerialName("level_info")
    val levelInfo: LevelInfo = LevelInfo(),
    @SerialName("login_mid")
    val loginMid: Long = 0,
    @SerialName("login_mid_hash")
    val loginMidHash: String = "",
    @SerialName("max_limit")
    val maxLimit: Long = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("need_login_subtitle")
    val needLoginSubtitle: Boolean = false,
    @SerialName("no_share")
    val noShare: Boolean = false,
    @SerialName("now_time")
    val nowTime: Long = 0,
    @SerialName("online_count")
    val onlineCount: Long = 0,
    @SerialName("online_switch")
    val onlineSwitch: OnlineSwitch = OnlineSwitch(),
    @SerialName("options")
    val options: Options = Options(),
    @SerialName("page_no")
    val pageNo: Long = 0,
    @SerialName("permission")
    val permission: String = "",
    @SerialName("preview_toast")
    val previewToast: String = "",
    @SerialName("role")
    val role: String = "",
    @SerialName("show_switch")
    val showSwitch: ShowSwitch = ShowSwitch(),
    @SerialName("subtitle")
    val subtitle: Subtitle = Subtitle(),
    @SerialName("toast_block")
    val toastBlock: Boolean = false,
    @SerialName("vip")
    val vip: Vip = Vip(),

    ) {
    @Serializable
    data class ElecHighLevel(
        @SerialName("Longro")
        val longro: String = "",
        @SerialName("level_str")
        val levelStr: String = "",
        @SerialName("privilege_type")
        val privilegeType: Long = 0,
        @SerialName("title")
        val title: String = "",
    )

    @Serializable
    data class Fawkes(
        @SerialName("config_version")
        val configVersion: Long = 0,
        @SerialName("ff_version")
        val ffVersion: Long = 0,
    )

    @Serializable
    data class GuideAttention(
        @SerialName("from")
        val from: Long = 0,
        @SerialName("pos_x")
        val posX: Double = 0.0,
        @SerialName("pos_y")
        val posY: Double = 0.0,
        @SerialName("to")
        val to: Long = 0,
        @SerialName("type")
        val type: Long = 0,
    )

    @Serializable
    data class IpInfo(
        @SerialName("city")
        val city: String = "",
        @SerialName("country")
        val country: String = "",
        @SerialName("ip")
        val ip: String = "",
        @SerialName("province")
        val province: String = "",
        @SerialName("zone_id")
        val zoneId: Long = 0,
        @SerialName("zone_ip")
        val zoneIp: String = "",
    )

    @Serializable
    data class LevelInfo(
        @SerialName("current_exp")
        val currentExp: Long = 0,
        @SerialName("current_level")
        val currentLevel: Long = 0,
        @SerialName("current_min")
        val currentMin: Long = 0,
        @SerialName("level_up")
        val levelUp: Long = 0,
        @SerialName("next_exp")
        val nextExp: Long = 0,
    )

    @Serializable
    data class OnlineSwitch(
        @SerialName("enable_gray_dash_playback")
        val enableGrayDashPlayback: String = "",
        @SerialName("new_broadcast")
        val newBroadcast: String = "",
        @SerialName("realtime_dm")
        val realtimeDm: String = "",
        @SerialName("subtitle_submit_switch")
        val subtitleSubmitSwitch: String = "",
    )

    @Serializable
    data class Options(
        @SerialName("is_360")
        val is360: Boolean = false,
        @SerialName("without_vip")
        val withoutVip: Boolean = false,
    )

    @Serializable
    data class ShowSwitch(
        @SerialName("long_progress")
        val longProgress: Boolean = false,
    )

    @Serializable
    data class Subtitle(
        @SerialName("allow_submit")
        val allowSubmit: Boolean = false,
        @SerialName("lan")
        val lan: String = "",
        @SerialName("lan_doc")
        val lanDoc: String = "",
        @SerialName("subtitles")
        val subtitles: List<MSubtitle> = listOf(),
    ) {
        @Serializable
        data class MSubtitle(
            @SerialName("ai_status")
            val aiStatus: Long = 0,
            @SerialName("ai_type")
            val aiType: Long = 0,
            @SerialName("id")
            val id: Long = 0,
            @SerialName("id_str")
            val idStr: String = "",
            @SerialName("is_lock")
            val isLock: Boolean = false,
            @SerialName("lan")
            val lan: String = "",
            @SerialName("lan_doc")
            val lanDoc: String,
            @SerialName("subtitle_url")
            val subtitleUrl: String = "",
            @SerialName("type")
            val type: Long = 0,
            var check: Boolean = false,
        )
    }

    @Serializable
    data class Vip(
        @SerialName("avatar_subscript")
        val avatarSubscript: Long = 0,
        @SerialName("avatar_subscript_url")
        val avatarSubscriptUrl: String = "",
        @SerialName("due_date")
        val dueDate: Long = 0,
        @SerialName("label")
        val label: Label = Label(),
        @SerialName("nickname_color")
        val nicknameColor: String = "",
        @SerialName("role")
        val role: Long = 0,
        @SerialName("status")
        val status: Long = 0,
        @SerialName("theme_type")
        val themeType: Long = 0,
        @SerialName("tv_due_date")
        val tvDueDate: Long = 0,
        @SerialName("tv_vip_pay_type")
        val tvVipPayType: Long = 0,
        @SerialName("tv_vip_status")
        val tvVipStatus: Long = 0,
        @SerialName("type")
        val type: Long = 0,
        @SerialName("vip_pay_type")
        val vipPayType: Long = 0,
    ) {
        @Serializable
        data class Label(
            @SerialName("bg_color")
            val bgColor: String = "",
            @SerialName("bg_style")
            val bgStyle: Long = 0,
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
            val useImgLabel: Boolean = false,
        )
    }
}
