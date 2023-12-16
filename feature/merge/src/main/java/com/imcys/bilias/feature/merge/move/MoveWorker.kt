package com.imcys.bilias.feature.merge.move

import android.content.Context
import android.media.MediaFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.contentValuesOf
import com.imcys.common.di.AppCoroutineScope
import com.imcys.common.utils.FileUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

val Context.mixDir: File
    get() = File("${filesDir.absolutePath}${File.separator}mix").apply {
        mkdirs()
    }

class MoveWorker @Inject constructor(
    @AppCoroutineScope private val scope: CoroutineScope,
    @ApplicationContext private val context: Context,
) : IMoveWorker {
    private val moveWorker = Channel<Pair<String, String>>(Channel.Factory.UNLIMITED)
    private var job: Job? = null
    override fun enqueue(path: String, viewTitle: String) {
        moveWorker.trySend(path to viewTitle)
    }

    override fun execute(callback: (String) -> Unit) {
        job = scope.launch {
            while (!moveWorker.isEmpty) {
                val (path, title) = moveWorker.receive()
                moveToMediaStore(path, title)
                callback(path)
            }
        }
//        delete("")
    }

    override fun delete(path: String) {
        dev.utils.common.FileUtils.deleteDir(context.mixDir)
    }

    private fun moveToMediaStore(fullPath: String, title: String) {
        scope.launch {
            getUri(title)?.let { uri ->
                try {
                    val sink = context.contentResolver.openOutputStream(uri)
                    if (sink != null) {
                        val source = File("$fullPath.mp4").inputStream()
                        FileUtils.copy(source, sink)
                        Timber.d("复制完成$fullPath")
                    }
                } catch (e: Exception) {
                    Timber.d(e)
                }
            }
        }
    }

    private fun getUri(title: String): Uri? {
        val values = contentValuesOf(
            MediaStore.Video.Media.MIME_TYPE to MediaFormat.MIMETYPE_VIDEO_MPEG4,
            MediaStore.Video.Media.DISPLAY_NAME to "$title.mp4",
            MediaStore.Video.Media.RELATIVE_PATH to "${Environment.DIRECTORY_MOVIES}/biliAS",
        )
        val contentResolver = context.contentResolver
        return contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
    }
}