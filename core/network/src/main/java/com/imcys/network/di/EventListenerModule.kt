package com.imcys.network.di

import com.imcys.network.configration.NetworkListener
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.EventListener

@Module
@InstallIn(SingletonComponent::class)
interface EventListenerModule {
    @Binds
    fun bindEventListener(networkListener: NetworkListener): EventListener
}