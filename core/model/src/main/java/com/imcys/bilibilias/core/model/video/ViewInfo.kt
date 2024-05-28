package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.Serializable

@Serializable
data class ViewInfo(val aid: Aid, val bvid: Bvid, val cid: Cid, val title: String)

typealias Cid = Long
typealias Aid = Long
typealias Bvid = String
