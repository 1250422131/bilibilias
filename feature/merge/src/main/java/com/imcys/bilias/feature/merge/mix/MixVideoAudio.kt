package com.imcys.bilias.feature.merge.mix

import androidx.collection.CircularArray
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.CommandParams
import com.imcys.common.di.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MixVideoAudio @Inject constructor(
    @AppCoroutineScope private val scope: CoroutineScope
) : IFFmpegCallBack {
    private val readyWorker = CircularArray<MixVideoAndAudio>()
    private val runningWorker = CircularArray<MixVideoAndAudio>(1)

    private fun promoteAndExecute() {
        if (readyWorker.isEmpty()) return
        val data: MixVideoAndAudio = readyWorker.popFirst()
        runningWorker.addLast(data)
        merge(data.video.absolutePath, data.audio.absolutePath, data.mix.name)
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
            FFmpegCommand.runCmd(command, this@MixVideoAudio)
        }
    }

    override fun onStart() {
        Timber.d("视频合并开始")
        val data: MixVideoAndAudio = runningWorker.first
    }

    override fun onCancel() {
        Timber.d("视频合并取消")
        promoteAndExecute()
    }

    override fun onComplete() {
        Timber.d("视频合并完成")
        promoteAndExecute()
    }

    override fun onError(errorCode: Int, errorMsg: String?) {
        Timber.d("视频合并错误=$errorCode,$errorMsg")
        val data: MixVideoAndAudio = runningWorker.popFirst()
        promoteAndExecute()
    }

    override fun onProgress(progress: Int, pts: Long) {
        Timber.d("视频合并进度$progress,$pts")
    }
}
