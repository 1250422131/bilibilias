package com.imcys.datastore.datastore

import androidx.datastore.core.DataStore
import com.bilias.core.datastore.AsCookie
import com.bilias.core.datastore.UserCookie
import com.imcys.common.di.AppCoroutineScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class CookieDataSource @Inject constructor(
    private val store: DataStore<UserCookie>,
    @AppCoroutineScope private val scope: CoroutineScope
) : ICookieDataSource {
    private val cache = mutableMapOf<String, AsCookie>()

    init {
        scope.launch {
            cache += store.data.first().store
        }
    }

    override fun getAll(): ImmutableList<AsCookie> {
        return cache.values.toImmutableList()
    }

    override fun get(name: String): AsCookie? {
        return cache[name]
    }

    override fun set(name: String, value: AsCookie) {
        cache[name] = value
    }

    override fun close() {
        scope.launch {
            store.updateData { UserCookie(cache) }
        }
    }
}
