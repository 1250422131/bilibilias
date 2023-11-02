package com.imcys.datastore.fastkv

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookiesData @Inject constructor(@ApplicationContext context: Context) : GlobalStorage(context, "cookies") {
    var sessionData by string("session")
        private set
    private var jct by string("jct")
    private var userID by string("userID")
    private var sid by string("sid")
    private var timestamp by long("timestamp")
    val isExpired: Boolean get() = (timestamp < System.currentTimeMillis())

    fun clearCookieData() {
        sessionData = ""
        jct = ""
        userID = ""
        sid = ""
        timestamp = 0
    }

    fun hasSessionData() = sessionData.isNotBlank()
    fun setCookie(name: String, value: String, timestamp: Long?) {
        if (name == "SESSDATA") sessionData = value
        if (name == "bili_jct") jct = value
        if (name == "DedeUserID") userID = value
        if (name == "sid") sid = value
        this.timestamp = timestamp ?: 0
    }
}