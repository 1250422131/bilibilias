package com.imcys.bilibilias.core.ffmpeg

import android.content.Context
import androidx.core.net.toUri
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.Session
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import java.util.concurrent.ExecutorService
import javax.inject.Inject

private const val TAG = "FFmpegKitImpl"

class FFmpegKitImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IFFmpegWork {
    override fun execute(command: Array<String>) {
        command[command.lastIndex] =
            FFmpegKitConfig.getSafParameterForWrite(context, command[command.lastIndex].toUri())
        val session = FFmpegKit.executeWithArguments(command)
        callback(session)
    }

    private fun callback(session: Session) {
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
