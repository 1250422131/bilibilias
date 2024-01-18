package com.imcys.network.download

import android.content.Context
import androidx.collection.ArrayMap
import androidx.collection.LongSparseArray
import com.imcys.bilibilias.okdownloader.Download
import com.imcys.bilibilias.okdownloader.Downloader
import com.imcys.network.constant.BILIBILI_WEB_URL
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

val Context.downloadDir: File
    get() = File(this.filesDir.parent, "download").apply {
        mkdirs()
    }

enum class DownloadTag(val tagName: String) {
    VIDEO(VIDEO_M4S), AUDIO(AUDIO_M4S)
}

@Singleton
class OkDownload @Inject constructor(
    private val downloader: Downloader
) : Download.Callback {
    private val retryVideo = LongSparseArray<MutableList<String>>(0)
    private val retryAudio = LongSparseArray<MutableList<String>>(0)
    private val groupProgress = ArrayMap<Long, Long>(0)

    fun enqueueTask(
        path: String,
        cId: Long,
        baseUrl: String,
        backupUrl: List<String>,
        tag: DownloadTag
    ) {
        addRetryList(tag, cId, backupUrl)
        val request = request(baseUrl, path, cId, tag, backupUrl)
        enqueue(request)
    }

    override fun onRetrying(call: Download.Call) {
        val groupId = call.request.groupId
        tryGetNewUrl(call.request.tag!!, groupId)?.let {
            onRetryOrFailure(call, it)
        }
    }

    override fun onFailure(call: Download.Call, response: Download.Response) {
        val groupId = call.request.groupId
        tryGetNewUrl(call.request.tag!!, groupId)?.let {
            onRetryOrFailure(call, it)
        }
        if (response.retryCount == call.request.retry) {
            remove(call.request.tag, groupId)
        }
    }

    override fun onLoading(call: Download.Call, current: Long, total: Long) {
        val groupId = call.request.groupId
        val group = groupProgress[groupId]
        if (group == null) {
            groupProgress[groupId] = 0L
        } else {
            groupProgress[groupId] = group + current / 2
        }
        groupProgress[call.request.groupId] = current
    }

    override fun onSuccess(call: Download.Call, response: Download.Response) {
        if (!response.isSuccessful()) return
        val tag = call.request.tag
        val groupId = call.request.groupId
        remove(tag, groupId)
    }

    private fun remove(tag: String?, groupId: Long) {
        tags(tag).remove(groupId)
    }

    private fun request(
        baseUrl: String,
        path: String,
        cId: Long,
        tag: DownloadTag,
        backupUrl: List<String>
    ): Download.Request {
        return Download.Request.Builder()
            .url(baseUrl)
            .into(File(path, tag.tagName))
            .header(HttpHeaders.Referrer, BILIBILI_WEB_URL)
            .priority(Download.Priority.MIDDLE)
            .tag(tag.tagName)
            .retry(backupUrl.size)
            .groupId(cId)
            .build()
    }

    private fun addRetryList(tag: DownloadTag, cId: Long, backupUrl: List<String>) {
        when (tag) {
            DownloadTag.VIDEO -> retryVideo.put(cId, backupUrl.toMutableList())
            DownloadTag.AUDIO -> retryAudio.put(cId, backupUrl.toMutableList())
        }
    }

    private fun enqueue(request: Download.Request) {
        val call = downloader.newCall(request)
        call.enqueue(this)
    }

    // 通过 tag 查询
    private fun tags(tag: String?): LongSparseArray<MutableList<String>> {
        check(tag == VIDEO_M4S || tag == AUDIO_M4S) { "tag 必须为 VIDEO_M4S or AUDIO_M4S" }
        return if (tag == VIDEO_M4S) {
            retryVideo
        } else {
            retryAudio
        }
    }

    private fun tryGetNewUrl(tagName: String, groupId: Long): String? {
        val tags = tags(tagName)
        return tags[groupId]?.removeFirstOrNull()
    }

    private fun onRetryOrFailure(call: Download.Call, newUrl: String) {
        val newCall = downloader.newCall(call.request.newBuilder().url(newUrl).build())
        newCall.enqueue(this)
    }
}
