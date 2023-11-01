package com.imcys.datastore.di

import android.content.Context
import com.imcys.datastore.fastkv.CookiesData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModel {
    @Provides
    @Singleton
    fun providesDefaultDispatcher(@ApplicationContext context: Context) = CookiesData(context)
}