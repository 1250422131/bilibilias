package com.imcys.bilibilias.core.download.chore

import android.content.Context
import androidx.core.net.toFile
import com.imcys.bilibilias.core.common.download.DefaultConfig
import com.imcys.bilibilias.core.datastore.UserPreferences
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.downloadDir
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.ffmpeg.FFmpegUtil
import com.imcys.bilibilias.core.ffmpeg.FFmpegWorker
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class MixingInterceptor @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    private val fFmpegWorker: FFmpegWorker,
    private val userPreferences: AsPreferencesDataSource,
) : Interceptor<GroupTask> {
    override val enable = runBlocking {
        userPreferences.userData.first().autoMerge
    }

    override fun intercept(message: GroupTask, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "合并视频 $enable, $message" }
        if (!enable) return
        val m = File(context.downloadDir, "${message.video.subTitle}_mix.mp4")
        val userCommand = runBlocking { userPreferences.userData.first().command }
        val command: Array<String?> = if (DefaultConfig.defaultCommand == userCommand) {
            FFmpegUtil.mixAudioVideo(
                message.video.uri.toFile().path,
                message.audio.uri.toFile().path,
                m.path
            )
        } else {
            userCommand
                .replace("{VIDEO_PATH}", message.video.uri.toFile().path)
                .replace("{AUDIO_PATH}", message.audio.uri.toFile().path)
                .replace("{VIDEO_MERGE_PATH}", m.path)
                .split(" ")
                .toTypedArray()
        }
        Napier.d(tag = "Interceptor") { "合并命令 ${command.joinToString(" ")}" }
        fFmpegWorker.runCommand(
            command,
            onStart = { Napier.d(tag = "FFmpegWorker") { "合并开始" } },
            onProgress = {
                Napier.d(tag = "FFmpegWorker") { "合并进度" + it.toString() }
            },
            onError = { Napier.d(tag = "FFmpegWorker") { "合并错误 $it" } },
            onCancel = { Napier.d(tag = "FFmpegWorker") { "合并取消" } },
            onComplete = {
                chain.proceed(m.path)
                Napier.d(tag = "FFmpegWorker") { "合并完成" }
            }
        )
    }
}
