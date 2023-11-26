package com.imcys.datastore.di

import android.app.Application
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import com.tencent.mmkv.MMKV
import javax.inject.Inject

class MMKVAppInitializer @Inject constructor(private val application: Application) :
    AppInitializers {
    override fun init() {
        MMKV.initialize(application)
    }

    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 10
}
