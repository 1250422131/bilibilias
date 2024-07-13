package com.imcys.bilibilias.core.model.bangumi

import com.imcys.bilibilias.core.model.video.Dimension
import com.imcys.bilibilias.core.model.video.Rights
import com.imcys.bilibilias.core.model.video.Stat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    @SerialName("aid")
    val aid: Long = 0,
    @SerialName("badge")
    val badge: String = "",
    @SerialName("badge_type")
    val badgeType: Int = 0,
    @SerialName("bvid")
    val bvid: String = "",
    @SerialName("cid")
    val cid: Long = 0,
    @SerialName("cover")
    val cover: String = "",
    @SerialName("dimension")
    val dimension: Dimension = Dimension(),
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("enable_vt")
    val enableVt: Boolean = false,
    @SerialName("ep_id")
    val epId: Long = 0,
    @SerialName("from")
    val from: String = "",
    @SerialName("icon_font")
    val iconFont: IconFont = IconFont(),
    @SerialName("id")
    val id: Int = 0,
    @SerialName("is_view_hide")
    val isViewHide: Boolean = false,
    @SerialName("link")
    val link: String = "",
    @SerialName("long_title")
    val longTitle: String = "",
    @SerialName("pub_time")
    val pubTime: Int = 0,
    @SerialName("pv")
    val pv: Int = 0,
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("rights")
    val rights: Rights = Rights(),
    @SerialName("share_copy")
    val shareCopy: String = "",
    @SerialName("share_url")
    val shareUrl: String = "",
    @SerialName("short_link")
    val shortLink: String = "",
    @SerialName("showDrmLoginDialog")
    val showDrmLoginDialog: Boolean = false,
    @SerialName("skip")
    val skip: Skip = Skip(),
    @SerialName("stat")
    val stat: Stat = Stat(),
    @SerialName("stat_for_unity")
    val statForUnity: StatForUnity = StatForUnity(),
    @SerialName("status")
    val status: Int = 0,
    @SerialName("subtitle")
    val subtitle: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("vid")
    val vid: String = ""
)
