package com.imcys.datastore.di

import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import io.fastkv.FastKVConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class FastKVConfigAppInitializer : AppInitializers {
    override fun init() {
        FastKVConfig.setExecutor(Dispatchers.IO.asExecutor())
    }

    override fun getStartType(): AppInitializerStartType {
        return AppInitializerStartType.TYPE_SERIES
    }

    override fun widget(): Int {
        return 0
    }
}
