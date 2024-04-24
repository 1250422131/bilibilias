package com.imcys.bilibilias.core.database.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadTaskDao {
    suspend fun insertOrUpdate(t: DownloadTaskEntity) {
        val task = loadAllDownloadList().find {
            it.aid == t.aid &&
                    it.bvid == t.bvid &&
                    it.cid == t.cid &&
                    it.fileType == t.fileType
        }
        if (task == null) {
            insertTask(t)
        } else {
            updateTask(
                t.copy(
                    uri = t.uri,
                    created = t.created,
                    subTitle = t.subTitle,
                    title = t.title,
                    state = t.state,
                    bytesSentTotal = t.bytesSentTotal,
                    contentLength = t.contentLength,
                )
            )
        }
    }

    @Upsert
    suspend fun insertOrUpdate2(downloadTaskEntity: DownloadTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(downloadTaskEntity: DownloadTaskEntity)

    @Update
    suspend fun updateTask(downloadTaskEntity: DownloadTaskEntity)

    @Query("SELECT * FROM download_task_list WHERE aid = :aid AND bvid = :bvid AND cid = :cid AND file_type=:fileType")
    suspend fun getTaskByInfo(
        aid: Aid,
        bvid: Bvid,
        cid: Cid,
        fileType: FileType
    ): DownloadTaskEntity?

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
    fun loadAllDownloadFlow(): Flow<List<DownloadTaskEntity>>

    @Query("SELECT * FROM download_task_list")
    fun loadAllDownloadList(): List<DownloadTaskEntity>
}
