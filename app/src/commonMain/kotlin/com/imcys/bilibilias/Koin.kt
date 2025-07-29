package com.imcys.bilibilias

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.coroutines.AsDispatchers.applicationScope
import com.imcys.bilibilias.core.data.UseCaseModule
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.di.CommonModule
import com.imcys.bilibilias.core.di.IO
import com.imcys.bilibilias.core.di.applicationScope
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.KtorPersistentHttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.core.media.cache.DataStoreMediaCacheStorage
import com.imcys.bilibilias.core.media.cache.MediaCacheSave
import com.imcys.bilibilias.core.media.cache.MediaCacheStorage
import com.imcys.bilibilias.logic.cache.CacheComponent
import com.imcys.bilibilias.logic.cache.DefaultCacheComponent
import com.imcys.bilibilias.logic.login.DefaultLoginComponent
import com.imcys.bilibilias.logic.login.LoginComponent
import com.imcys.bilibilias.logic.player.DefaultPlayerComponent
import com.imcys.bilibilias.logic.player.PlayerComponent
import com.imcys.bilibilias.logic.root.AppComponentContext
import com.imcys.bilibilias.logic.root.DefaultAppComponentContext
import com.imcys.bilibilias.logic.root.DefaultRootComponent
import com.imcys.bilibilias.logic.root.RootComponent
import com.imcys.bilibilias.logic.search.DefaultSearchComponent
import com.imcys.bilibilias.logic.search.SearchComponent
import com.imcys.bilibilias.logic.setting.DefaultSettingsComponent
import com.imcys.bilibilias.logic.setting.SettingsComponent
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
import org.koin.core.module.dsl.singleOf
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
    includes(otherModules(), UseCaseModule, CommonModule)
}

private fun KoinApplication.otherModules() = module {
    singleOf(::DefaultAppComponentContext) bind AppComponentContext::class
    singleOf(::DefaultRootComponent) bind RootComponent::class
    singleOf(::DefaultSearchComponent) bind SearchComponent::class
    singleOf(::DefaultCacheComponent) bind CacheComponent::class
    singleOf(::DefaultLoginComponent) bind LoginComponent::class
    singleOf(::DefaultPlayerComponent) bind PlayerComponent::class
    singleOf(::DefaultSettingsComponent) bind SettingsComponent::class
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
    @OptIn(ExperimentalTime::class)
    single<MediaCacheStorage> {
        DataStoreMediaCacheStorage(
            store = DataStoreFactory.new(
                serializer = ListSerializer(MediaCacheSave.serializer()).asDataStoreSerializer { emptyList() },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
                produceFile = { resolveDataStoreFile("media_cache_storage") },
                scope = CoroutineScope(applicationScope.coroutineContext + IO),
            )
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