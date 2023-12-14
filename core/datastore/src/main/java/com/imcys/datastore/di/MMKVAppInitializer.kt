package com.imcys.datastore.di

import android.content.Context
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import com.tencent.mmkv.MMKV
import javax.inject.Inject

class MMKVAppInitializer @Inject constructor() : AppInitializers {
    override fun init(context: Context) {
        MMKV.initialize(context)
    }

    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES

    override fun widget(): Int = 10
}
