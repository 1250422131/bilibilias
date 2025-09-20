package com.imcys.bilibilias.network.plugin

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_PGC_PLAYER_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_WEBI_PGC_SEASON_VIEW
import com.imcys.bilibilias.network.config.APP_KEY
import com.imcys.bilibilias.network.utils.BiliAppSigner
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.URLProtocol
import kotlinx.coroutines.flow.first

/**
 * Roam 插件配置:
 * whitelist: 需要触发域名替换的匹配片段（默认使用内置常量）
 * domainReplacement: key=原始host  value=新的host
 * forceScheme: 可选强制协议(http / https)，不需要可不设置
 */
class RoamPluginConfig {
    var whitelist: List<String> = emptyList()
    var domainReplacement: Map<String, String> = emptyMap()
    var forceScheme: String? = "https"

    var biliUsersDao: BILIUsersDao? = null

    var appSetting: DataStore<AppSettings>? = null
}

/**
 * 漫游插件
 */
val RoamPlugin = createClientPlugin("RoamPlugin", ::RoamPluginConfig) {


    val whitelist = pluginConfig.whitelist.ifEmpty {
        listOf(
            WEB_PGC_PLAYER_URL,
            WEB_WEBI_PGC_SEASON_VIEW,
        )
    }
    val mapping = pluginConfig.domainReplacement
    val forceScheme = pluginConfig.forceScheme
    val biliUsersDao = pluginConfig.biliUsersDao
    val appSettings = pluginConfig.appSetting

    onRequest { request, _ ->
        if (request.isSSE()) return@onRequest
        if (appSettings?.data?.first()?.enabledRoam == false) return@onRequest
        request.headers.append("Roam-Enabled", "true")
        val originalFull = request.url.toString()
        if (whitelist.none { originalFull.contains(it) }) return@onRequest


        val tvUser = biliUsersDao?.getBILIUserByPlatform(LoginPlatform.TV)
        val oldHost = request.url.host
        val newHost = mapping[oldHost]
        if (!newHost.isNullOrBlank() && newHost != oldHost) {
            request.url.host = newHost
            if (!forceScheme.isNullOrBlank()) {
                request.url.protocol = URLProtocol.createOrDefault(forceScheme)
            }
        }

        // 补充get参数
        tvUser?.let { user ->
            if (request.url.parameters["access_key"].isNullOrBlank()) {
                request.url.parameters.append("access_key", "${user.accessToken}")
            }
            if (request.url.parameters[APP_KEY].isNullOrBlank()) {
                request.url.parameters.append(APP_KEY, BiliAppSigner.APP_KEY)
            }
            if (request.url.parameters["area"].isNullOrBlank()) {
                request.url.parameters.append("area", "hk")
            }

        }

    }
}
