package com.imcys.bilibilias.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.imcys.bilibilias.common.data.dao.DownloadFinishTaskDao
import com.imcys.bilibilias.common.data.dao.RoamDao
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.common.data.entity.RoamInfo

@Database(
    entities = [
        DownloadFinishTaskInfo::class, RoamInfo::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun downloadFinishTaskDao(): DownloadFinishTaskDao

    abstract fun roamDao(): RoamDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE as_download_finish_task ADD COLUMN video_avid_new INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL(
                    "ALTER TABLE as_download_finish_task ADD COLUMN video_cid_new INTEGER NOT NULL DEFAULT 0"
                )
                database.execSQL("UPDATE as_download_finish_task SET video_avid_new = video_avid")
                database.execSQL("UPDATE as_download_finish_task SET video_cid_new = video_cid")
                database.execSQL("ALTER TABLE as_download_finish_task RENAME TO as_download_finish_task_old")
                database.execSQL(
                    "CREATE TABLE as_download_finish_task (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "video_title TEXT NOT NULL, " +
                        "video_page_title TEXT NOT NULL, " +
                        "video_bvid TEXT NOT NULL, " +
                        "video_avid INTEGER NOT NULL DEFAULT 0, " +
                        "video_cid INTEGER NOT NULL DEFAULT 0, " +
                        "save_path TEXT NOT NULL, " +
                        "file_type INTEGER NOT NULL DEFAULT 0, " +
                        "addtime INTEGER NOT NULL DEFAULT 0)"
                )
                database.execSQL(
                    "INSERT INTO as_download_finish_task " +
                        "(id, video_title, video_page_title, video_bvid, video_avid, video_cid, save_path, file_type, addtime) " +
                        "SELECT id, video_title, video_page_title, video_bvid, video_avid_new, video_cid_new, save_path, file_type, addtime FROM as_download_finish_task_old"
                )
                database.execSQL("DROP TABLE as_download_finish_task_old")
            }
        }
    }
}
