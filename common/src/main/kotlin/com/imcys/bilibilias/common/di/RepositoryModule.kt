package com.imcys.bilibilias.common.di

import com.imcys.bilibilias.common.data.dao.DownloadFinishTaskDao
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDownloadFinishTaskRepository(
        dao: DownloadFinishTaskDao,
    ): DownloadFinishTaskRepository = DownloadFinishTaskRepository(dao)
}
