package com.imcys.datastore.fastkv

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookiesData @Inject constructor(@ApplicationContext context: Context) : GlobalStorage(context, "cookies") {
    var sessionData by string("session")
    var jct by string("jct")
    var userID by string("userID")
    var sid by string("sid")
    var timestamp by long("timestamp")
    val isExpired: Boolean get() = (timestamp < System.currentTimeMillis())

    fun clearCookies() {
        sessionData = ""
        jct = ""
        userID = ""
        sid = ""
        timestamp = 0
    }
}