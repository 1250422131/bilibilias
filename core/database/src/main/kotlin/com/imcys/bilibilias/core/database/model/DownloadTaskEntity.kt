package com.imcys.bilibilias.core.database.model

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.MapColumn
import androidx.room.PrimaryKey
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

@Entity(
    tableName = "download_task_list",
)
// todo 记录错误信息
data class DownloadTaskEntity(
    val uri: Uri,
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val aid: Aid,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) val bvid: Bvid,
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val cid: Cid,
    @ColumnInfo("file_type") val fileType: FileType,
    @ColumnInfo(name = "sub_title") val subTitle: String,
    val title: String,
    val created: Instant = Clock.System.now(),
    val state: State = State.PENDING,
    val bytesSentTotal: Long = 0,
    val contentLength: Long = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {

    @get:Ignore
    val progress: Float
        get() = progressCalculation()

    @Ignore
    val isCompelete = bytesSentTotal == contentLength
    private fun progressCalculation(): Float = when (contentLength) {
        UNKNOWN_TOTAL_OFFSET -> UNKNOWN_PROGRESS
        0L -> if (bytesSentTotal == 0L) 1f else UNKNOWN_PROGRESS
        else -> bytesSentTotal * 1.0f / contentLength
    }

    override fun toString(): String {
        return "DownloadTaskEntity(aid=$aid, bvid='$bvid', cid=$cid, fileType=$fileType, subTitle='$subTitle', state=$state, bytesSentTotal=$bytesSentTotal, contentLength=$contentLength, id=$id, created=$created)"
    }

    companion object {
        const val UNKNOWN_TOTAL_OFFSET = -1L
        const val UNKNOWN_PROGRESS = 0f
        fun createTestDownloadTaskEntity(): DownloadTaskEntity {
            val uuid = UUID.randomUUID()
            val uuidString = uuid.toString()
            val uuidInt = uuid.timestamp()
            return DownloadTaskEntity(
                uuidString.toUri(),
                uuidInt,
                uuidString,
                uuidInt,
                FileType.AUDIO,
                "subtitle",
                "title",
                id = uuidInt.toInt(),
            )
        }
    }
}
