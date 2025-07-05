package com.imcys.bilibilias.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.entity.BILIUserCookiesEntity
import com.imcys.bilibilias.database.entity.BILIUsersEntity


@Database(
    entities = [
        BILIUsersEntity::class,
        BILIUserCookiesEntity::class
    ],
    version = 1,
    exportSchema = false,
)
internal abstract class BILIBILIASDatabase : RoomDatabase() {
    abstract fun biliUsersDao(): BILIUsersDao

    abstract fun biliUserCookiesDao(): BILIUserCookiesDao
}