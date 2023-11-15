package com.imcys.network.download

import android.content.Context
import androidx.collection.ArrayMap
import com.billbook.lib.downloader.CallbackExecutor
import com.billbook.lib.downloader.Download
import com.billbook.lib.downloader.Downloader
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.CommandParams
import com.coder.ffmpeg.utils.FFmpegUtils
import com.hjq.toast.Toaster
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private val Context.downloadDir: File get() = File(this.filesDir.parent, "download")

@Singleton
class OkDownload @Inject constructor(
    @ApplicationContext private val context: Context,
    private val downloader: Downloader
) : Download.Callback {
    private val queuedDownloadCalls = ArrayMap<String, MutableList<Download.Call>>(0)
    private val _completedDownload = mutableListOf<AsDownload.AsRequest>()
    val completedDownload = _completedDownload.toImmutableList()
    fun enqueueTask(
        aId: String,
        bvId: String,
        cId: String,
        baseUrl: String,
        backupUrl: List<String>,
        tag: String
    ) {
        val request = AsDownload.AsRequest.Builder()
            .aId(aId)
            .bvId(bvId)
            .cId(cId)
            .title("Test")
            .extra(Extra(baseUrl, backupUrl))
            .url(baseUrl)
            .into(File("${context.downloadDir}/$aId/c_$cId", tag))
            .priority(Download.Priority.MIDDLE)
            .tag(tag)
            .retry(backupUrl.size)
            .callbackOn(if (tag == VIDEO_M4S) CallbackExecutor.SERIAL else CallbackExecutor.UNCONFINED)
            .build()
        enqueue(request)
    }

    private fun enqueue(request: Download.Request) {
        val call = downloader.newCall(request)
        call.enqueue(this)
        addCall((call.request as AsDownload.AsRequest).cId, call)
    }

    override fun onFailure(call: Download.Call, response: Download.Response) {
        val asRequest = call.request as AsDownload.AsRequest
        onRetryOrFailure(call, asRequest.extra.backupUrl.last())
    }

    override fun onRetrying(call: Download.Call) {
//        val asRequest = call.request as AsDownload.AsRequest
//        onRetryOrFailure(call, asRequest.extra.backupUrl.first())
    }

    override fun onLoading(call: Download.Call, current: Long, total: Long) {
        val asRequest = call.request as AsDownload.AsRequest
    }

    override fun onSuccess(call: Download.Call, response: Download.Response) {
        if (!response.isSuccessful()) return
        val asRequest = call.request as AsDownload.AsRequest
        _completedDownload.add(asRequest)
        val request = getCall(asRequest.cId)
        request.remove(call)
        tryMerge(call)
//        if (asRequest.tag == VIDEO_M4S) {
//            val audio = request.find { it.request.tag == AUDIO_M4S }!!
//            audio.isExecuted()
//            downloader.executorService
//            Timber.d("onSuccess=${audio.request.destFile().absolutePath},${asRequest.destFile().absolutePath}")
//        } else {
//            val video = request.find { it.request.tag == VIDEO_M4S }!!
//            Timber.d("onSuccess=${video.request.destFile().absolutePath},${asRequest.destFile().absolutePath}")
//        }
    }

    private fun tryMerge(call: Download.Call) {
        val asRequest = call.request as AsDownload.AsRequest
        val asRequestList = _completedDownload.filter { it.cId == asRequest.cId }
        if (asRequestList.size < 2) return
        val videoFile =
            asRequestList.find { it.tag == VIDEO_M4S }?.destFile()?.absolutePath
                ?: error("合并失败")
        val audioFile =
            asRequestList.find { it.tag == AUDIO_M4S }?.destFile()?.absolutePath
                ?: error("合并失败")
        merge(videoFile, audioFile, "")
    }

    fun merge(videoFile: String, audioFile: String, title: String) {
        // ffmpeg -i video.mp4 -i audio.wav -c copy output.mkv
        val mergeCommand3 = CommandParams()
            .append("-i")
            .append(videoFile)
            .append("-vcodec")
            .append("copy")
            .append("-i")
            .append(audioFile)
            .append("-acodec")
            .append("copy")
            .append("${context.filesDir}/text_111.mp4")
            .get()
        val m2 = CommandParams()
            .append("-i")
            .append(videoFile)
            .append("-i")
            .append(audioFile)
            .append("-c")
            .append("copy")
            .append(context.filesDir.path + "/test.mp4")
            .get()
        Timber.d("video=$videoFile")
        Timber.d("audio=$audioFile")
        val mergeCommand =
            FFmpegUtils.mixAudioVideo(videoFile, audioFile, context.filesDir.path + "/test.mp4")
        MainScope().launch(Dispatchers.IO) {
            FFmpegCommand.runCmd(mergeCommand3, object : IFFmpegCallBack {
                override fun onCancel() {
                }

                override fun onComplete() {
                    Toaster.showShort("视频$title-Test合并成功")
                    Timber.d("视频$title-Test合并成功")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    Timber.d("合并错误=$errorCode, $errorMsg")
                }

                override fun onProgress(progress: Int, pts: Long) {
                    Timber.d("合并进度=$progress")
                }

                override fun onStart() {
                    Timber.d("视频$title-Test合并开始")
                }
            })
        }
    }

    private fun getCall(cId: String): MutableList<Download.Call> {
        return queuedDownloadCalls[cId] ?: error("未添加列表")
    }

    private fun addCall(cId: String, call: Download.Call) {
        val listOfCalls = queuedDownloadCalls[cId]
        if (listOfCalls == null) {
            queuedDownloadCalls[cId] = mutableListOf(call)
        } else {
            listOfCalls.add(call)
        }
    }

    private fun onRetryOrFailure(call: Download.Call, newUrl: String) {
        val newCall = downloader.newCall(
            call.request.newBuilder()
                .url(newUrl)
                .build()
        )
        newCall.enqueue(this)
        val asRequest = call.request as AsDownload.AsRequest
        val calls = queuedDownloadCalls[asRequest.cId]
        calls?.removeAll { it.request.tag == call.request.tag }
        calls?.add(newCall)
    }
}
