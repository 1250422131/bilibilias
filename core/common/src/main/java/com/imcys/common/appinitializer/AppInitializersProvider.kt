package com.imcys.common.appinitializer

import android.app.Application
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AppInitializersProvider @Inject constructor(private val application: Application) {
    private val initializers: Set<AppInitializers> by lazy {
        EntryPointAccessors.fromApplication(application, AppInitializerEntryPoint::class.java)
            .getAppInitializers()
    }

    fun startInit() {
        val seriesList = initializers
            .filter { it.getStartType() == AppInitializerStartType.TYPE_SERIES }
            .sortedByDescending { it.widget() }
        val parallelList = initializers
            .filter { it.getStartType() == AppInitializerStartType.TYPE_PARALLEL }
        Timber.d("开始执行 并行", "AppInitializersProvider")

        parallelList.parallelStream().forEach {
            MainScope().launch(Dispatchers.IO) {
                it.init(application)
            }
        }

        Timber.d("结束执行 并行", "AppInitializersProvider")
        seriesList.forEach {
            it.init(application)
        }
    }
}
