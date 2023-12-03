package com.imcys.network.configration

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import javax.inject.Inject

class AddWbiIntercept @Inject constructor(private val client: HttpClient) : Sender {
    private var currentCall: HttpClientCall? = null
    override suspend fun execute(requestBuilder: HttpRequestBuilder): HttpClientCall {
        requestBuilder.url
        val sendResult = client.sendPipeline.execute(
            requestBuilder,
            requestBuilder.body
        )

        val call = sendResult as? HttpClientCall
            ?: error("Failed to execute send pipeline. Expected [HttpClientCall], but received $sendResult")

        currentCall = call
        return call
    }
}