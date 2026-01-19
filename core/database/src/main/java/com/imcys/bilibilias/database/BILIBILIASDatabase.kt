package com.imcys.bilibilias.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.dao.DownloadTaskDao
import com.imcys.bilibilias.database.entity.BILIUserCookiesEntity
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadTask
import com.imcys.bilibilias.database.entity.download.DownloadTaskNode


@Database(
    entities = [
        BILIUsersEntity::class,
        BILIUserCookiesEntity::class,
        DownloadTask::class,
        DownloadTaskNode::class,
        DownloadSegment::class,
    ],
    version = 4,
    exportSchema = true,
)
internal abstract class BILIBILIASDatabase : RoomDatabase() {
    abstract fun biliUsersDao(): BILIUsersDao

    abstract fun biliUserCookiesDao(): BILIUserCookiesDao

    abstract fun downloadTaskDao(): DownloadTaskDao

}