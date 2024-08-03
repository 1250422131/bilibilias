package com.imcys.bilibilias.common.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.imcys.bilibilias.common.data.entity.DownloadTaskInfo

@Dao
interface DownloadTaskDao {

    @Insert
    suspend fun insert(downloadTaskInfo: DownloadTaskInfo)

    @Query("select * from download_task_info order by id desc")
    suspend fun getByIdOrderList(): MutableList<DownloadTaskInfo>

    @Delete
    suspend fun delete(downloadTaskInfo: DownloadTaskInfo)

    @Query("select * from download_task_info where id = :taskId limit 1")
    suspend fun findById(taskId: Int): DownloadTaskInfo

    @Update
    suspend fun update(info: DownloadTaskInfo)

    @Query("select * from download_task_info where cid =:cid and fileType = :tag")
    suspend fun findByCidAndTag(cid: Long, tag: String): DownloadTaskInfo?
}
