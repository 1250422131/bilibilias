package com.imcys.network.download

import android.content.Context
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.CommandParams
import com.coder.ffmpeg.utils.FFmpegUtils
import com.imcys.bilibilias.okdownloader.Download
import com.imcys.common.di.AppCoroutineScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FFmpegMerge @Inject constructor(
    @ApplicationContext private val context: Context,
    @AppCoroutineScope private val scope: CoroutineScope,
) : IFFmpegCallBack {
    private val workerQueue = Channel<MergeData>(Channel.UNLIMITED)

    init {
        scope.launch {
            while (isActive) {
                while (!workerQueue.isEmpty) {
                    for (data in workerQueue) {
                        merge(data.videoFile, data.audioFile, data.targetFile)
                    }
                }
            }
        }
    }

    fun submit(data: MergeData) {
        workerQueue.trySend(data)
    }

    private fun tryMerge(call: Download.Call) {
        val asRequest = call.request
//        val asRequestList = _completedDownload.filter { it.cId == asRequest.cId }
//        if (asRequestList.size < 2) return
//        val videoFile =
//            asRequestList.find { it.tag == VIDEO_M4S }?.destFile()?.absolutePath
//                ?: error("合并失败")
//        val audioFile =
//            asRequestList.find { it.tag == AUDIO_M4S }?.destFile()?.absolutePath
//                ?: error("合并失败")
//        merge(videoFile, audioFile, "")
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
        val mergeCommand =
            FFmpegUtils.mixAudioVideo(videoFile, audioFile, context.filesDir.path + "/test.mp4")
        MainScope().launch(Dispatchers.IO) {
            FFmpegCommand.runCmd(mergeCommand3, this@FFmpegMerge)
        }
    }

    override fun onStart() {
        Timber.d("视频合并开始")
    }

    override fun onCancel() {
        Timber.d("视频合并取消")
    }

    override fun onComplete() {
        Timber.d("视频合并完成")
    }

    override fun onError(errorCode: Int, errorMsg: String?) {
        Timber.d("视频合并错误=$errorCode,$errorMsg")
    }

    override fun onProgress(progress: Int, pts: Long) {
        Timber.d("视频合并进度$progress,$pts")
    }
}

class MergeData(
    val videoFile: String,
    val audioFile: String,
    val targetFile: String,
    val cID: String,
    val path: String,
    val title: String
)