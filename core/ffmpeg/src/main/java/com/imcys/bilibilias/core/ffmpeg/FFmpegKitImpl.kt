package com.imcys.bilibilias.core.ffmpeg

import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.Session
import com.arthenica.ffmpegkit.StatisticsCallback
import io.github.aakira.napier.Napier
import java.util.concurrent.ExecutorService
import javax.inject.Inject

private const val TAG = "FFmpegKitImpl"

class FFmpegKitImpl @Inject constructor(private val executorService: ExecutorService) :
    IFFmpegWork {
    override fun execute(command: String) {
        val session = FFmpegKit.execute(command)
        FFmpegKit.executeWithArgumentsAsync(arrayOf(), {
            callback(it)
        }, {
            Napier.i(tag = TAG) { it.toString() }
        }, {
            Napier.i(tag = TAG) { it.toString() }
        }, executorService)
    }

    private fun callback(session: Session) {
        if (ReturnCode.isSuccess(session.returnCode)) {
            Napier.d(tag = TAG) { "合并成功" }
        } else if (ReturnCode.isCancel(session.returnCode)) {
            Napier.d(tag = TAG) { "合并失败" }
        } else {
            Napier.w("命令执行失败,当前状态 ${session.state} 错误码${session.returnCode} 调用栈${session.failStackTrace}")
        }
    }
}
