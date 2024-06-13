package com.imcys.bilibilias.core.download.chore

import android.content.Context
import androidx.core.net.toFile
import com.anggrayudi.storage.file.CreateMode
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.makeFile
import com.anggrayudi.storage.media.FileDescription
import com.anggrayudi.storage.media.MediaStoreCompat
import com.imcys.bilibilias.core.data.toast.AsToastState
import com.imcys.bilibilias.core.data.toast.AsToastType
import com.imcys.bilibilias.core.data.toast.ToastMachine
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.ffmpeg.FFmpegUtil
import com.imcys.bilibilias.core.ffmpeg.IFFmpegWork
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MixingInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: AsPreferencesDataSource,
    private val ffmpegWork: IFFmpegWork,
    private val toastMachine: ToastMachine,
) : Interceptor<GroupTask> {
    override val enable = true

    // val command = arrayOf(
    //            "-y",
    //            "-i",
    //            vFile.toFile().path,
    //            "-i",
    //            aFile.toFile().path,
    //            "-vcodec", "copy", "-acodec", "copy",
    //            "$contentUri",
    //        )
    override fun intercept(message: GroupTask, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "合并视频 $enable, $message" }
        if (!enable) return
        runBlocking {
            toastMachine.show("开始合并视频: ${message.video.subTitle}")
            val path = userPreferences.userData.first().storagePath
            Napier.d { "指定路径 $path" }
            if (path != null) {
                指定写入路径(message)
            } else {
                没有指定写入路径(message)
            }
        }
    }

    private suspend fun 指定写入路径(message: GroupTask) {
        val grantedPaths = DocumentFileCompat.getAccessibleAbsolutePaths(context)
        val path = grantedPaths.values.firstOrNull()?.firstOrNull() ?: return
        val folder = DocumentFileCompat.fromFullPath(context, path, requiresWriteAccess = true)
        val file = folder?.makeFile(context, "${message.video.subTitle}.mp4", MimeType.VIDEO)
        val command = FFmpegUtil.mixAudioVideo2(
            message.video.uri.toFile().path,
            message.audio.uri.toFile().path,
            file?.uri.toString()
        )
        ffmpegWork.execute(command, {
            toastMachine.show(
                AsToastState(
                    "合并成功: ${message.video.subTitle}",
                    AsToastType.Success
                )
            )
        }, {
            toastMachine.show(
                AsToastState(
                    "合并失败: ${message.video.subTitle}",
                    AsToastType.Error
                )
            )
        })
    }

    private suspend fun 没有指定写入路径(message: GroupTask) {
        val mediaFile = MediaStoreCompat.createVideo(
            context,
            FileDescription("${message.video.subTitle}.mp4", "biliAs", MimeType.VIDEO),
            mode = CreateMode.REPLACE
        )
        val command = FFmpegUtil.mixAudioVideo2(
            message.video.uri.toFile().path,
            message.audio.uri.toFile().path,
            mediaFile?.uri.toString()
        )
        ffmpegWork.execute(command, {
            toastMachine.show(
                AsToastState(
                    "合并成功: ${message.video.subTitle}",
                    AsToastType.Success
                )
            )
        }, {
            toastMachine.show(
                AsToastState(
                    "合并失败: ${message.video.subTitle}",
                    AsToastType.Error
                )
            )
        })
    }
}
