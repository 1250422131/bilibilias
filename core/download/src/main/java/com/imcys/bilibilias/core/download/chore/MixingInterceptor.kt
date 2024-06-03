package com.imcys.bilibilias.core.download.chore

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.contentValuesOf
import androidx.core.net.toFile
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.ffmpeg.IFFmpegWork
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MixingInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: AsPreferencesDataSource,
    private val ifFmpegWork: IFFmpegWork,
) : Interceptor<GroupTask> {
    override val enable = runBlocking {
        userPreferences.userData.first().autoMerge
    }

    override fun intercept(message: GroupTask, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "合并视频 $enable, $message" }
        if (!enable) return
        val resolver = context.contentResolver
        val date = System.currentTimeMillis() / 1000
        val contentValues = contentValuesOf(
            MediaStore.Video.Media.DISPLAY_NAME to "${message.video.subTitle}.mp4",
            MediaStore.Video.Media.RELATIVE_PATH to "${Environment.DIRECTORY_MOVIES}/biliAs",
            MediaStore.Video.Media.IS_PENDING to 1,
            MediaStore.Video.Media.MIME_TYPE to "video/mp4",
            MediaStore.Video.Media.DATE_ADDED to date,
            MediaStore.Video.Media.DATE_MODIFIED to date,
        )
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
        val contentUri = resolver.insert(collection, contentValues)
        val vFile = message.video.uri
        val aFile = message.audio.uri
        val command = arrayOf(
            "-y",
            "-i",
            vFile.toFile().path,
            "-i",
            aFile.toFile().path,
            "-vcodec", "copy", "-acodec", "copy",
            "$contentUri",
        )
        ifFmpegWork.execute(command)

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        if (contentUri != null) {
            resolver.update(contentUri, contentValues, null, null)
        }
    }
}
