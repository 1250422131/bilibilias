package com.imcys.bilibilias.core.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datasource.persistent.CookiePersistent
import com.imcys.bilibilias.core.datasource.persistent.TokenPersistent
import com.imcys.bilibilias.core.datasource.persistent.TokenSave
import com.imcys.bilibilias.core.datasource.persistent.TokenSave.Companion.INIT
import com.imcys.bilibilias.core.datastore.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.di.IO
import com.imcys.bilibilias.core.di.applicationScope
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.builtins.ListSerializer
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val DataSourceModule = module {
    singleOf(::CookiePersistent) bind CookiesStorage::class
    singleOf(::TokenPersistent)
    factoryOf(::BilibiliLoginApi)
    single<DataStore<List<Cookie>>>(named("Cookie")) {
        DataStoreFactory.new(
            serializer = ListSerializer(Cookie.serializer()).asDataStoreSerializer { emptyList() },
            produceFile = { resolveDataStoreFile("cookies") },
            corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
            scope = CoroutineScope(applicationScope.coroutineContext + IO)
        )
    }
    single<DataStore<TokenSave>>(named("TokenSave")) {
        DataStoreFactory.new(
            serializer = TokenSave.serializer().asDataStoreSerializer { INIT },
            corruptionHandler = ReplaceFileCorruptionHandler { INIT },
            produceFile = { resolveDataStoreFile("token") },
            scope = CoroutineScope(applicationScope.coroutineContext + IO)
        )
    }
}