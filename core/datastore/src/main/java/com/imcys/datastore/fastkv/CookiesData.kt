package com.imcys.datastore.fastkv

import android.content.Context
import androidx.collection.ArrayMap
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.model.cookie.AsCookie
import dagger.hilt.android.qualifiers.ApplicationContext
import io.fastkv.interfaces.FastEncoder
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CookiesData @Inject constructor(
    private val cookieEncoder: CookieListEncoder,
    @ApplicationContext context: Context,
    @Dispatcher(AsDispatchers.IO) ioDispatcher: CoroutineDispatcher
) : GlobalStorage("cookies", context, ioDispatcher) {
    private val map = ArrayMap<String, AsCookie>(5)
    private val cookie by obj("cookie_list", cookieEncoder)

    init {
        cookie?.forEach {
            map[it.name] = it
        }
    }

    fun get(name: String): AsCookie? {
        return map.getOrPut(name) {
            cookie?.find { it.name == name }
        }
    }

    fun add(c: AsCookie) {
        map[c.name] = c
        cookie?.removeAll { it.name == c.name }
        cookie?.add(c)
    }

    fun remove(name: String) {
        map.remove(name)
        cookie?.removeAll { it.name == name }
    }

    var sessionData: String by string("session", "")
        private set

    /**
     * bili_jct
     *
     * biliCSRF
     */
    var csrf: String by string("jct")
    var userID: String by string("user_id")
        private set
    var sid: String by string("sid")
        private set
    var timestamp: Long by long("timestamp")
        private set

    var refreshToken: String by string("refresh_token")
    val isExpired: Boolean get() = (timestamp < System.currentTimeMillis())

    fun clearCookieData() {
        sessionData = ""
        csrf = ""
        userID = ""
        sid = ""
        timestamp = 0
    }

    fun hasSessionData() = sessionData.isNotBlank()
    fun setCookie(name: String, value: String, timestamp: Long?) {
        if (name == "SESSDATA") sessionData = value
        if (name == "bili_jct") csrf = value
        if (name == "DedeUserID") userID = value
        if (name == "sid") sid = value
        this.timestamp = timestamp ?: 0
    }

    override fun encoders(): Array<FastEncoder<*>> {
        return arrayOf(cookieEncoder)
    }
}
