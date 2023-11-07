package com.imcys.network.configration

import com.imcys.datastore.fastkv.CookiesData
import com.imcys.model.cookie.AsCookie
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import timber.log.Timber
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
                    SESSION_DATA,
                    cookiesData.sessionData,
                    CookieEncoding.RAW,
                    domain = "bilibili.com",
                    path = "/",
                    httpOnly = true,
                    secure = true
                )
            )
        }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        if (cookie.name == SESSION_DATA) {
            val timestamp = cookie.expires?.timestamp ?: return
            if (timestamp < System.currentTimeMillis()) {
                return
            }
        }
        Timber.d(cookie.toString())
        cookiesData.add(cookie.toAsCookie())
        cookiesData.setCookie(cookie.name, cookie.value, cookie.expires?.timestamp)
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        Timber.d(cache.toString())
        cookiesData.get(SESSION_DATA)
        return cache.toList()
    }

    override fun close() = Unit
}

private const val SESSION_DATA = "SESSDATA"
fun Cookie.toAsCookie(): AsCookie {
    return AsCookie(
        name,
        value,
        maxAge = maxAge,
        timestamp = expires?.timestamp ?: 0,
        domain = domain,
        path = path,
        secure = secure,
        httpOnly = httpOnly
    )
}

fun AsCookie.toCookie(): Cookie {
    return Cookie(
        name,
        value,
        encoding = CookieEncoding.RAW,
        maxAge = maxAge,
        domain = domain,
        path = path,
        secure = secure,
        httpOnly = httpOnly,
    )
}
