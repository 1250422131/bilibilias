package com.imcys.bilibilias.core.http.downloader.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SegmentInfo(
    val index: Int,
    val url: String,
    val isDownloaded: Boolean,
    val byteSize: Long = -1,
    @SerialName("tempFilePath")
    val relativeTempFilePath: String,
    val rangeStart: Long? = null,
    val rangeEnd: Long? = null,
)