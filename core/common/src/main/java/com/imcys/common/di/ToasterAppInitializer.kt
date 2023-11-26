package com.imcys.common.di

import android.app.Application
import com.hjq.toast.Toaster
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import javax.inject.Inject

class ToasterAppInitializer @Inject constructor(private val application: Application) :
    AppInitializers {
    override fun init() {
        Toaster.init(application)
    }

    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 0
}
