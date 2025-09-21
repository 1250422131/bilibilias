package com.imcys.bilibilias.network.plugin

import android.util.Log
import com.imcys.bilibilias.network.config.BILIBILI_URL
import com.imcys.bilibilias.network.config.REFERER
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.request

val AutoBILIInfoPlugin = createClientPlugin("AutoBILIInfoPlugin") {
    onRequest { request, _ ->
        if (request.headers[REFERER] == null) {
            request.headers.append(REFERER, BILIBILI_URL)
        }
    }
}