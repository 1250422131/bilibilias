package com.imcys.bilibilias.core.datasource

import androidx.datastore.core.DataStoreFactory
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.persistent.CookiePersistent
import com.imcys.bilibilias.core.datasource.persistent.TokenPersistent
import com.imcys.bilibilias.core.datasource.persistent.TokenSave
import com.imcys.bilibilias.core.datasource.persistent.TokenSave.Companion.INIT
import com.imcys.bilibilias.core.datasource.utils.ApiResponseUnwrapper
import com.imcys.bilibilias.core.datastore.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.di.applicationScope
import com.imcys.bilibilias.core.json.HttpClientJson
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.Cookie
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.ListSerializer
import org.koin.dsl.module

val DataSourceModule = module {
    single {
        BilibiliLoginApi(
            createHttpClient {
                defaultRequest {
                    url("https://passport.bilibili.com")
                }
                install(ApiResponseUnwrapper)
                install(ContentNegotiation) {
                    json(HttpClientJson)
                }
                install(HttpCookies) {
                    storage = get()
                }
                BrowserUserAgent()
                Logging {
                    level = LogLevel.BODY
                    logger = object : Logger {
                        override fun log(message: String) {
                            co.touchlab.kermit.Logger.i("BilibiliLoginApi") { message }
                        }
                    }
                }
            }
        )
    }
    single<CookiesStorage> {
        CookiePersistent(
            DataStoreFactory.new(
                serializer = ListSerializer(Cookie.serializer()).asDataStoreSerializer { emptyList() },
                produceFile = { resolveDataStoreFile("cookies") },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO)
            )
        )
    }
    single<TokenPersistent> {
        TokenPersistent(
            DataStoreFactory.new(
                serializer = TokenSave.serializer().asDataStoreSerializer { INIT },
                corruptionHandler = ReplaceFileCorruptionHandler { INIT },
                produceFile = { resolveDataStoreFile("token") },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO)
            )
        )
    }
}