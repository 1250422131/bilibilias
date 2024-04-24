package com.imcys.bilibilias.core.database.di

import android.content.Context
import androidx.room.Room
import com.imcys.bilibilias.core.database.AsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
    @Provides
    @Singleton
    fun providesAsDatabase(
        @ApplicationContext context: Context,
    ): AsDatabase = Room.databaseBuilder(
        context,
        AsDatabase::class.java,
        "as-database",
    )
        .fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()
}
