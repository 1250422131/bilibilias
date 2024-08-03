package com.imcys.bilibilias.okdownloader

import android.net.Uri
import com.imcys.bilibilias.okdownloader.data.Extras
import java.io.File

const val DEFAULT_UNIQUE_IDENTIFIER = 0L
const val DEFAULT_GROUP_ID = 0L
const val DEFAULT_RETRY = 5
const val DEFAULT_PRIORITY = 0

class RequestInfo(val url: String, val file: String) {

    var identifier: Long = DEFAULT_UNIQUE_IDENTIFIER

    var groupId: Long = DEFAULT_GROUP_ID

    val md5: String? = null

    val size: Long? = null

    var retry: Int = DEFAULT_RETRY
        set(value) {
            require(value >= 0) { "The maximum number of attempts has to be greater than -1" }
            field = value
        }

    var priority: Int = DEFAULT_PRIORITY

    var tag: String? = null

    val id: Long = getUniqueId(url, file)

    val fileUri: Uri = getFileUri(file)

    var extras: Extras = Extras.emptyExtras
        set(value) {
            field = value.copy()
        }

    val headers: MutableMap<String, String> = mutableMapOf()
    fun addHeader(name: String, value: String) = apply {
        headers[name] = value
    }
}

fun requestInfo(url: String, file: String, builderAction: RequestInfo.() -> Unit): RequestInfo {
    return RequestInfo(url, file).apply(builderAction)
}

fun isUriPath(path: String): Boolean = path.takeIf { it.isNotEmpty() }
    ?.let { it.startsWith("content://") || it.startsWith("file://") }
    ?: false

fun getFileUri(path: String): Uri {
    return when {
        isUriPath(path) -> Uri.parse(path)
        else -> Uri.fromFile(File(path))
    }
}

fun getUniqueId(url: String, file: String): Long {
    return (url.hashCode() * 31 + file.hashCode() * 37).toLong()
}

class Test {
    init {
        requestInfo("", "") {
            identifier
        }
    }
}
