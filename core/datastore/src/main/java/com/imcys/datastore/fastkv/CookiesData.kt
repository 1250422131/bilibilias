package com.imcys.datastore.fastkv

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CookiesData @Inject constructor(
    @ApplicationContext context: Context
) : FastKVOwner("cookies", context), ICookieStore {
    private var cookieByteArray: ByteArray? by array()

    private var timestamp: Long by long()

    override val valid: Boolean = timestamp > System.currentTimeMillis()

    var biliJctOrCsrf: String by string("")

    override var refreshToken by string("")
    fun setRefreshToke(token: String) {
        if (token.isNotBlank()) {
            refreshToken = token
        }
    }

    override fun setTime(timestamp: Long) {
        this.timestamp = timestamp
    }

    override fun get(): ByteArray? = cookieByteArray
    override fun set(byteArray: ByteArray) {
        cookieByteArray = byteArray
        kv.commit()
    }

    override fun clear() {
        cookieByteArray = byteArrayOf()
        timestamp = 0
    }
}
