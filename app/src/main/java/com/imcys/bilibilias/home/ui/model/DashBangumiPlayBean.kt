package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 番剧dash下载类
 */
@Serializable
data class DashBangumiPlayBean(
    val code: Int = 0,
    val message: String = "",
    val result: ResultBean = ResultBean(),
) {
    @Serializable
    data class ResultBean(
        val code: Int = 0,
        val type: String = "",
        val result: String = "",
        val from: String = "",
        val message: String = "",
        val timelength: Int = 0,
        val dash: DashBean = DashBean(),
        val status: Int = 0,
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
                val baseUrl: String = "",
                val size: Long = 0,
                val width: Int = 0,
                val id: Int = 0,
                val height: Int = 0,
            )

            @Serializable
            data class AudioBean(
                val baseUrl: String = "",
                val size: Long = 0,
                val width: Int = 0,
                val id: Int = 0,
                val height: Int = 0,
            )
        }

        @Serializable
        data class SupportFormatsBean(
            val display_desc: String = "",
            val description: String = "",
            val quality: Int = 0,
        )
    }
}
