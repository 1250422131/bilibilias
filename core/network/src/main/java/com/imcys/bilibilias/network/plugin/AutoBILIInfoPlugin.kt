package com.imcys.bilibilias.network.plugin

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_PGC_PLAYER_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_QRCODE_GENERATE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_QRCODE_POLL_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_VIDEO_PLAYER_URL
import com.imcys.bilibilias.network.config.BILIBILI_URL
import com.imcys.bilibilias.network.config.REFERER
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.first


class AutoBILIInfoPluginConfig {
    var appSetting: DataStore<AppSettings>? = null
}

val AutoBILIInfoPlugin = createClientPlugin("AutoBILIInfoPlugin", ::AutoBILIInfoPluginConfig) {

    val appSettings = pluginConfig.appSetting

    val uaWhiteList = listOf(
        TV_QRCODE_GENERATE_URL,
        TV_QRCODE_POLL_URL,
    )

    val refererWhiteList = listOf(
        TV_VIDEO_PLAYER_URL,
        TV_PGC_PLAYER_URL
    )

    onRequest { request, _ ->
        val url = request.url.toString()

        if (appSettings?.data?.first()?.videoParsePlatform == AppSettings.VideoParsePlatform.TV) {
            val host = request.url.host
            if (
                host.contains("bilivideo")
                || host.contains("edge")
                || uaWhiteList.any { url.contains(it) }
                || refererWhiteList.any { url.contains(it) }
            ) {
                request.headers.remove(HttpHeaders.Referrer)
                request.headers.remove(HttpHeaders.UserAgent)
                return@onRequest
            }
        }

        if (request.headers[REFERER] == null) {
            request.headers.append(REFERER, BILIBILI_URL)
        }

    }
}