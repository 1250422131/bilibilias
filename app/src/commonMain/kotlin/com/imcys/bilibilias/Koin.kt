package com.imcys.bilibilias

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.coroutines.AsDispatchers.applicationScope
import com.imcys.bilibilias.core.datasource.DataSourceModule
import com.imcys.bilibilias.core.datastore.DataStoreModule
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.di.applicationScope
import com.imcys.bilibilias.core.domain.UseCaseModule
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.KtorPersistentHttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.logic.LogicModule
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.builtins.ListSerializer
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        if (config != null) {
            includes(config)
        } else {
            printLogger()
        }
        startCommonKoinModule(applicationScope)
        modules(platformModule())
    }
}

fun KoinApplication.commonModules() = module {
    includes(
        otherModules(),
        UseCaseModule,
        DataSourceModule,
        LogicModule,
        DataStoreModule
    )
}

private fun KoinApplication.otherModules() = module {
    @OptIn(ExperimentalTime::class)
    single<HttpDownloader> {
        KtorPersistentHttpDownloader(
            dataStore = DataStoreFactory.new(
                ListSerializer(DownloadState.serializer()).asDataStoreSerializer { emptyList() },
                produceFile = { resolveDataStoreFile("ktor_persistent_http_downloader") },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO),
            ),
            client = createHttpClient {
                defaultRequest {
                    header(HttpHeaders.Referrer, "https://www.bilibili.com")
                }
                Logging {
                    level = LogLevel.HEADERS
                    logger = object : io.ktor.client.plugins.logging.Logger {
                        override fun log(message: String) {
                            Logger.i("HttpDownloader") { message }
                        }
                    }
                }
            },
            fileSystem = SystemFileSystem,
            baseSaveDir = Path(KmpContext.dataDir, "Download"),
        )
    }
    single<CoroutineScope> {
        CoroutineScope(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                Logger.w("ApplicationScope", throwable) {
                    "Uncaught exception in coroutine $coroutineContext"
                }
            } + SupervisorJob() + Dispatchers.IO,
        )
    }
    single { KmpContext }
}

fun KoinApplication.startCommonKoinModule(
    coroutineScope: CoroutineScope,
): KoinApplication {
    coroutineScope.launch {
        koin.get<HttpDownloader>().init()
    }
    return this
}

expect fun KoinApplication.platformModule(): Module