package com.imcys.bilibilias.core.datasource

import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.persistent.CookiesStorageImpl
import com.imcys.bilibilias.core.datasource.utils.ApiResponseUnwrapper
import com.imcys.bilibilias.core.datasource.utils.WbiInitializer
import com.imcys.bilibilias.core.json.HttpClientJson
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.core.logging.logger
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DataSourceModule = module {
    val httpLogger = logger("BilibiliApi")
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
                HttpLogging()
            },
            get(),
            httpLogger,
        )
    }
    single {
        BilibiliApi(
            createHttpClient {
                defaultRequest {
                    url("https://api.bilibili.com")
                    header(HttpHeaders.Origin, "https://m.bilibili.com")
                    header(HttpHeaders.Referrer, "https://m.bilibili.com")
                }
                install(HttpCookies) {
                    storage = get<CookiesStorage>()
                }
                install(ApiResponseUnwrapper)
                install(ContentNegotiation) {
                    json(HttpClientJson)
                }
                BrowserUserAgent()
                HttpLogging()
            },
            get(),
            get(),
            httpLogger,
        )
    }
    factoryOf(::WbiInitializer)
    singleOf(::CookiesStorageImpl) bind CookiesStorage::class
}