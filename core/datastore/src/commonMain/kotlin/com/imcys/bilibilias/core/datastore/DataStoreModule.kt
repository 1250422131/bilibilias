package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.datastore.model.MediaCacheSave
import com.imcys.bilibilias.core.datastore.model.TokenSave
import com.imcys.bilibilias.core.datastore.model.TokenSave.Companion.INIT
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import com.imcys.bilibilias.core.di.applicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import org.koin.dsl.module

val DataStoreModule = module {
    single<MediaCacheDataSource> {
        DataStoreMediaCacheDataSource(
            store = DataStoreFactory.new(
                serializer = ListSerializer(MediaCacheSave.serializer()).asDataStoreSerializer { emptyList() },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
                produceFile = { resolveDataStoreFile("media_cache_storage") },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO),
            )
        )
    }
    single {
        AsPreferencesDataSource(
            DataStoreFactory.new(
                serializer = UserPreferences.serializer()
                    .asDataStoreSerializer { UserPreferences.INIT },
                corruptionHandler = ReplaceFileCorruptionHandler { UserPreferences.INIT },
                produceFile = { resolveDataStoreFile("user_preferences") },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO),
            ),
        )
    }
    single {
        CookieJarDataSource(
            DataStoreFactory.new(
                serializer = MapSerializer(String.serializer(), String.serializer())
                    .asDataStoreSerializer { emptyMap() },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyMap() },
                produceFile = { resolveDataStoreFile("cookie_jar") },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO),
            ),
        )
    }
    single<TokenRepository> {
        TokenRepository(
            DataStoreFactory.new(
                serializer = TokenSave.serializer().asDataStoreSerializer { INIT },
                corruptionHandler = ReplaceFileCorruptionHandler { INIT },
                produceFile = { resolveDataStoreFile("token") },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO)
            )
        )
    }
}