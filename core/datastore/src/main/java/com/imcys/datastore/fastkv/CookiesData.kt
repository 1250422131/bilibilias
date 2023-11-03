package com.imcys.datastore.fastkv

import javax.inject.Inject

class CookiesData @Inject constructor() : GlobalStorage("cookies") {

    var sessionData by string("session")
        private set

    /**
     * bili_jct
     *
     * biliCSRF
     */
    var csrf by string("jct")
    var userID by string("user_id")
        private set
    var sid by string("sid")
        private set
    var timestamp by long("timestamp")
        private set

    var refreshToken by string("refresh_token")
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
}