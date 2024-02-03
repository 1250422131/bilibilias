package com.imcys.datastore.di

import com.imcys.datastore.datastore.CookieDataSource
import com.imcys.datastore.datastore.ICookieDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModel {
    @Binds
    fun bindICookieDataSource(cookieDataSource: CookieDataSource): ICookieDataSource
}
