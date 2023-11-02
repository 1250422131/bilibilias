package com.imcys.network.configration

import com.imcys.datastore.fastkv.CookiesData
import com.imcys.network.constants.ROAM_HOST
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieManager @Inject constructor(
    private val cookiesData: CookiesData
) : CookiesStorage {
    /**
     * Cookie(name=SESSDATA, value=f36dcd11%2C1709481409%2C05a59%2A91ie_fpMIgVzpgsUiEDdBbN1o3FtOw_EPzGpxKgh9cyFsrBAzbnYG2re0UI717VPU1Z5nEQwAAHgA, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=true, httpOnly=true, extensions={})
     * Cookie(name=bili_jct, value=e7e3acaf7f66a1b6f343f4f60ed9a3e1, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     * Cookie(name=DedeUserID, value=10993030, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     * Cookie(name=DedeUserID__ckMd5, value=70b078dc71b5cc07, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     * Cookie(name=sid, value=p52c33i5, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     */
    private val cache = mutableSetOf<Cookie>()

    init {
        if (cookiesData.hasSessionData()) {
            cache.add(
                Cookie(
                    "SESSDATA",
                    cookiesData.sessionData, CookieEncoding.RAW, domain = "bilibili.com", path = "/",
                    httpOnly = true, secure = true
                )
            )
        }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        cache.add(cookie)
        cookiesData.setCookie(cookie.name, cookie.value, cookie.expires?.timestamp)
    }

    override fun close() = Unit

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return if (requestUrl.host == ROAM_HOST) cache.toList() else emptyList()
    }
}
