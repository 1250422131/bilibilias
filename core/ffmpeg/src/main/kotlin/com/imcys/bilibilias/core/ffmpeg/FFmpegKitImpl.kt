package com.imcys.bilibilias.core.ffmpeg

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.Level
import com.arthenica.ffmpegkit.ReturnCode
import com.arthenica.ffmpegkit.Session
import com.imcys.bilibilias.core.ffmpeg.util.convertSAFProtocol
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import java.net.URLDecoder
import javax.inject.Inject

private const val TAG = "FFmpegKitImpl"

class FFmpegKitImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : IFFmpegWork {
    init {
        FFmpegKitConfig.setLogLevel(Level.AV_LOG_DEBUG)
    }

    override fun execute(
        template: String,
        outputUri: String,
        vararg contentSourcesUri: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        Napier.d {
            "命令模板 $template, out:${UrlEncoderUtil.decode(outputUri)}, input:${
                contentSourcesUri.joinToString { UrlEncoderUtil.decode(it) }
            }"
        }
        val realCommand = generateCommand(template, outputUri, arrayOf(*contentSourcesUri))
        FFmpegKit.executeWithArgumentsAsync(
            realCommand,
            {
                completeCallback(it, onSuccess, onFailure)
            },
        )
    }

    private fun completeCallback(session: Session, onSuccess: () -> Unit, onFailure: () -> Unit) {
        Napier.d(tag = TAG) { "合并命令 " + session.command }
        if (ReturnCode.isSuccess(session.returnCode)) {
            onSuccess()
        } else if (ReturnCode.isCancel(session.returnCode)) {
            onFailure()
        } else {
            Napier.w("命令执行失败, 当前状态 ${session.state} 错误码 ${session.returnCode} 调用栈 ${session.failStackTrace}")
        }
        Napier.i(tag = TAG) { session.toString() }
    }

    private fun String.countSubstrings(): Int {
        return """{input}""".toRegex().findAll(this).count()
    }

    @VisibleForTesting
    internal fun generateCommand(
        template: String,
        outputUri: String,
        contentSourcesUri: Array<String>,
    ): Array<String> {
        // val countSubstrings = template.countSubstrings()
        // val size = contentSourcesUri.size
        // check(countSubstrings == size) { "占位符与输入源数量不同, template is $countSubstrings, sources is $size" }
        val strings = contentSourcesUri.map { context.convertSAFProtocol(it, true) }
        val outUri = context.convertSAFProtocol(outputUri, false)
        var index = 0
        return template.split(" ")
            .map {
                when (it) {
                    "{input}" -> strings[index++]
                    "{output}" -> outUri
                    else -> it
                }
            }.toTypedArray()
    }
}
