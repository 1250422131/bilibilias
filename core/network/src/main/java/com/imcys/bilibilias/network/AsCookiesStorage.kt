package com.imcys.bilibilias.network


import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.datastore.source.UsersDataSource
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi


@OptIn(ExperimentalSerializationApi::class)
class AsCookiesStorage(
    private val usersDataSource: UsersDataSource,
    private val biliUserCookiesDao: BILIUserCookiesDao
) : CookiesStorage {
    private val cookies = mutableListOf<Cookie>()
    private val daoCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private suspend fun isLogin() = usersDataSource.isLogin()
    private var isInit = false
    // 添加协程的 Mutex 锁
    private val cookieMutex = Mutex()

    init {
        // 初始化当前Cookie
        runCatching {
            daoCoroutineScope.launch {
                syncDataBaseCookies()
                isInit = true
            }
        }
    }

    /**
     * 同步数据库的Cookie
     */
    suspend fun syncDataBaseCookies() {
        if (!isLogin()) {
            // 未来登录走其他的操作
        } else {
            // 登录
            val dataBaseCookies =
                biliUserCookiesDao.getBILIUserCookiesByUid(usersDataSource.getUserId())
            cookieMutex.withLock {
                dataBaseCookies.forEach {
                    cookies.removeAll { cookie -> cookie.name == it.name }
                    val cookie = Cookie(
                        name = it.name,
                        value = it.value,
                        encoding = CookieEncoding.valueOf(it.encoding.name),
                        domain = it.domain,
                        path = it.path,
                        secure = it.secure,
                        httpOnly = it.httpOnly
                    )
                    cookies.add(cookie)
                }
            }
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        if (!isInit) {
            syncDataBaseCookies()
        }
        return cookieMutex.withLock {
            cookies.run {
                if (usersDataSource.users.first().notUseBuvid3) {
                    filter { it.name != "buvid3" }
                } else {
                    this.toList()
                }
            }
        }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val timestamp = cookie.expires?.timestamp ?: 0
        if (timestamp < System.currentTimeMillis()) return

        val mCookie = cookie.copy(domain = requestUrl.host)
        cookieMutex.withLock {
            cookies.removeAll { it -> it.name == mCookie.name }
            cookies.add(mCookie)
        }
    }


    suspend fun getCookieValue(key: String): String? {
        return cookieMutex.withLock {
            cookies.find { it.name == key }?.value
        }
    }

    suspend fun getAllCookies(): MutableList<Cookie> {
        return cookieMutex.withLock {
            cookies.toMutableList()
        }
    }

    suspend fun updateAllCookies(cookie: MutableList<Cookie>) {
        cookieMutex.withLock {
            cookies.clear()
            cookies.addAll(cookie)
        }
    }

    override fun close() {
    }

}
