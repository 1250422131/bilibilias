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

/**
 *  @PrimaryKey(autoGenerate = true)
 *     var id: Int = 0,
 *     @ColumnInfo(name = "video_title") var videoTitle: String,
 *     @ColumnInfo(name = "video_page_title") var videoPageTitle: String,
 *     @ColumnInfo(name = "video_bvid") var videoBvid: String,
 *     @ColumnInfo(name = "video_avid") var videoAvid: Long,
 *     @ColumnInfo(name = "video_cid") var videoCid: Long,
 *     @ColumnInfo(name = "save_path") var savePath: String,
 *     @ColumnInfo(name = "file_type") var fileType: Int,
 *     @ColumnInfo(name = "addtime") var addTime: Long = System.currentTimeMillis(),
 *     @Ignore var showEdit: Boolean = false, // 显示编辑状态
 *     @Ignore var selectState: Boolean = false, // 选中状态
 */
@Entity(
    tableName = "download_task_list",
)
data class DownloadTasEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val uri: Uri,
    val created: Instant,
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val aid: Aid,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) val bvid: Bvid,
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val cid: Cid,
    @ColumnInfo("file_type") val fileType: FileType,
    @ColumnInfo(name = "sub_title") val subTitle: String,
    val title: String,
    val state: State,
)
