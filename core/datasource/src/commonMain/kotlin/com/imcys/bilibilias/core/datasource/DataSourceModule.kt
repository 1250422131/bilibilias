package com.imcys.bilibilias.core.datasource

import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.persistent.CookiesStorageImpl
import com.imcys.bilibilias.core.datasource.utils.ApiResponseUnwrapper
import com.imcys.bilibilias.core.json.HttpClientJson
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.core.logging.logger
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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
                        private val logger = logger<BilibiliLoginApi>()
                        override fun log(message: String) {
                            logger.info { message }
                        }
                    }
                }
            },
            get()
        )
    }
    singleOf(::CookiesStorageImpl) bind CookiesStorage::class
}