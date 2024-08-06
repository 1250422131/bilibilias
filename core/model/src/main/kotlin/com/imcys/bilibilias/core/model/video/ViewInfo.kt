package com.imcys.bilibilias.core.model.video

import kotlinx.serialization.Serializable

typealias Aid = Long
typealias Bvid = String
typealias Cid = Long
typealias Mid = Long

@Deprecated("类应该只记录id", ReplaceWith("ViewIds"))
@Serializable
data class ViewInfo(val aid: Aid, val bvid: Bvid, val cid: Cid, val title: String)

@Serializable
data class ViewIds(val aid: Aid, val bvid: Bvid, val cid: Cid)
