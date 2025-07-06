package com.imcys.bilibilias.data.repository

import com.imcys.bilibilias.network.AsCookiesStorage
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.config.BILIBILI_URL
import com.imcys.bilibilias.network.config.BUVID3
import com.imcys.bilibilias.network.config.BUVID4
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService
import io.ktor.client.plugins.cookies.addCookie
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.util.date.GMTDate

class RiskManagementRepository(
    private val webApiService: BILIBILIWebAPIService,
    private val asCookiesStorage: AsCookiesStorage
) {
    companion object {
        var spiB3 = ""
        var spiB4 = ""
    }

    /**
     * 更新签名
     */
    suspend fun updateWebSpiCookie() {
        if (spiB3.isNotEmpty() && spiB4.isNotEmpty()) return
        webApiService.getWebSpiInfo().collect {
            when (it) {
                is NetWorkResult.Success<*> -> {
                    if (it.data?.b3 != null && it.data?.b4 != null) {
                        asCookiesStorage.addCookie(
                            BILIBILI_URL, Cookie(
                                name = BUVID3,
                                value = it.data?.b3!!,
                                expires = GMTDate(System.currentTimeMillis() + 86400 * 1000L),
                                path = "/"
                            )
                        )
                        asCookiesStorage.addCookie(
                            BILIBILI_URL, Cookie(
                                name = BUVID4,
                                value = it.data?.b4!!,
                                expires = GMTDate(System.currentTimeMillis() + 86400 * 1000L),
                                path = "/"
                            )
                        )

                    }
                }

                else -> {}
            }
        }
    }

}