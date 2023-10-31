package com.imcys.bilibilias.home.ui.model

import com.imcys.model.VideoDetails


data class ToolItemBean(

    val title: String = "",
    val image_url: String = "",
    val bg_color: String = "",
    //0为普通工具，1为视频，番剧，2为直播
    val type: Int = 0,
    val videoDetails: com.imcys.model.VideoDetails? = null,
    val clickEvent: () -> Unit = {},

    )