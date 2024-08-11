package com.imcys.bilibilias.core.download.chore

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.data.util.MessageType
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.media.MimeType
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.ffmpeg.FFmpegUtil
import com.imcys.bilibilias.core.ffmpeg.IFFmpegWork
import com.imcys.bilibilias.core.network.di.ApplicationScope
import com.lazygeniouz.dfc.file.DocumentFileCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.utils.app.ContentResolverUtils
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
) : Interceptor<GroupTask> {
    override val enable = true

    override fun intercept(message: GroupTask, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "合并视频 $enable, $message" }
        if (!enable) return
        scope.launch {
            errorMonitor.addShortErrorMessage("开始合并视频: ${message.video.subTitle}")
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
    private suspend fun 指定写入路径(message: GroupTask, path: String) {
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

    private suspend fun 没有指定写入路径(message: GroupTask) {
        val uri = getExistingVideoUriOrNull(message)
        val contentUri = if (uri == null) {
            MediaStoreUtils.createVideoUri(
                message.video.subTitle,
                MimeType.VIDEO,
                RELATIVE_PATH,
            ) ?: throw Exception("创建文件失败")
        } else {
            uri
        }
        val command = FFmpegUtil.mixAudioVideo2(
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

    private fun getExistingVideoUriOrNull(message: GroupTask): Uri? {
        ContentResolverUtils.query(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            },
            arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.RELATIVE_PATH,
            ),
            "${MediaStore.Video.Media.DISPLAY_NAME} = ? AND ${MediaStore.Video.Media.RELATIVE_PATH} = ?",
            arrayOf("${message.video.subTitle}.mp4", RELATIVE_PATH),
            null,
        )?.use { cursor ->
            Napier.d { "查询已合并文件是否存在" }
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val pathColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val path = cursor.getString(pathColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id,
                )
                Napier.d { "查询结果: $id-$name-$path-$contentUri" }
                return contentUri
            }
        }
        return null
    }
}
