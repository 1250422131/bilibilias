package com.imcys.network.repository.wbi

import com.imcys.model.UserNav
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.utils.WBIUtils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WbiKeyRepository @Inject constructor(
    private val client: HttpClient
) : IWbiSignatureDataSources {
    override suspend fun getSignature(): String {
        val userNav = client.get(BilibiliApi2.WBI_SIGNATURE).body<UserNav>()
        return WBIUtils.getMixinKey(userNav.imgKey, userNav.subKey)
    }
}
