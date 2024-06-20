package com.imcys.bilibilias.core.database.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
        val task = findByIdWithFileType(t.aid, t.bvid, t.cid, t.fileType)
        if (task == null) {
            insertTask(t)
        } else {
            updateTask(
                task.copy(
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(downloadTaskEntity: DownloadTaskEntity)

    @Update
    suspend fun updateTask(downloadTaskEntity: DownloadTaskEntity)

    @Query("SELECT * FROM download_task_list WHERE aid = :aid AND bvid = :bvid AND cid = :cid AND file_type=:fileType")
    suspend fun findByIdWithFileType(
        aid: Aid,
        bvid: Bvid,
        cid: Cid,
        fileType: FileType
    ): DownloadTaskEntity?

    @Query("SELECT * FROM download_task_list WHERE aid = :aid AND bvid = :bvid AND cid = :cid")
    suspend fun findById(
        aid: Aid,
        bvid: Bvid,
        cid: Cid,
    ): List<DownloadTaskEntity>

    @Query("SELECT * FROM download_task_list WHERE uri = :uri")
    suspend fun findByUri(uri: Uri): DownloadTaskEntity

    @Query(
        "UPDATE download_task_list " +
                "SET bytesSentTotal = :bytesSentTotal, contentLength = :contentLength " +
                "WHERE uri = :uri"
    )
    fun updateProgressByUri(bytesSentTotal: Long, contentLength: Long, uri: Uri)

    @Query(
        "UPDATE download_task_list " +
                "SET state = :state " +
                "WHERE uri = :uri"
    )
    fun updateStateByUri(state: State, uri: Uri)

    @Query(
        "UPDATE download_task_list " +
                "SET state = :state, bytesSentTotal = :bytesSentTotal, contentLength = :contentLength " +
                "WHERE uri = :uri"
    )
    fun updateProgressWithStateByUri(
        state: State,
        bytesSentTotal: Long,
        contentLength: Long,
        uri: Uri,
    )

    @Query("SELECT * FROM download_task_list")
    fun findAllTask(): Flow<List<DownloadTaskEntity>>

    @Delete
    fun delete(entity: DownloadTaskEntity)
}
