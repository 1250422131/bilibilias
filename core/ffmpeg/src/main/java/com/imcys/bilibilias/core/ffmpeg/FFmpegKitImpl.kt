package com.imcys.bilibilias.core.ffmpeg

import android.content.Context
import androidx.core.net.toUri
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.Session
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import javax.inject.Inject

private const val TAG = "FFmpegKitImpl"

class FFmpegKitImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : IFFmpegWork {
    override fun execute(
        inputVideo: String,
        inputAudio: String,
        output: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val readVideo = FFmpegKitConfig.getSafParameterForRead(context, inputVideo.toUri())
        val readAudio = FFmpegKitConfig.getSafParameterForRead(context, inputAudio.toUri())
        val outputVideo = FFmpegKitConfig.getSafParameterForWrite(context, output.toUri())
        val command = buildCommandParams {
            append("-y")

            append("-i")
            append("$readVideo")

            append("-i")
            append("$readAudio")

            append("-vcodec")
            append("copy")
            append("-acodec")
            append("copy")

            append("$outputVideo")
        }
        val session = FFmpegKit.executeWithArguments(command)
        callback(session, onSuccess, onFailure)
    }

    override fun execute(command: Array<String>, onSuccess: () -> Unit, onFailure: () -> Unit) {
        command[command.lastIndex] =
            FFmpegKitConfig.getSafParameterForWrite(context, command[command.lastIndex].toUri())
        val session = FFmpegKit.executeWithArguments(command)
        callback(session, onSuccess, onFailure)
    }

    private fun callback(session: Session, onSuccess: () -> Unit, onFailure: () -> Unit) {
        Napier.d(tag = TAG) { "合并命令 " + session.command }
        if (ReturnCode.isSuccess(session.returnCode)) {
            Napier.d(tag = TAG) { "合并成功" }
            onSuccess()
        } else if (ReturnCode.isCancel(session.returnCode)) {
            Napier.d(tag = TAG) { "合并失败" }
            onFailure()
        } else {
            Napier.w("命令执行失败, 当前状态 ${session.state} 错误码 ${session.returnCode} 调用栈 ${session.failStackTrace}")
        }
    }
}
