package com.imcys.bilibilias.core.download

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.domain.GetVideoInfoMetaWithPlayUrlUseCase
import com.imcys.bilibilias.core.download.DefaultConfig.DEFAULT_NAMING_RULE
import com.imcys.bilibilias.core.download.media.MimeType
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Sources
import com.imcys.bilibilias.core.model.video.ViewIds
import com.imcys.bilibilias.core.network.di.ApplicationScope
import com.lazygeniouz.dfc.file.DocumentFileCompat
import com.liulishuo.okdownload.core.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.DevUtils
import dev.utils.app.ContentResolverUtils
import dev.utils.app.UriUtils
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.thauvin.erik.urlencoder.UrlEncoderUtil
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.maxBy

val Context.downloadDir
    get() =
        File(getExternalFilesDir(null)!!.parent, "download").apply {
            mkdirs()
        }
private const val TAG = "DownloadManager"

@Suppress("LongParameterList")
@Singleton
class DownloadManager @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val downloadTaskDao: DownloadTaskDao,
    private val listener: AsDownloadListener,
    private val asPreferencesDataSource: AsPreferencesDataSource,
    private val getVideoInfoMetaWithPlayUrlUseCase: GetVideoInfoMetaWithPlayUrlUseCase,
) {
    init {
        if (BuildConfig.DEBUG) {
            Util.enableConsoleLog()
        }
    }

    fun download(ids: List<Bvid>) {
        applicationScope.launch {
            Napier.d { ids.joinToString() }
            for (id in ids) {
                getVideoInfoMetaWithPlayUrlUseCase(id).first().forEach {
                    val pageTitle = it.partTitle
                    val title = it.title
                    val downloadVideoUrl =
                        getDownloadVideoUrlStrategy(it.video, null, null)
                    val downloadAudioUrl = getDownloadAudioUrlStrategy(it.audio)
                    val viewIds = ViewIds(it.aid, it.bvid, it.cid)

                    createTask(downloadVideoUrl, viewIds, pageTitle, title, FileType.VIDEO)
                    createTask(downloadAudioUrl, viewIds, pageTitle, title, FileType.AUDIO)
                }
            }
        }
    }

    fun download(request: DownloadRequest) {
        applicationScope.launch {
            Napier.d { "下载任务详情: $request" }
            val videoInfo = getVideoInfoMetaWithPlayUrlUseCase(request.bvid).first()
            val info = videoInfo.single { it.cid == request.cid }
            val pageTitle = info.partTitle
            val title = info.title
            val downloadVideoUrl =
                getDownloadVideoUrlStrategy(info.video, request.codecid, request.quality)
            val downloadAudioUrl = getDownloadAudioUrlStrategy(info.audio)
            val viewIds = ViewIds(info.aid, info.bvid, info.cid)
            when (request.taskType) {
                TaskType.ALL -> {
                    createTask(downloadVideoUrl, viewIds, pageTitle, title, FileType.VIDEO)
                    createTask(downloadAudioUrl, viewIds, pageTitle, title, FileType.AUDIO)
                }

                TaskType.VIDEO -> createTask(
                    downloadVideoUrl,
                    viewIds,
                    pageTitle,
                    title,
                    FileType.VIDEO,
                )

                TaskType.AUDIO -> createTask(
                    downloadAudioUrl,
                    viewIds,
                    pageTitle,
                    title,
                    FileType.AUDIO,
                )
            }
        }
    }

    private fun getDownloadVideoUrlStrategy(
        sources: List<Sources>,
        codecId: Int?,
        quality: Int?,
    ): String {
        val map = sources.groupBy { it.id }
        val default: () -> String = { map.maxBy { it.key }.value.maxBy { it.codecid }.baseUrl }
        if (codecId == null && quality == null) {
            return default()
        }
        return map[quality]?.singleOrNull { it.codecid == codecId }?.baseUrl
            ?: default()
    }

    private fun getDownloadAudioUrlStrategy(sources: List<Sources>) =
        sources.maxBy { it.id }.baseUrl

    private suspend fun createTask(
        url: String,
        ids: ViewIds,
        title: String,
        subTitle: String,
        type: FileType,
    ): Unit = withContext(ioDispatcher) {
        val mimeType = when (type) {
            FileType.VIDEO -> MimeType.VIDEO
            FileType.AUDIO -> MimeType.AUDIO
        }
        val path = getUserDownloadFolderPath()
        val uri = if (path == null) {
            File("").nameWithoutExtension
            File(context.downloadDir, System.currentTimeMillis().toString() + type.extension).toUri()
        } else {
            val (dir, file) = fileNameTemplate(ids, title, subTitle)
            val tree = DocumentFileCompat.fromTreeUri(context, path.toUri())!!
            tree.createDirWithFile(mimeType, dir, file)
        }
        AsDownloadTask(ids, title, subTitle, type, url, uri).also(listener::add)
    }

    private suspend fun getUserDownloadFolderPath(): String? {
        val userData = asPreferencesDataSource.userData.first()
        val path = userData.storageFolder
        Napier.d { "存储路径: ${path?.let { UrlEncoderUtil.decode(it) }}" }
        return path
    }

    private fun DocumentFileCompat.createDirWithFile(
        mimeType: String,
        dir: String,
        filename: String,
    ): Uri {
        val dirs = createDirIfNotExits(dir)
        val file = dirs.findFile(filename)
        val f = file ?: dirs.createFile(mimeType, filename)
            ?: throw CreateFailedException("创建文件失败")

        Napier.d { "$dirs/$filename" }
        return f.uri
    }

    private fun DocumentFileCompat.createDirIfNotExits(name: String): DocumentFileCompat {
        return findFile(name) ?: createDirectory(name)
            ?: throw CreateFailedException("创建文件夹失败")
    }

    private suspend fun fileNameTemplate(
        ids: ViewIds,
        title: String,
        subTitle: String,
    ): Pair<String, String> {
        val userData = asPreferencesDataSource.userData.first()
        var template = userData.fileNamesConvention ?: DEFAULT_NAMING_RULE
        Napier.d { "命名模板: $template" }
        // 如果文件名的尾部是 / 则去掉
        while (template.endsWith("/")) {
            template = template.dropLast(1)
        }
        val path = template
            .replace("{AV}", ids.aid.toString())
            .replace("{BV}", ids.bvid)
            .replace("{CID}", ids.cid.toString())
            .replace("{TITLE}", title)
            .replace("{P_TITLE}", subTitle)
        val index = path.indexOfLast { it == '/' }
        val dir = path.substring(0, index)
        val filename = path.substring(index + 1, path.length)
        Napier.d { "$dir/$filename" }
        return dir to filename
    }

    fun delete(ids: List<Int>) {
        applicationScope.launch {
            downloadTaskDao.findByIds(ids).forEach {
                delete(it)
            }
        }
    }

    private suspend fun delete(taskEntity: DownloadTaskEntity) {
        Napier.d { taskEntity.toString() }
        val uri = taskEntity.uri
        if (UriUtils.isFileScheme(uri)) {
            if (uri.toFile().delete()) {
//                Toaster.show("删除成功")
            } else {
//                Toaster.show("删除失败")
            }
        } else if (UriUtils.isContentScheme(uri)) {
            if (ContentResolverUtils.deleteDocument(uri)) {
//                Toaster.show("删除成功")
            } else {
//                Toaster.show("删除失败")
            }
        } else {
            Napier.d { taskEntity.toString() }
            throw IllegalArgumentException("task uri error")
        }
        downloadTaskDao.delete(listOf(taskEntity.id))
        deleteEmptyDirectoriesOfFolder(DevUtils.getContext().downloadDir)
    }

    private fun deleteEmptyDirectoriesOfFolder(folder: File) {
        val files = folder.listFiles() ?: return
        if (files.isEmpty()) {
            folder.delete()
        } else {
            for (fileEntry in files) {
                if (fileEntry.isDirectory) {
                    deleteEmptyDirectoriesOfFolder(fileEntry)
                    if (fileEntry.listFiles() != null && fileEntry.listFiles()?.size == 0) {
                        fileEntry.delete()
                    }
                }
            }
        }
    }
}
