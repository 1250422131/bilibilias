package com.imcys.bilibilias.core.database.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import kotlinx.datetime.Instant

@Entity(
    tableName = "download_task_list",
)
data class DownloadTaskEntity(
    val uri: Uri,
    val created: Instant,
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val aid: Aid,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) val bvid: Bvid,
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val cid: Cid,
    @ColumnInfo("file_type") val fileType: FileType,
    @ColumnInfo(name = "sub_title") val subTitle: String,
    val title: String,
    val state: State,
    val bytesSentTotal: Long = 0,
    val contentLength: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
