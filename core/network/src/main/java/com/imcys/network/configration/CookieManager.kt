package com.imcys.network.configration

import com.imcys.datastore.fastkv.ICookieStore
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.cbor.Cbor
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalSerializationApi::class)
class CookieManager
@Inject constructor(
    private val cookiesData: ICookieStore,
    private val cbor: Cbor
) : CookiesStorage {
    private val cache = mutableListOf<Cookie>()
    private val sterileMap = ListSerializer(CookieSerializer)

    init {
        if (cookiesData.valid) {
            cookiesData.get()?.let { bytes ->
                cbor.decodeFromByteArray(sterileMap, bytes).map {
                    cache.add(it)
                }
            }
        }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        Timber.d("addCookie: $cookie")
        val timestamp = cookie.expires?.timestamp ?: 0
        cache += cookie
        cookiesData.setTime(timestamp)
        save()
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        Timber.d("getCookie: $requestUrl")
        return cache
    }

    override fun close() {
        save()
    }

    private fun save() {
        cookiesData.set(
            cbor.encodeToByteArray(
                sterileMap,
                cache
            )
        )
    }
}
