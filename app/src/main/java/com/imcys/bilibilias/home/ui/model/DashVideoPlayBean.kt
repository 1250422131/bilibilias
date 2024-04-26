package com.imcys.bilibilias.home.ui.model

import com.imcys.deeprecopy.an.EnhancedData
import kotlinx.serialization.Serializable

/**
 * dash类型视频返回数据
 */
@Serializable
@EnhancedData
data class DashVideoPlayBean(
    val code: Int = 0,
    val message: String = "",
    val ttl: Int = 0,
    val data: DataBean = DataBean(),
) {


    @Serializable
    data class DataBean(
        val from: String = "",
        val result: String = "",
        val message: String = "",
        val timelength: Int = 0,
        val dash: DashBean = DashBean(),
        val accept_description: MutableList<String> = mutableListOf(),
        val accept_quality: List<Int> = emptyList(),
        val support_formats: List<SupportFormatsBean> = emptyList(),
    ) {
        @Serializable
        data class DashBean(
            val duration: Int = 0,
            val video: List<VideoBean> = emptyList(),
            val audio: List<AudioBean> = emptyList(),
        ) {
            @Serializable
            data class VideoBean(
                val id: Int = 0,
                val baseUrl: String = "",
                val width: Int = 0,
                val height: Int = 0,
            )

            @Serializable
            data class AudioBean(
                var selected: Int = 0,
                val id: Int = 0,
                val baseUrl: String = "",
                val width: Int = 0,
                val height: Int = 0,
            )
        }

        @Serializable
        data class SupportFormatsBean(
            val quality: Int = 0,
            val new_description: String = "",
            val display_desc: String = "",
        )
    }
}
