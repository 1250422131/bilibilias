package com.imcys.network.di

import com.imcys.network.configration.NetworkListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.EventListener

@Module
@InstallIn(SingletonComponent::class)
class EventListenerModule {
    @Provides
    fun provideEventListener(): EventListener.Factory =
        NetworkListener.Factory()
}