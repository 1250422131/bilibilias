package com.imcys.bilibilias.network.plugin

import com.imcys.bilibilias.common.event.sendPlayVoucherErrorEvent
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_PGC_PLAYER_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_VIDEO_PLAYER_NO_WEBI_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_VIDEO_PLAYER_URL
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request

val RiskControlPlugin = createClientPlugin("RiskControlPlugin") {
    onResponse { response ->
        if (response.isSSE()) return@onResponse
        val responseBody = response.bodyAsText()
        val url = response.request.url.toString()
        if (responseBody.contains("v_voucher")){

            // 播放白名单
            val playWhitelist = listOf(
                WEB_VIDEO_PLAYER_URL,
                WEB_VIDEO_PLAYER_NO_WEBI_URL,
                WEB_PGC_PLAYER_URL
            )
            if (playWhitelist.any { url.contains(it) }){
                // 通知错误
                sendPlayVoucherErrorEvent()
            }
        }

    }
}