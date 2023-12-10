package com.imcys.common.di

import android.app.Application
import com.bilias.crash.Crash
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import java.io.File
import javax.inject.Inject

class CrashAppInitializer @Inject constructor(private val application: Application) :
    AppInitializers {
    override fun init() {
        Crash.initCrash(application, application.filesDir.absolutePath + File.separator + "crash")
    }

    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 0
}
