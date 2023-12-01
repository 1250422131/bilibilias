package com.imcys.datastore.fastkv

import android.content.Context
import android.util.ArrayMap
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Cookie(name=SESSDATA, value=xxx, encoding=RAW, maxAge=0, expires=GMTDate(seconds=2, minutes=8, hours=8, dayOfWeek=THURSDAY, dayOfMonth=16, dayOfYear=137, month=MAY, year=2024, timestamp=1715846882000), domain=bilibili.com, path=/, secure=true, httpOnly=true, extensions={})
 * Cookie(name=bili_jct, value=xxx, encoding=RAW, maxAge=0, expires=GMTDate(seconds=2, minutes=8, hours=8, dayOfWeek=THURSDAY, dayOfMonth=16, dayOfYear=137, month=MAY, year=2024, timestamp=1715846882000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
 * Cookie(name=DedeUserID, value=xxx, encoding=RAW, maxAge=0, expires=GMTDate(seconds=2, minutes=8, hours=8, dayOfWeek=THURSDAY, dayOfMonth=16, dayOfYear=137, month=MAY, year=2024, timestamp=1715846882000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
 * Cookie(name=DedeUserID__ckMd5, value=xxx, encoding=RAW, maxAge=0, expires=GMTDate(seconds=2, minutes=8, hours=8, dayOfWeek=THURSDAY, dayOfMonth=16, dayOfYear=137, month=MAY, year=2024, timestamp=1715846882000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
 * Cookie(name=sid, value=xxx, encoding=RAW, maxAge=0, expires=GMTDate(seconds=2, minutes=8, hours=8, dayOfWeek=THURSDAY, dayOfMonth=16, dayOfYear=137, month=MAY, year=2024, timestamp=1715846882000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
 */
class CookiesData @Inject constructor(
    @ApplicationContext context: Context
) : FastKVOwner("cookies", context) {
    private val cookiesData = ArrayMap<String, String>(5)
    var cookieByteArray by array()
        private set

    private var login by boolean()

    var timestamp: Long by long(0)
    val expires: Boolean = timestamp < System.currentTimeMillis()

    val isLogin: Boolean = login

    fun save(data: ByteArray) {
        cookieByteArray = data
        login = true
        kv.commit()
    }

    fun logout() {
        login = false
    }

    var sessionData by string()
        private set

    /**
     * bili_jct
     *
     * biliCSRF
     */
    var csrf: String by string("")
    var userID: String by string("")
        private set

    var refreshToken by string("")

    fun clearCookieData() {
        logout()
    }
}
