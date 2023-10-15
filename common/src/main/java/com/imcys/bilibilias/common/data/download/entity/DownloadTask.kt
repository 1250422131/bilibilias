package com.imcys.bilibilias.common.data.download.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DownloadTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val path: String,
    val title: String,
    val bvid: String,
    val cid: Long,
    val fileType: DownloadFileType,
    val created: Long = System.currentTimeMillis(),
)

enum class DownloadFileType {
    VideoAndAudio,
    OnlyAudio,
}
