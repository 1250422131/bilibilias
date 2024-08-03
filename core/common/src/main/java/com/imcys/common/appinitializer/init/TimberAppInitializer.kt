package com.imcys.common.appinitializer.init

import android.content.Context
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import timber.log.Timber

class TimberAppInitializer : AppInitializers {
    override fun init(context: Context) {
        Timber.plant(Timber.DebugTree())
        Napier.base(DebugAntilog())
    }
    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 0
}
