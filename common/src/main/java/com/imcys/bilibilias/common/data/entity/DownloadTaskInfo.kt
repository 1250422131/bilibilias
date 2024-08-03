package com.imcys.bilibilias.common.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("download_task_info")
data class DownloadTaskInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val pageTitle: String,
    val bvid: String,
    val avid: Long? = null,
    val cid: Long,
    val file: String,
    val fileType: String?,
    val created: Long = System.currentTimeMillis(),
    var error: Int,
    var state: Int,
    val fileUri: String
)
