package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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


 fun DashBangumiPlayBean.toDashVideoPlayBean(): DashVideoPlayBean {
    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
    val bangumiPlayResultStr = json.encodeToString(this.result)

    val dashVideoPlayBeanDataBean =
        json.decodeFromString<DashVideoPlayBean.DataBean>(bangumiPlayResultStr)


    result.support_formats.forEach {
        dashVideoPlayBeanDataBean.accept_description.add(it.description)
    }


    return DashVideoPlayBean().copy(
        code = code,
        message = message,
        data = dashVideoPlayBeanDataBean
    )
}

