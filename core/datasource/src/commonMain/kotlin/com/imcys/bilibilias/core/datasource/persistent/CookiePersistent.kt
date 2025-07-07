package com.imcys.bilibilias.core.datasource.persistent

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.imcys.bilibilias.core.datastore.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import io.ktor.http.Cookie
import kotlinx.serialization.builtins.ListSerializer

internal object CookiePersistent {
    private val cookieStore: DataStore<List<Cookie>> by lazy {
        DataStoreFactory.new(
            serializer = ListSerializer(Cookie.serializer()).asDataStoreSerializer { emptyList() },
            produceFile = { resolveDataStoreFile("cookies") },
            corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
        )
    }
    val cookieFlow = cookieStore.data
    suspend fun setCookie(cookie: Cookie) {
        cookieStore.updateData {
            it.filterNot { it.name == cookie.name }
                .plus(cookie)
        }
    }
}

