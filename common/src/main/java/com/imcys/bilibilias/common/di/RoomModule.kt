package com.imcys.bilibilias.common.di

import android.content.Context
import androidx.room.Room
import com.imcys.bilibilias.common.data.AppDatabase
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
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()

    @Provides
    @Singleton
    fun provideDownloadFinishTaskDao(
        roomDatabase: AppDatabase,
    ) = roomDatabase.downloadFinishTaskDao()

    @Provides
    @Singleton
    fun provideRoamDao(
        roomDatabase: AppDatabase,
    ) = roomDatabase.roamDao()
}
