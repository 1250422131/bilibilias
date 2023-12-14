package com.imcys.network.download

import androidx.collection.CircularArray
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.CommandParams
import com.imcys.common.di.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FFmpegMerge @Inject constructor(
    @AppCoroutineScope private val scope: CoroutineScope
) : IFFmpegCallBack {
    private val readyWorker = CircularArray<MergeData>()
    private val runningWorker = CircularArray<MergeData>()

    private var listener: Listener? = null

    fun execute(data: MergeData) {
        readyWorker.addLast(data)
        if (runningWorker.isEmpty()) {
            promote()
        }
    }

    private fun promote() {
        if (readyWorker.isEmpty()) return
        val data: MergeData = readyWorker.popFirst()
        runningWorker.addLast(data)
        merge(data.videoFile.absolutePath, data.audioFile.absolutePath, data.mixFile)
    }

    /**
     * ffmpeg -i sample_video_ffmpeg.mp4 -vf ass=output_subtitle.ass output_ass.mp4
     */
    private fun buildJMixAssCommand(
        videoFile: String,
        assFile: String,
        muxFile: String
    ): Array<String?> {
        return CommandParams()
            .append("-i")
            .append(videoFile)
            .append("-vf")
            .append("ass=$assFile")
            .append(muxFile)
            .get()
    }

    private fun buildMixCommand(
        videoFile: String,
        audioFile: String,
        muxFile: String
    ): Array<String?> {
        return CommandParams()
            .append("-i")
            .append(videoFile)
            .append("-i")
            .append(audioFile)
            .append("-c:v")
            .append("copy")
            .append("-c:a")
            .append("copy")
            .append("$muxFile.mp4")
            .get()
    }

    private fun merge(
        videoFile: String,
        audioFile: String,
        mixFile: String,
    ) {
        val command = buildMixCommand(videoFile, audioFile, mixFile)
        scope.launch {
            FFmpegCommand.runCmd(command, this@FFmpegMerge)
        }
    }

    override fun onStart() {
        Timber.d("视频合并开始")
        val data: MergeData = runningWorker.first
        listener?.onStart(data.realName)
    }

    override fun onCancel() {
        Timber.d("视频合并取消")
        promote()
    }

    override fun onComplete() {
        Timber.d("视频合并完成")
        val data: MergeData = runningWorker.popFirst()
        listener?.onSuccess(data.mixFile, data.realName, readyWorker.size())
        if (readyWorker.isEmpty()) {
            listener?.onComplete()
        }
        promote()
    }

    override fun onError(errorCode: Int, errorMsg: String?) {
        Timber.d("视频合并错误=$errorCode,$errorMsg")
        val data: MergeData = runningWorker.popFirst()
        listener?.onError(errorCode, errorMsg, data.mixFile, data.realName)
        promote()
    }

    override fun onProgress(progress: Int, pts: Long) {
        Timber.d("视频合并进度$progress,$pts")
        listener?.onProgress(progress, pts)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onProgress(progress: Int, pts: Long)
        fun onError(errorCode: Int, errorMsg: String?, mixFile: String, realName: String)
        fun onComplete()

        /**
         * @param title 标题
         */
        fun onStart(title: String)
        fun onSuccess(fullPath: String, realName: String, tasks: Int)
    }
}

data class MergeData(
    val videoFile: File,
    val audioFile: File,
    val mixFile: String,
    val realName: String
)
