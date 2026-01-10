package com.imcys.bilibilias.download

import com.imcys.bilibilias.data.model.download.DownloadSubTask
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadStage
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.database.entity.download.DownloadTask
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AppDownloadTask(
    val downloadTask: DownloadTask,
    val downloadSegment: DownloadSegment,
    val downloadSubTasks: List<DownloadSubTask>,
    val downloadViewInfo: DownloadViewInfo,
    val downloadStage: DownloadStage = DownloadStage.DOWNLOAD,
    val cover: String?,
    val progress: Float = 0.0f,
    val downloadState: DownloadState = DownloadState.WAITING,
    var taskRuntimeInfo: TaskRuntimeInfo = TaskRuntimeInfo(),
) {
    fun updateRuntimeInfo(info: TaskRuntimeInfo) {
        this.taskRuntimeInfo = info
    }
}

@Serializable
data class LocalSubtitle(
    @SerialName("lang")
    val lang: String,
    @SerialName("langDoc")
    val langDoc: String,
    @SerialName("path")
    val path: String,
)

@Serializable
data class LocalMediaResource(
    @SerialName("path")
    val path: String,
    @SerialName("fileType")
    val fileType: String,
    @SerialName("trackName")
    val trackName: String,
)

@Serializable
data class TaskRuntimeInfo(
    @SerialName("subtitles")
    var subtitles: List<LocalSubtitle> = emptyList(),
    @SerialName("coverPath")
    var coverPath: String = "",
    @SerialName("outMediaPath")
    var outMediaPath: String = "",
    @SerialName("videoMedia")
    var videoMedia: LocalMediaResource? = null,
    @SerialName("audioMediaList")
    var audioMediaList: List<LocalMediaResource> = emptyList(),
)