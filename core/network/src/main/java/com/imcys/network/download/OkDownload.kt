package com.imcys.network.download

import android.content.Context
import com.imcys.bilibilias.okdownloader.Download
import com.imcys.bilibilias.okdownloader.Downloader
import com.imcys.bilibilias.okdownloader.data.Extras
import com.imcys.network.constants.BILIBILI_WEB_URL
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private val Context.downloadDir: File get() = File(this.filesDir.parent, "download")
const val EXTRAS_ID_A = "A_ID"
const val EXTRAS_ID_B = "BV_ID"
const val EXTRAS_ID_C = "C_ID"
const val EXTRAS_TITLE = "title"

enum class DownloadTag(val tagName: String) {
    VIDEO(VIDEO_M4S), AUDIO(AUDIO_M4S)
}

@Singleton
class OkDownload @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloader: Downloader,
    private val fFmpegMerge: FFmpegMerge
) : Download.Callback {
    private val retryVideo = mutableMapOf<String, MutableList<String>>()
    private val retryAudio = mutableMapOf<String, MutableList<String>>()
    fun enqueueTask(
        aId: String,
        bvId: String,
        cId: String,
        baseUrl: String,
        backupUrl: List<String>,
        tag: DownloadTag
    ) {
        when (tag) {
            DownloadTag.VIDEO -> retryVideo[cId] = backupUrl.toMutableList()
            DownloadTag.AUDIO -> retryAudio[cId] = backupUrl.toMutableList()
        }
        val request = Download.Request.Builder()
            .url(baseUrl)
            .into(File("${context.downloadDir}/$aId/c_$cId", tag.tagName))
            .extras(
                Extras(
                    mapOf(
                        EXTRAS_ID_A to aId,
                        EXTRAS_ID_B to bvId,
                        EXTRAS_ID_C to cId,
                        EXTRAS_TITLE to "test"
                    )
                )
            )
            .header(HttpHeaders.Referrer, BILIBILI_WEB_URL)
            .priority(Download.Priority.MIDDLE)
            .tag(tag.tagName)
            .retry(backupUrl.size)
            .build()
            .apply {
                groupId = cId.toLong()
            }
        enqueue(request)
    }

    private fun enqueue(request: Download.Request) {
        val call = downloader.newCall(request)
        call.enqueue(this)
    }

    override fun onFailure(call: Download.Call, response: Download.Response) {
        val cid = call.getExtras(EXTRAS_ID_C)
        tryGetNewUrl(call.request.tag!!, cid)?.let {
            onRetryOrFailure(call, it)
        }
    }

    fun tryGetNewUrl(tagName: String, cid: String): String? {
        check(tagName == VIDEO_M4S || tagName == AUDIO_M4S) { "try get new url failure" }
        return if (tagName == VIDEO_M4S) {
            retryVideo[cid]?.removeFirstOrNull()
        } else {
            retryAudio[cid]?.removeFirstOrNull()
        }
    }

    fun Download.Call.getExtras(id: String): String = this.request.extras.getString(id, "")

    override fun onRetrying(call: Download.Call) {
        val cid = call.getExtras(EXTRAS_ID_C)
        tryGetNewUrl(call.request.tag!!, cid)?.let {
            onRetryOrFailure(call, it)
        }
    }

    override fun onLoading(call: Download.Call, current: Long, total: Long) {}

    override fun onSuccess(call: Download.Call, response: Download.Response) {
        if (!response.isSuccessful()) return
    }

    private fun onRetryOrFailure(call: Download.Call, newUrl: String) {
        val newCall = downloader.newCall(call.request.newBuilder().url(newUrl).build())
        newCall.enqueue(this)
    }
}
