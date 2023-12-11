package com.imcys.datastore.di

import com.imcys.datastore.fastkv.CookiesData
import com.imcys.datastore.fastkv.ICookieStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreModel {
    @Binds
    fun bindICookieStore(cookiesData: CookiesData): ICookieStore
}