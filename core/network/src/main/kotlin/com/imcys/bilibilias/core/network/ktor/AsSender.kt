package com.imcys.bilibilias.core.network.ktor

import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.network.Parameter
import com.imcys.bilibilias.core.network.di.requireCSRF
import com.imcys.bilibilias.core.network.di.requireWbi
import com.imcys.bilibilias.core.network.utils.TokenUtil
import io.github.aakira.napier.Napier
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.http.ParametersBuilder
import kotlinx.coroutines.flow.first

internal suspend fun Sender.wbiIntercept(
    request: HttpRequestBuilder,
    loginInfoDataSource: LoginInfoDataSource,
): HttpClientCall {
    if (request.attributes.getOrNull(requireCSRF) == true) {
        val cookie = loginInfoDataSource.cookieStore.first()["bili_jct"]
        if (cookie != null) {
            request.parameter("csrf", cookie.value_)
        }
    }
    if (request.attributes.getOrNull(requireWbi) == true) {
        val params = request.url.parameters
        val signatureParams = mutableListOf<Parameter>()
        for ((k, v) in params.entries()) {
            signatureParams.add(Parameter(k, v.first()))
        }
        val signature = TokenUtil.genBiliSign(
            signatureParams.associate { it.name to it.value }.toMutableMap(),
            loginInfoDataSource.mixKey.first(),
        )
        Napier.i(tag = "wbi") { signature.joinToString("\n") }
        val newParameter = ParametersBuilder()
        for ((n, v) in signature) {
            newParameter.append(n, v)
        }

        request.url.encodedParameters = newParameter
    }
    return execute(request)
}
