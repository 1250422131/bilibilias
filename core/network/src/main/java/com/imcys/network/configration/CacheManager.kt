package com.imcys.network.configration

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager@Inject constructor(@ApplicationContext context: Context) :
    CacheStorage by FileStorage(context.cacheDir)
