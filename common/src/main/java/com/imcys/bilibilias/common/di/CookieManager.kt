package com.imcys.bilibilias.common.di

import com.imcys.bilibilias.common.base.config.CookieManagerRepository
import com.imcys.bilibilias.common.base.constant.ROAM_HOST
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import timber.log.Timber

internal class CookieManager : CookiesStorage {
    /**
     * Cookie(name=SESSDATA, value=f36dcd11%2C1709481409%2C05a59%2A91ie_fpMIgVzpgsUiEDdBbN1o3FtOw_EPzGpxKgh9cyFsrBAzbnYG2re0UI717VPU1Z5nEQwAAHgA, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=true, httpOnly=true, extensions={})
     * Cookie(name=bili_jct, value=e7e3acaf7f66a1b6f343f4f60ed9a3e1, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     * Cookie(name=DedeUserID, value=10993030, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     * Cookie(name=DedeUserID__ckMd5, value=70b078dc71b5cc07, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     * Cookie(name=sid, value=p52c33i5, encoding=RAW, maxAge=0, expires=GMTDate(seconds=49, minutes=56, hours=15, dayOfWeek=SUNDAY, dayOfMonth=3, dayOfYear=63, month=MARCH, year=2024, timestamp=1709481409000), domain=bilibili.com, path=/, secure=false, httpOnly=false, extensions={})
     */
    private val cache = mutableSetOf<Cookie>()

    init {
        cache.add(createCookie("SESSDATA", CookieManagerRepository.SESSDATA))
        cache.add(createCookie("bili_jct", CookieManagerRepository.bili_jct))
        cache.add(createCookie("DedeUserID", CookieManagerRepository.DedeUserID))
        cache.add(createCookie("DedeUserID__ckMd5", CookieManagerRepository.DedeUserID__ckMd5))
        cache.add(createCookie("sid", CookieManagerRepository.sid))
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        cache.add(cookie)
        if (cookie.name == "SESSDATA") CookieManagerRepository.SESSDATA = cookie.value
        if (cookie.name == "bili_jct") CookieManagerRepository.bili_jct = cookie.value
        if (cookie.name == "DedeUserID") CookieManagerRepository.DedeUserID = cookie.value
        if (cookie.name == "DedeUserID__ckMd5") CookieManagerRepository.DedeUserID__ckMd5 = cookie.value
        if (cookie.name == "sid") CookieManagerRepository.sid = cookie.value
        CookieManagerRepository.timestamp = cookie.expires?.timestamp ?: 0
        Timber.tag("AuthViewModel1").d("url=${requestUrl.host}, cookie=${cookie.name},${cookie.value}")
    }

    override fun close() {}

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return if (requestUrl.host == ROAM_HOST) cache.toList() else emptyList()
    }

    private fun createCookie(name: String, value: String): Cookie {
        return Cookie(name, value, CookieEncoding.RAW, domain = "bilibili.com", path = "/")
    }
}