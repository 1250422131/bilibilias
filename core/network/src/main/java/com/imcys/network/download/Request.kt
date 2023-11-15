package com.imcys.network.download

import com.billbook.lib.downloader.CallbackExecutor
import com.billbook.lib.downloader.Download
import com.billbook.lib.downloader.Download.Request.Builder
import com.billbook.lib.downloader.internal.util.requireNotNullOrEmpty


class AsDownload : Download {
    class AsRequest(
        url: String,
        path: String,
        tag: String,
        callbackExecutor: CallbackExecutor,
        priority: Download.Priority,
        val aId: String,
        val bvId: String,
        val cId: String,
        val title: String,
        val extra: Extra
    ) : Download.Request(
        url = url,
        path = path,
        md5 = null,
        tag = tag,
        size = null,
        retry = 5,
        callbackExecutor = callbackExecutor,
        priority = priority,
    ) {
        override fun newBuilder(): Download.Request.Builder = Builder(this)

        class Builder : Download.Request.Builder() {
            private var aId: String? = null
            private var bvId: String? = null
            private var cId: String? = null
            private var title: String? = null
            private var extra: Extra? = null
            fun aId(aId: String) = apply {
                this.aId = aId
            }

            fun bvId(bvId: String) = apply {
                this.bvId = bvId
            }

            fun cId(cId: String) = apply {
                this.cId = cId
            }

            fun title(cId: String) = apply {
                this.title = cId
            }

            fun extra(extra: Extra) = apply {
                this.extra = extra
            }

            override fun build(): AsRequest {
                return AsRequest(
                    url = requireNotNullOrEmpty(url) { "Missing url!" },
                    path = requireNotNullOrEmpty(path) { "Missing path!" },
                    tag = requireNotNullOrEmpty(tag) { "Missing tag!" },
                    callbackExecutor = callbackExecutor,
                    priority = priority,
                    aId = requireNotNullOrEmpty(aId) { "Missing aId!" },
                    bvId = requireNotNullOrEmpty(bvId) { "Missing bvId!" },
                    cId = requireNotNullOrEmpty(cId) { "Missing cId!" },
                    title = requireNotNullOrEmpty(title) { "Missing title!" },
                    extra = requireNotNull(extra) { "Missing title!" }
                )
            }
        }
    }
}

data class Extra(
    val url: String,
    val backupUrl: List<String>
)
