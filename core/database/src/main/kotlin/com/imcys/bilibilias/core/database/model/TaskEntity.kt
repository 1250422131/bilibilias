package com.imcys.bilibilias.core.database.model

import android.net.Uri
import com.ctrip.sqllin.dsl.annotation.DBRow
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity.Companion.UNKNOWN_PROGRESS
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity.Companion.UNKNOWN_TOTAL_OFFSET
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@DBRow
@Serializable
data class TaskEntity(
    val uri: String,
    val created: Long,
    val aid: Long,
    val bvid: String,
    val cid: Long,
    val fileType: String,
    val subTitle: String,
    val title: String,
    val state: String,
    val bytesSentTotal: Long = 0,
    val contentLength: Long = 0,
)
