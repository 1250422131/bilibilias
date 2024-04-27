package com.imcys.bilibilias.home.ui.model

data class ToolItemBean(
    val title: String = "",
    val image_url: String = "",
    val bg_color: String = "",
    //0为普通工具，1为视频，番剧，2为直播，3为UP主
    val type: Int = 0,
    val videoBaseBean: VideoBaseBean? = null,
    val userCardBean: UserCardBean.DataBean.CardBean? = null,
    val clickEvent: () -> Unit = {},
)
