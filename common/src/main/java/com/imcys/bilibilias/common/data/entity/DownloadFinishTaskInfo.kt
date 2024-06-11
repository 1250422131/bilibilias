package com.imcys.bilibilias.common.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.imcys.deeprecopy.an.EnhancedData

@EnhancedData
@Entity(tableName = "as_download_finish_task")
data class DownloadFinishTaskInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "video_title") var videoTitle: String,
    @ColumnInfo(name = "video_page_title") var videoPageTitle: String,
    @ColumnInfo(name = "video_bvid") var videoBvid: String,
    @ColumnInfo(name = "video_avid") var videoAvid: Long,
    @ColumnInfo(name = "video_cid") var videoCid: Long,
    @ColumnInfo(name = "save_path") var savePath: String,
    @ColumnInfo(name = "saf_path") var safPath: String,
    @ColumnInfo(name = "file_type") var fileType: Int,
    @ColumnInfo(name = "addtime") var addTime: Long = System.currentTimeMillis(),
    @Ignore var showEdit: Boolean = false, // 显示编辑状态
    @Ignore var selectState: Boolean = false, // 选中状态
) {

    constructor() : this(0, "", "", "", 0, 0, "", "", 0)
}
