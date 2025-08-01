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
import com.imcys.bilibilias.core.di.CommonModule
import com.imcys.bilibilias.core.di.IO
import com.imcys.bilibilias.core.di.applicationScope
import com.imcys.bilibilias.core.domain.UseCaseModule
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.KtorPersistentHttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.logic.LogicModule
import com.imcys.bilibilias.logic.root.AppComponentContext
import com.imcys.bilibilias.logic.root.DefaultAppComponentContext
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.builtins.ListSerializer
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.runOnKoinStarted
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
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
        modules(PlatformModule)
    }
}

fun KoinApplication.commonModules() = module {
    includes(
        otherModules(),
        UseCaseModule,
        CommonModule,
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
                scope = CoroutineScope(applicationScope.coroutineContext + IO),
            ),
            client = createHttpClient {
                defaultRequest {
                    header(HttpHeaders.Accept, "*/*")
                    header(HttpHeaders.Referrer, "https://www.bilibili.com")
                    header(HttpHeaders.Origin, "https://www.bilibili.com")
                    header(HttpHeaders.Connection, "keep-alive")
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
            } + SupervisorJob() + IO,
        )
    }
}

fun KoinApplication.startCommonKoinModule(
    coroutineScope: CoroutineScope,
): KoinApplication {
    coroutineScope.launch {
        koin.get<HttpDownloader>().init()
    }
    return this
}

expect val PlatformModule: Module