package com.imcys.bilibilias.common.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "as_download_finish_task")
data class DownloadFinishTaskInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "video_title") val videoTitle: String,
    @ColumnInfo(name = "video_page_title") val videoPageTitle: String,
    @ColumnInfo(name = "video_bvid") val videoBvid: String,
    @ColumnInfo(name = "video_avid") val videoAvid: Int,
    @ColumnInfo(name = "video_cid") val videoCid: Int,
    @ColumnInfo(name = "save_path") val savePath: String,
    @ColumnInfo(name = "file_type") val fileType: Int,
    @ColumnInfo(name = "addtime") val addTime: Long = System.currentTimeMillis(),
) {
    constructor() : this(0, "", "", "", 0, 0, "", 0)

}