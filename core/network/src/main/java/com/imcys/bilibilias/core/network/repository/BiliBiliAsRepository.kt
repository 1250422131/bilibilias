package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.bilibilias.HomeBanner
import com.imcys.bilibilias.core.model.bilibilias.UpdateNotice
import com.imcys.bilibilias.core.network.api.BiliBiliAsApi
import com.imcys.bilibilias.core.network.di.WrapperClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class BiliBiliAsRepository @Inject constructor(
    wrapperClient: WrapperClient
) {
    private val client = wrapperClient.client
    suspend fun getUpdateNotice(): UpdateNotice {
        return client.get(BiliBiliAsApi.UPDATE_DATA) {
            parameter("type", "json")
            parameter("version", BiliBiliAsApi.VERSION)
        }.body()
    }

    suspend fun postSignatureMessage(sha: String, md5: Pair<String, Long>, crc: String) {
        client.get(BiliBiliAsApi.UPDATE_DATA) {
            parameterTypeJson()
            parameter("version", BiliBiliAsApi.VERSION)
            parameter("SHA", sha)
            parameter("MD5", md5.first)
            parameter("CRC", crc)
            parameter("LJ", md5.second)
        }
    }

    suspend fun getHomeBanner(): HomeBanner =
        client.get(BiliBiliAsApi.UPDATE_DATA) {
            parameter("type", "banner")
        }.body()

    private fun HttpRequestBuilder.parameterTypeJson() =
        url.parameters.append("type", "json")
}
