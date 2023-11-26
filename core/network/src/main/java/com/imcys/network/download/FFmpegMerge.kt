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
    @AppCoroutineScope private val scope: CoroutineScope,
) : IFFmpegCallBack {
    private val workerQueue = CircularArray<MergeData>()
    private var listener: Listener? = null

    /**
     * 当前合并音视频的信息
     */
    private var currentWork: MergeData? = null

    /**
     * 是否有任务在运行中
     */
    private var running = false
    fun submit(data: MergeData) {
        workerQueue.addLast(data)
        if (!running) {
            execute()
        }
    }

    private fun execute() {
        if (workerQueue.isEmpty) return
        running = true
        val data = workerQueue.popFirst()
        currentWork = data
        merge(data.videoFile.absolutePath, data.audioFile.absolutePath, data.muxFile)
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
        listener?.onStart(workerQueue.size())
    }

    override fun onCancel() {
        Timber.d("视频合并取消")
        execute()
    }

    override fun onComplete() {
        Timber.d("视频合并完成")
        listener?.onSuccess(currentWork?.muxFile, currentWork?.realName)
        if (workerQueue.isEmpty) {
            listener?.onComplete()
            running = false
            currentWork = null
        }
        execute()
    }

    override fun onError(errorCode: Int, errorMsg: String?) {
        Timber.d("视频合并错误=$errorCode,$errorMsg")
        listener?.onError(errorCode, errorMsg)
        execute()
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
        fun onError(errorCode: Int, errorMsg: String?)
        fun onComplete()

        /**
         * @param size 剩余任务数
         */
        fun onStart(size: Int)
        fun onSuccess(fullPath: String?, realName: String?)
    }
}

data class MergeData(
    val videoFile: File,
    val audioFile: File,
    val muxFile: String,
    val realName: String
)
