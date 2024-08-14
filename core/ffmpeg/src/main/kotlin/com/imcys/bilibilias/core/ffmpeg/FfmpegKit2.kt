package com.imcys.bilibilias.core.ffmpeg

import android.content.Context
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.Level
import com.arthenica.ffmpegkit.ReturnCode
import com.imcys.bilibilias.core.ffmpeg.util.convertSAFProtocol
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import javax.inject.Inject

private const val TAG = "FfmpegKit2"

@Deprecated("")
class FfmpegKit2 @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    init {
        FFmpegKitConfig.enableLogCallback {
            Napier.d { it.toString() }
        }
        FFmpegKitConfig.enableMediaInformationSessionCompleteCallback {
            Napier.d { it.toString() }
        }
        FFmpegKitConfig.enableStatisticsCallback {
            Napier.d { it.toString() }
        }
        FFmpegKitConfig.enableFFmpegSessionCompleteCallback {
            Napier.d { it.toString() }
        }
        FFmpegKitConfig.setLogLevel(Level.AV_LOG_DEBUG)
    }

    fun execute(command: Array<String>, videoUri: String, audioUri: String, outputUri: String) {
        for ((i, c) in command.withIndex()) {
            when (c) {
                videoUri, audioUri -> {
                    val text = context.convertSAFProtocol(command[i], true)
                    command[i] = text
                }

                outputUri -> {
                    val text = context.convertSAFProtocol(command[i], false)
                    command[i] = text
                }
            }
        }
        val session = FFmpegKit.executeWithArguments(command)
        Napier.d(tag = TAG) { "合并命令 " + session.command }
        if (ReturnCode.isSuccess(session.returnCode)) {
            Napier.d(tag = TAG) { "合并成功" }
        } else if (ReturnCode.isCancel(session.returnCode)) {
            Napier.d(tag = TAG) { "合并失败" }
        } else {
            Napier.w("命令执行失败, 当前状态 ${session.state} 错误码 ${session.returnCode} 调用栈 ${session.failStackTrace}")
        }
    }
}
