package com.imcys.bilibilias.core.database.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.State
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(downloadTaskEntity: DownloadTaskEntity)

    @Upsert
    suspend fun updateTask(task: DownloadTaskEntity)

    @Query("SELECT * FROM download_task_list WHERE uri = :uri")
    suspend fun getTaskByUri(uri: Uri): DownloadTaskEntity

    @Query(
        "UPDATE download_task_list " +
            "SET bytesSentTotal = :bytesSentTotal, contentLength = :contentLength " +
            "WHERE uri = :uri"
    )
    fun updateProgress(uri: Uri, bytesSentTotal: Long, contentLength: Long)

    @Query(
        "UPDATE download_task_list " +
            "SET state = :state " +
            "WHERE uri = :uri"
    )
    fun updateState(uri: Uri, state: State)

    @Query(
        "UPDATE download_task_list " +
            "SET state = :state, bytesSentTotal = :bytesSentTotal, contentLength = :contentLength " +
            "WHERE uri = :uri"
    )
    fun updateProgressAndState(uri: Uri, state: State, bytesSentTotal: Long, contentLength: Long)

    @Query("SELECT * FROM download_task_list")
    fun getAllTask(): Flow<List<DownloadTaskEntity>>
}
