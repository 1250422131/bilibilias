package com.imcys.bilibilias.okdownloader.android.internal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val VERSION_1 = 1

/**
 * @author xluotong@gmail.com
 */
@Database(version = VERSION_1, entities = [RecordEntity::class])
internal abstract class DownloadDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: DownloadDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context, DownloadDatabase::class.java, "ok_downloader.db")
                .build().also { INSTANCE = it }
        }
    }
}
