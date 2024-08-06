package com.imcys.bilibilias.core.database

import androidx.room.BuiltInTypeConverters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.database.util.InstantConverter
import com.imcys.bilibilias.core.database.util.UriConverters

@Database(
    entities = [DownloadTaskEntity::class],
    version = 2,
    autoMigrations = [],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    UriConverters::class,
    builtInTypeConverters = BuiltInTypeConverters(),
)
internal abstract class AsDatabase : RoomDatabase() {
    abstract fun downloadTaskDao(): DownloadTaskDao
}
