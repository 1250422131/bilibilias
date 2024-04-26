package com.imcys.bilibilias.home.ui.model

import androidx.room.Ignore
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.JsonElement

/**
 * 用户投稿类
 */
@Serializable
data class UserWorksBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {
    @Serializable
    data class DataBean(
        val list: ListBean = ListBean(),
        val page: PageBean = PageBean(),
    ) {
        @Serializable
        data class ListBean(
            val vlist: List<VlistBean> = emptyList()
        ) {
            @Serializable
            data class VlistBean(
                val aid: Long = 0,
                val description: String = "",
                val title: String = "",
                val author: String = "",
                val mid: Long = 0,
                val bvid: String = "",
                val pic: String,
                val play: Int = 0,
                val video_review: Int,
            )
        }

        @Serializable
        data class PageBean(
            val pn: Int = 0,
            val ps: Int = 0,
            val count: Int = 0
        )
    }
}
