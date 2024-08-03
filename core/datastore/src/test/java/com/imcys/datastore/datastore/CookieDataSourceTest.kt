package com.imcys.datastore.datastore

import com.bilias.core.datastore.cookie.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.*
import kotlin.test.*

class CookieDataSourceTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: CookieDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = CookieDataSource(tmpFolder.testUserPreferencesDataStore(testScope))
    }

    @Test
    fun getCookies() = testScope.runTest {
        subject.cookies.collect {

        }
    }

    @Test
    fun setCookie() = testScope.runTest {
        val cookie = Cookie("data", "abc")
        subject.setCookie(cookie)
        subject.cookies.collect {
            it[cookie.name]?.let { c ->
                assertEquals(cookie, c)
            }
        }
    }


}