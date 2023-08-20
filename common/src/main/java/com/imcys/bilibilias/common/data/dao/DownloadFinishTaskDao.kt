package com.imcys.bilibilias.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo

@Dao
interface DownloadFinishTaskDao {

    @Insert
    suspend fun insert(downloadFinishTaskInfo: DownloadFinishTaskInfo)

    @Query("SELECT * from as_download_finish_task ORDER BY id DESC")
    suspend fun getByIdOrderList(): MutableList<DownloadFinishTaskInfo>

    @Delete
    suspend fun delete(downloadFinishTaskInfo: DownloadFinishTaskInfo)

    @Query("SELECT * from as_download_finish_task WHERE id = :taskId LIMIT 1")
    suspend fun findById(taskId: Int): DownloadFinishTaskInfo
}
