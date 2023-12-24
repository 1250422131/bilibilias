package com.imcys.common.appinitializer.init

import android.app.Application
import android.content.Context
import com.hjq.toast.Toaster
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import javax.inject.Inject

class ToasterAppInitializer @Inject constructor() : AppInitializers {
    override fun init(context: Context) {
        Toaster.init(context as Application)
    }

    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 0
}
