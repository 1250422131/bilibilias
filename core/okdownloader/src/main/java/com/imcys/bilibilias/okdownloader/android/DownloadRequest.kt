package com.imcys.bilibilias.okdownloader.android

import com.imcys.bilibilias.okdownloader.Download
import com.imcys.bilibilias.okdownloader.data.Extras
import com.imcys.bilibilias.okdownloader.internal.util.requireNotNullOrEmpty

open class DownloadRequest protected constructor(
    url: String,
    path: String,
    md5: String?,
    tag: String?,
    size: Long?,
    retry: Int?,
    priority: Download.Priority,
    internal val network: Int,
    extras: Extras,
    headers: Map<String, String>,
) : Download.Request(
    url = url,
    path = path,
    md5 = md5,
    tag = tag,
    size = size,
    retry = retry,
    priority = priority,
    extras = extras,
    headers = headers
) {

    override fun newBuilder(): Download.Request.Builder {
        return Builder(this)
    }

    override fun toString(): String {
        return "DownloadRequest(network=$network) ${super.toString()}"
    }

    open class Builder : Download.Request.Builder {

        protected var network: Int = NETWORK_WIFI or NETWORK_DATA

        constructor()

        constructor(request: DownloadRequest) : super(request) {
            this.network = request.network
        }

        override fun url(url: String): Builder = apply {
            this.url = url
        }

        override fun into(path: String): Builder = apply {
            this.path = path
        }

        override fun md5(md5: String): Builder = apply {
            this.md5 = md5
        }

        open fun networkOn(network: Int): Builder = apply {
            this.network = network
        }

        override fun tag(tag: String): Builder = apply {
            this.tag = tag
        }

        override fun size(size: Long): Builder = apply {
            this.size = size
        }

        override fun retry(retry: Int): Builder = apply {
            this.retry = retry
        }

        override fun priority(priority: Download.Priority): Builder = apply {
            this.priority = priority
        }

        override fun build(): DownloadRequest {
            return DownloadRequest(
                url = requireNotNullOrEmpty(url) { "Missing url!" },
                path = requireNotNullOrEmpty(path) { "Missing path!" },
                md5 = md5,
                size = size,
                retry = retry,
                tag = tag,
                priority = priority,
                network = network,
                extras = Extras.emptyExtras,
                headers = mapOf()
            )
        }
    }

    companion object {
        const val NETWORK_WIFI = 0x00000001
        const val NETWORK_DATA = 0x00000002
    }
}
