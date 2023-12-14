package com.imcys.common.di

import android.content.Context
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import timber.log.Timber

class TimberAppInitializer: AppInitializers {
    override fun init(context: Context) {
        Timber.plant(Timber.DebugTree())
    }
    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 0
}
