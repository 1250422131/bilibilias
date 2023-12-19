package com.imcys.network.di

import android.content.Context
import coil.Coil
import coil.ImageLoader
import com.imcys.common.appinitializer.AppInitializerStartType
import com.imcys.common.appinitializer.AppInitializers
import javax.inject.Inject
import javax.inject.Provider

class CoilAppInitializers @Inject constructor() : AppInitializers {
    @Inject
    lateinit var imageLoader: Provider<ImageLoader>
    override fun init(context: Context) {
        Coil.setImageLoader(imageLoader.get())
    }

    override fun getStartType(): AppInitializerStartType = AppInitializerStartType.TYPE_SERIES


    override fun widget(): Int = 0
}
