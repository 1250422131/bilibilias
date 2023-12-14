package com.imcys.datastore.di

import android.content.Context
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import io.fastkv.FastKVConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

class FastKVConfigAppInitializer : AppInitializers {
    override fun init(context: Context) {
        FastKVConfig.setExecutor(Dispatchers.IO.asExecutor())
    }

    override fun getStartType(): AppInitializerStartType {
        return AppInitializerStartType.TYPE_SERIES
    }

    override fun widget(): Int {
        return 0
    }
}
