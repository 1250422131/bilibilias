package com.imcys.bilibilias.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imcys.bilibilias.common.data.dao.DownloadTaskDao
import com.imcys.bilibilias.common.data.dao.RoamDao
import com.imcys.bilibilias.common.data.entity.DownloadTaskInfo
import com.imcys.bilibilias.common.data.entity.RoamInfo

@Database(
    entities = [
        DownloadTaskInfo::class, RoamInfo::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun downloadTaskDao(): DownloadTaskDao

    abstract fun roamDao(): RoamDao
}
