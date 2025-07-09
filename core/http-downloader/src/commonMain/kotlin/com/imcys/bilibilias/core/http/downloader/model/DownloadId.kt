package com.imcys.bilibilias.core.http.downloader.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class DownloadId(val value: String) {
    override fun toString(): String = value
}