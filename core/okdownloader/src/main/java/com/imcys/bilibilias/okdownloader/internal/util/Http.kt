package com.imcys.bilibilias.okdownloader.internal.util

import com.imcys.bilibilias.okdownloader.Downloader
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

private const val HEADER_CONTENT_LENGTH = "Content-Length"
private const val HEADER_RANGE = "Range"
private const val RANGE_START_PARAM = "bytes=%s-"

internal fun Response.contentLength() = this.header(HEADER_CONTENT_LENGTH)?.toLongOrNull() ?: 0L

internal fun Request.Builder.ofRangeStart(start: Long): Request.Builder {
    if (start != 0L) {
        this.addHeader(HEADER_RANGE, String.format(RANGE_START_PARAM, start))
    }
    return this
}

internal fun OkHttpClient.asFactory(): Downloader.Factory<OkHttpClient> = Downloader.Factory { this }

internal val DefaultOkhttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .cache(null)
        .build()
}
