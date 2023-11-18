package com.imcys.common.di

import com.imcys.common.appinitializer.AppInitializers
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppInitializerEntryPoint {
    fun getAppInitializers(): Set<AppInitializers>
}
