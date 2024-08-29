package com.imcys.bilibilias.core.download.chore

import android.content.Context
import androidx.core.net.toUri
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.CreateFailedException
import com.imcys.bilibilias.core.download.media.MimeType
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
private const val COMMAND = "-y -i {input} -i {input} -vcodec copy -acodec copy {output}"

@Suppress("ktlint:standard:function-naming")
class MixingInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope,
    private val userPreferences: AsPreferencesDataSource,
    private val ffmpegWork: IFFmpegWork,
    private val errorMonitor: ErrorMonitor,
) : Interceptor<List<DownloadTaskEntity>> {
    override val enable = true

    override fun intercept(message: List<DownloadTaskEntity>, chain: Interceptor.Chain) {
        Napier.d(tag = "Interceptor") { "合并视频 $enable, $message" }
        if (!enable) return
        scope.launch {
            val path = userPreferences.userData.first().storageFolder

            Napier.d { "指定路径 $path" }
            val subTitle = message.first().subTitle
            val out = if (path != null) {
                val savePath = DocumentFileCompat.fromTreeUri(context, path.toUri())!!
                savePath.findFile("$subTitle.mp4")?.uri
                    ?: savePath.createFile(MimeType.VIDEO, "$subTitle.mp4")?.uri
                    ?: throw CreateFailedException("创建文件失败")
            } else {
                MediaStoreUtils.createVideoUri(
                    subTitle + "_mix",
                    MediaStoreUtils.MIME_TYPE_VIDEO_MP4,
                    RELATIVE_PATH,
                ) ?: throw CreateFailedException("创建文件失败")
            }
            ffmpegWork.execute(
                template = COMMAND,
                out.toString(),
                contentSourcesUri = message.map {
                    it.uri.toString()
                }.toTypedArray(),
                {},
                {},
            )
        }
    }
}
