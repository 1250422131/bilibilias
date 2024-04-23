package com.imcys.bilibilias.core.database.di

import com.imcys.bilibilias.core.database.AsDatabase
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DaosModule {
    @Provides
    fun providesDownloadTaskDao(
        database: AsDatabase,
    ): DownloadTaskDao = database.downloadTaskDao()
}
