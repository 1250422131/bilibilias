package com.imcys.bilibilias.core.database.model

import android.net.Uri
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity.Companion.UNKNOWN_PROGRESS
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity.Companion.UNKNOWN_TOTAL_OFFSET
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import kotlinx.datetime.Instant

data class Task(
    val uri: Uri,
    val created: Instant,
    val aid: Aid,
    val bvid: Bvid,
    val cid: Cid,
    val fileType: FileType,
    val subTitle: String,
    val title: String,
    val state: State = State.PENDING,
    val bytesSentTotal: Long = 0,
    val contentLength: Long = 0,
) {
    val progress = when (contentLength) {
        UNKNOWN_TOTAL_OFFSET -> UNKNOWN_PROGRESS
        0L -> if (bytesSentTotal == 0L) 1f else UNKNOWN_PROGRESS
        else -> bytesSentTotal * 1.0f / contentLength
    }

    val isCompelete = state == State.COMPLETED
}
