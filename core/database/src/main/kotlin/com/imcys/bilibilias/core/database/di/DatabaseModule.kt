package com.imcys.bilibilias.core.database.di

import android.content.Context
import androidx.room.Room
import com.ctrip.sqllin.driver.DatabaseConfiguration
import com.ctrip.sqllin.driver.toDatabasePath
import com.ctrip.sqllin.dsl.Database
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

    @Provides
    @Singleton
    fun providesAsDatabase2(
        @ApplicationContext context: Context,
    ): Database = Database(
        DatabaseConfiguration(
            name = "as-database2.db",
            path = context.toDatabasePath(),
            version = 1,
            create = {
                it.execSQL(
                    "create table `TaskEntity` (\n" +
                            "  `id` integer not null primary key autoincrement,\n" +
                            "  `uri` TEXT not null,\n" +
                            "  `created` INTEGER not null,\n" +
                            "  `aid` INTEGER not null,\n" +
                            "  `bvid` TEXT not null,\n" +
                            "  `cid` INTEGER not null,\n" +
                            "  `fileType` TEXT not null,\n" +
                            "  `subTitle` TEXT not null,\n" +
                            "  `title` TEXT not null,\n" +
                            "  `state` TEXT not null,\n" +
                            "  `bytesSentTotal` INTEGER not null,\n" +
                            "  `contentLength` INTEGER not null\n" +
                            ")"
                )
            }
        ),
        enableSimpleSQLLog = true
    )
}
