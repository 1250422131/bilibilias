package com.imcys.bilibilias.common.di

import android.content.Context
import androidx.room.Room
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.dao.DownloadTaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BILIBILIAS_DATABASE",
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDownloadTaskDao(
        roomDatabase: AppDatabase,
    ): DownloadTaskDao = roomDatabase.downloadTaskDao()

    @Provides
    @Singleton
    fun provideRoamDao(
        roomDatabase: AppDatabase,
    ) = roomDatabase.roamDao()
}
