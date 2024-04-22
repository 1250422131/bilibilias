package com.imcys.bilibilias.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.imcys.bilibilias.core.database.model.DownloadTasEntity

@Dao
interface DownloadTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(downloadTasEntity: DownloadTasEntity)
}
