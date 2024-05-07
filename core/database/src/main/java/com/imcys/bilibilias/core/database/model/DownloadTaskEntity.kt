package com.imcys.bilibilias.core.database.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
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
) {
    companion object {
        const val UNKNOWN_TOTAL_OFFSET = -1L
        const val UNKNOWN_PROGRESS = 0f
    }

    @Ignore
    val progress = when (contentLength) {
        UNKNOWN_TOTAL_OFFSET -> UNKNOWN_PROGRESS
        0L -> if (bytesSentTotal == 0L) 1f else UNKNOWN_PROGRESS
        else -> bytesSentTotal * 1.0f / contentLength
    }

    @Ignore
    val isCompelete = bytesSentTotal == contentLength
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DownloadTaskEntity

        if (aid != other.aid) return false
        if (bvid != other.bvid) return false
        if (cid != other.cid) return false
        if (fileType != other.fileType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = aid.hashCode()
        result = 31 * result + bvid.hashCode()
        result = 31 * result + cid.hashCode()
        result = 31 * result + fileType.hashCode()
        return result
    }
}
