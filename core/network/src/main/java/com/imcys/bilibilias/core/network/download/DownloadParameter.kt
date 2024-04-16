package com.imcys.bilibilias.core.network.download

data class DownloadParameter(
    val aid: Long,
    val bvid: String,
    val cid: Long,
    val title: String,
    val format: Format
)

data class Format(val codecid: Int, val taskType: TaskType, val quality: Int)
enum class TaskType(val extension: String) {
    ALL("m4s"),
    VIDEO("mp4"),
    AUDIO("mp4")
}
