package com.imcys.bilibilias.core.download.chore

import android.content.Context
import androidx.core.net.toFile
import com.imcys.bilibilias.core.download.downloadDir
import com.imcys.bilibilias.core.ffmpeg.FFmpegUtil
import com.imcys.bilibilias.core.ffmpeg.FFmpegWorker
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.runBlocking
import java.io.File

class MixingInterceptor @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    private val fFmpegWorker: FFmpegWorker,
) : Interceptor {
    override val enable: Boolean = true
    override fun intercept(chain: Interceptor.Chain) {
        if (!enable) return
        val request = chain.request()
        val command = FFmpegUtil.mixAudioVideo(
            request.video.uri.toFile().path,
            request.audio.uri.toFile().path,
            File(context.downloadDir, "DDDDD.mp4").path
        )
        runBlocking {
            fFmpegWorker.runCommand(
                command,
                onStart = { Napier.d(tag = "FFmpegWorker") { "合并开始" } },
                onProgress = {
                    Napier.d(tag = "FFmpegWorker") { "合并进度" + it.toString() }
                },
                onError = { Napier.d(tag = "FFmpegWorker") { "合并错误 $it" } },
                onCancel = { Napier.d(tag = "FFmpegWorker") { "合并取消" } },
                onComplete = { Napier.d(tag = "FFmpegWorker") { "合并完成" } }
            )
            chain.proceed(chain.request())
        }
    }
}
