package com.imcys.bilibilias.feature.download

import android.net.Uri
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import kotlinx.collections.immutable.ImmutableList

data class Model(val entities: List<List<DownloadTask>>)

data class DownloadTask(
    val id: Int,
    val viewInfo: ViewInfo,
    val subTitle: String,
    val fileType: FileType,
    val uri: Uri,
    val progress: Float,
    val state: String,
)

internal fun DownloadTaskEntity.mapToTask(): DownloadTask {
    return DownloadTask(
        id,
        ViewInfo(aid, bvid, cid, title),
        subTitle,
        fileType,
        uri,
        progress,
        state.cn
    )
}

sealed interface Event {
    data class DeleteFile(val viewInfo: ViewInfo, val fileType: FileType) : Event
}
