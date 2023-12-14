package com.imcys.model.video

import com.imcys.model.Dash
import com.imcys.model.PlayerInfo
import com.imcys.model.VideoDetails

data class ViewDetailAndPlayUrl(
    val aid: Long,
    val bvid: String,
    val cid: Long,
    val title: String,
    val dash: Dash,
    val videoDetails: VideoDetails,
    val playerPlayUrl: PlayerInfo
)