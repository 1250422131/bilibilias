package com.imcys.datastore.fastkv

import android.content.*
import com.imcys.model.login.*
import dagger.hilt.android.qualifiers.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.cbor.*
import javax.inject.*

@OptIn(ExperimentalSerializationApi::class)
class PersistentCookie @Inject constructor(
    @ApplicationContext context: Context,
    private val cbor: Cbor
) : FastKVOwner("PersistentCookie", context) {
    private var cookieByteArray by array(byteArrayOf())
    private val cache = mutableListOf<Cookie>()

    fun getCookie(): List<Cookie> {
        if (!logging) return emptyList()
        if (cache.isEmpty()) {
            cbor.decodeFromByteArray(ListSerializer(Cookie.serializer()), cookieByteArray)
                .forEach(cache::add)
        }
        return cache
    }

    fun getCookie(name: String): Cookie? = cache.find { it.name == name }

    @OptIn(ExperimentalSerializationApi::class)
    fun setCookie(cookie: Cookie) {
        cache.removeAll { it.name == cookie.name }
        cache += cookie
        cookieByteArray = cbor.encodeToByteArray(ListSerializer(Cookie.serializer()), cache)
        kv.commit()
        logging = true
    }

    var logging: Boolean by boolean()

    val biliJctOrCsrf: String
        get() = getCookie(SET_COOKIE_BILI_JCT)?.value ?: ""

    var refreshToken by string("")
        private set

    fun setRefreshToke(token: String) {
        if (token.isNotBlank()) {
            refreshToken = token
        }
    }

    fun clear() {
        kv.clear()
    }

    companion object {
        const val SET_COOKIE_SESSDATA = "SESSDATA"
        const val SET_COOKIE_BILI_JCT = "bili_jct"
        const val SET_COOKIE_DEDEUSERID = "DedeUserID"
        const val SET_COOKIE_DEDEUSERID__CKMD5 = "DedeUserID__ckMd5"
        const val SET_COOKIE_SID = "sid"
    }
}