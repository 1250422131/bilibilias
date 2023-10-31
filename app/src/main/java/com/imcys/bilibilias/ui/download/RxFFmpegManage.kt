package com.imcys.bilibilias.ui.download

import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegSession
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.SessionState
import com.imcys.common.utils.updatePhotoMedias
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.tonyodev.fetch2.Download
import io.microshow.rxffmpeg.RxFFmpegCommandList
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import org.jetbrains.annotations.ApiStatus
import timber.log.Timber
import java.io.File

private const val TAG = "RxFFmpegManage"

class RxFFmpegManage constructor(
    private val video: Download,
    private val audio: Download,
    private val filename: String
) : RxFFmpegSubscriber() {
    private val tag = Timber.tag("RxFFmpegManage")
    private val rxFFmpeg = RxFFmpegInvoke.getInstance()

    fun buildFFmpegCommand(): Array<out String> {
        return RxFFmpegCommandList()
            .append("-i")
            .append(video.file)
            .append("-i")
            .append(audio.file)
            .append("-c")
            .append("copy")
            .append(filename)
            .build()
    }

    fun runCommand(command: Array<String>) {
        rxFFmpeg.runCommandRxJava(command)
    }

    fun runBuiltCommand() {
        rxFFmpeg.runCommandRxJava(buildFFmpegCommand())
    }

    /**
     * "-i file1.mp4 -c:v mpeg4 file2.mp4"
     * api https://github.com/tanersener/ffmpeg-kit/tree/main/android#31-android-api
     */
    @ApiStatus.Experimental
    fun runCommand(command: String) {
        val session = FFmpegKit.execute(command)

        if (ReturnCode.isSuccess(session.returnCode)) {
            print("执行成功")
        } else if (ReturnCode.isCancel(session.returnCode)) {
            print("执行失败")
        } else {
            Timber.tag(
                TAG
            ).d("Command failed with state %s and rc %s.%s", session.state, session.returnCode, session.failStackTrace)
        }
    }

    @ApiStatus.Experimental
    fun currentSessionState(session: FFmpegSession) {
        when (session.state) {
            SessionState.CREATED -> TODO()
            SessionState.RUNNING -> TODO()
            SessionState.FAILED -> TODO()
            SessionState.COMPLETED -> TODO()
            null -> {}
        }
    }

    override fun onFinish() {
        FileUtils.deleteFiles(video.file, audio.file)
        com.imcys.common.utils.updatePhotoMedias(File(filename))
    }

    override fun onProgress(progress: Int, progressTime: Long) {
    }

    override fun onError(message: String?) {
    }

    override fun onCancel() {
    }
}
