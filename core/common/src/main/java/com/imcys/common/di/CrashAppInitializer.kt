package com.imcys.common.di

import android.content.Context
import com.bilias.crash.Crash
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import java.io.File
import javax.inject.Inject

class CrashAppInitializer @Inject constructor() : AppInitializers {
    override fun init(context: Context) {
        Crash.initCrash(context, context.filesDir.absolutePath + File.separator + "crash")
    }

    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 0
}
