package com.imcys.bilibilias.core.download.chore

import android.content.Context
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.data.util.MessageType
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.media.MimeType
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.ffmpeg.FFmpegUtil
import com.imcys.bilibilias.core.ffmpeg.IFFmpegWork
import com.imcys.bilibilias.core.network.di.ApplicationScope
import com.lazygeniouz.dfc.file.DocumentFileCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.utils.app.MediaStoreUtils
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RELATIVE_PATH = "Movies/biliAs/"

@Suppress("ktlint:standard:function-naming")
class MixingInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope,
    private val userPreferences: AsPreferencesDataSource,
    private val ffmpegWork: IFFmpegWork,
    private val errorMonitor: ErrorMonitor,
) : Interceptor<List<DownloadTaskEntity>> {
    override val enable = true

    override fun intercept(message:  List<DownloadTaskEntity>, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "合并视频 $enable, $message" }
        if (!enable) return
        scope.launch {
            val path = userPreferences.userData.first().storageFolder


            Napier.d { "指定路径 $path" }
            if (path != null) {
                指定写入路径(message, path)
            } else {
                没有指定写入路径(message)
            }
        }
    }

    @Suppress("standard:function-naming")
    private fun 指定写入路径(message: GroupTask, path: String) {
        val savePath = DocumentFileCompat.fromTreeUri(context, path.toUri())!!
        val outputFile = savePath.findFile("${message.video.subTitle}.mp4")
            ?: savePath.createFile(MimeType.VIDEO, "${message.video.subTitle}.mp4")
            ?: throw Exception("创建文件失败")

        ffmpegWork.execute(
            message.video.uri.toString(),
            message.audio.uri.toString(),
            outputFile.uri.toString(),
            {
                errorMonitor.addShortErrorMessage(
                    "合并成功: ${message.video.subTitle}",
                    MessageType.Success,
                )
            },
            {
                errorMonitor.addShortErrorMessage(
                    "合并失败: ${message.video.subTitle}",
                    MessageType.Error,
                )
            },
        )
    }

    private fun 没有指定写入路径(message: GroupTask) {
        val contentUri = MediaStoreUtils.createVideoUri(
            message.video.subTitle + "_mix",
            MediaStoreUtils.MIME_TYPE_VIDEO_MP4,
            RELATIVE_PATH,
        ) ?: throw Exception("创建文件失败")

        val command = FFmpegUtil.mixAudioVideo(
            message.video.uri.toFile().path,
            message.audio.uri.toFile().path,
            contentUri.toString(),
        )
        Napier.d { "合并命令 ${command.joinToString(" ")}" }
        ffmpegWork.execute(
            command,
            {
                errorMonitor.addShortErrorMessage(
                    "合并成功: ${message.video.subTitle}",
                    MessageType.Success,
                )
            },
            {
                errorMonitor.addShortErrorMessage(
                    "合并失败: ${message.video.subTitle}",
                    MessageType.Error,
                )
            },
        )
    }
}
