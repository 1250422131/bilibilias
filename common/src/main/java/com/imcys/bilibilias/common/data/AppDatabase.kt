package com.imcys.bilibilias.common.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.imcys.bilibilias.common.data.dao.DownloadFinishTaskDao
import com.imcys.bilibilias.common.data.dao.RoamDao
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.common.data.entity.RoamInfo

@Database(
    entities = [
        DownloadFinishTaskInfo::class, RoamInfo::class
    ], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun downloadFinishTaskDao(): DownloadFinishTaskDao

    abstract fun roamDao(): RoamDao


    companion object {

        private const val DB_NAME = "BILIBILIAS_DATABASE"

        // 防止同一时间创建多个实例
        @Volatile
        private var INSTANCE: AppDatabase? = null


        //获取实例
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    // 可以使用单例Application来代替此参数传递
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    //是否允许在主线程进行查询
                    .allowMainThreadQueries()
                    //数据库升级异常之后的回滚
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }


    }

}