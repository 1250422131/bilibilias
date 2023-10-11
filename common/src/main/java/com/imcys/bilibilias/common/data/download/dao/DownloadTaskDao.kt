package com.imcys.bilibilias.common.data.download.dao

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface DownloadTaskDao {
    @Insert
    suspend fun insert(): Long
}
