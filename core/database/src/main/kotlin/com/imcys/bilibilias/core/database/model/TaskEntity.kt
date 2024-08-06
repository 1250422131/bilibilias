package com.imcys.bilibilias.core.database.model

import com.ctrip.sqllin.dsl.annotation.DBRow
import kotlinx.serialization.Serializable

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
