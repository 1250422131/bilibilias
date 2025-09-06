package com.imcys.bilibilias

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.data.di.DataModule
import com.imcys.bilibilias.core.datasource.DataSourceModule
import com.imcys.bilibilias.core.datasource.utils.WbiInitializer
import com.imcys.bilibilias.core.datastore.DataStoreModule
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.di.CommonModule
import com.imcys.bilibilias.core.di.applicationScope
import com.imcys.bilibilias.core.domain.UseCaseModule
import com.imcys.bilibilias.core.ffmpeg.MediaMultiplexerModule
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.KtorPersistentHttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.core.logging.logger
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
        modules(platformModule())
        startCommonKoinModule(koin.get())
    }
}

fun KoinApplication.commonModules() = module {
    includes(
        NavigationModule,
        CommonModule,
        DataModule,
        DataSourceModule,
        DataStoreModule,
        LogicModule,
        MediaMultiplexerModule,
        UseCaseModule,
        otherModules()
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
                        private val logger =
                            logger<HttpDownloader>()

                        override fun log(message: String) {
                            logger.info { message }
                        }
                    }
                }
            },
            fileSystem = SystemFileSystem,
            baseSaveDir = Path(BuildConfig.MEDIA_DOWNLOAD),
        )
    }
    single<CoroutineScope> {
        CoroutineScope(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                logger("ApplicationScope").warn(throwable) {
                    "Uncaught exception in coroutine $coroutineContext"
                }
            } + SupervisorJob() + Dispatchers.IO,
        )
    }
}

fun KoinApplication.startCommonKoinModule(
    coroutineScope: CoroutineScope,
): KoinApplication {
    coroutineScope.launch {
        koin.get<HttpDownloader>().init()
    }
    coroutineScope.launch {
        koin.get<WbiInitializer>().initialize()
    }
    return this
}

expect fun KoinApplication.platformModule(): Module