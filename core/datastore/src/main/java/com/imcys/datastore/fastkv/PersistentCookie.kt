package com.imcys.datastore.fastkv

import com.imcys.model.login.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.cbor.*

@OptIn(ExperimentalSerializationApi::class)
object PersistentCookie : FastKVOwner("PersistentCookie") {
    private var cookieByteArray by array(byteArrayOf())
    private val cbor: Cbor = Cbor
    private fun decode(): List<Cookie> {
        return cbor.decodeFromByteArray(ListSerializer(Cookie.serializer()), cookieByteArray)
    }

    private fun encode(cookies: List<Cookie>) {
        cookieByteArray = cbor.encodeToByteArray(ListSerializer(Cookie.serializer()), cookies)
    }

    fun getCookie(): List<Cookie> {
        if (!logging) return emptyList()
        return decode()
    }

    fun setCookie(cookies: List<Cookie>) {
        encode(cookies)
        kv.commit()
        logging = true
    }

    fun save() {
        kv.commit()
    }

    var logging: Boolean by boolean()

    var refreshToken by string("")
        private set

    fun setRefreshToke(token: String) {
        if (token.isNotBlank()) {
            refreshToken = token
        }
    }

    fun clear() {
        logging = false
        kv.clear()
    }


    const val SET_COOKIE_SESSDATA = "SESSDATA"
    const val SET_COOKIE_BILI_JCT = "bili_jct"
    const val SET_COOKIE_DEDEUSERID = "DedeUserID"
    const val SET_COOKIE_DEDEUSERID__CKMD5 = "DedeUserID__ckMd5"
    const val SET_COOKIE_SID = "sid"
}
