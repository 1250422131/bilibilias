package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.app.BaseApplication
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.cache.storage.FileStorage

class CacheManager : CacheStorage by FileStorage(BaseApplication.applicationContext().cacheDir)
