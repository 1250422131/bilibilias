package com.imcys.bilibilias.core.download

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.domain.GetViewWithPlayerPlayUrlUseCase
import com.imcys.bilibilias.core.download.DefaultConfig.DEFAULT_NAMING_RULE
import com.imcys.bilibilias.core.download.media.MimeType
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Sources
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewIds
import com.imcys.bilibilias.core.network.di.ApplicationScope
import com.lazygeniouz.dfc.file.DocumentFileCompat
import com.liulishuo.okdownload.core.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.DevUtils
import dev.utils.app.ContentResolverUtils
import dev.utils.app.UriUtils
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.maxBy
import kotlin.collections.singleOrNull

val Context.downloadDir
    get() =
        File(getExternalFilesDir(null)!!.parent, "download").apply {
            mkdirs()
        }
private const val TAG = "DownloadManager"

@Suppress("LongParameterList")
@Singleton
class DownloadManager @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    @ApplicationContext private val context: Context,
    private val downloadTaskDao: DownloadTaskDao,
    private val listener: AsDownloadListener,
    private val asPreferencesDataSource: AsPreferencesDataSource,
    private val getViewWithPlayerPlayUrlUseCase: GetViewWithPlayerPlayUrlUseCase,
    private val errorMonitor: ErrorMonitor,
) {
    init {
        if (BuildConfig.DEBUG) {
            Util.enableConsoleLog()
        }
    }

    fun download(ids: List<Bvid>) {
        Napier.d { ids.joinToString() }
        scope.launch {
            for (id in ids) {
                // todo 处理 pages
                downloadedTest(
                    taskType = TaskType.ALL,
                    viewWithPlayerVideoStreamUrl = { getViewWithPlayerPlayUrlUseCase(id).first() },
                    vUrl = {
                        it.groupBy { it.id }
                            .maxBy { it.key }
                            .value
                            .maxBy { it.codecid }
                            .baseUrl
                    },
                    aUrl = {
                        it.maxBy { it.id }.baseUrl
                    },
                    pages = {
                        it.first().part
                    },
                    vTask = { url, ids, title, subTitle ->
                        createTask(url, ids, title, subTitle, FileType.VIDEO)
                    },
                    aTask = { url, ids, title, subTitle ->
                        createTask(url, ids, title, subTitle, FileType.VIDEO)
                    },
                )
            }
        }
    }

    fun download(request: DownloadRequest) {
        Napier.d { "下载任务详情: $request" }
        scope.launch {
            downloadedTest(
                taskType = request.format.taskType,
                viewWithPlayerVideoStreamUrl = { getViewWithPlayerPlayUrlUseCase(request.viewInfo.bvid).first() },
                vUrl = { sources ->
                    val map = sources.groupBy { it.id }
                    val videos = map[request.format.quality] ?: map.maxBy { it.key }.value
                    videos.singleOrNull { it.codecid == request.format.codecid }?.baseUrl
                        ?: videos.maxBy { it.codecid }.baseUrl
                },
                aUrl = { it.maxBy { it.id }.baseUrl },
                pages = { pages ->
                    pages.single { it.cid == request.viewInfo.cid }.part
                },
                vTask = { url, ids, title, subTitle ->
                    createTask(url, ids, title, subTitle, FileType.VIDEO)
                },
                aTask = { url, ids, title, subTitle ->
                    createTask(url, ids, title, subTitle, FileType.AUDIO)
                },
            )
        }
    }

    suspend fun downloadedTest(
        taskType: TaskType,
        viewWithPlayerVideoStreamUrl: suspend () -> Pair<ViewDetail, VideoStreamUrl>,
        vUrl: (List<Sources>) -> String,
        aUrl: (List<Sources>) -> String,
        pages: (List<ViewDetail.Pages>) -> String,
        vTask: (url: String, ids: ViewIds, title: String, subTitle: String) -> Unit,
        aTask: (url: String, ids: ViewIds, title: String, subTitle: String) -> Unit,
    ) {
        val (detail, url) = viewWithPlayerVideoStreamUrl()
        val dash = url.dash
        val downloadVideoUrl = vUrl(dash.video)
        val downloadAudioUrl = aUrl(dash.audio)
        val viewIds = ViewIds(detail.aid, detail.bvid, detail.cid)
        val subTitle = pages(detail.pages)
        val title = detail.title
        when (taskType) {
            TaskType.ALL -> {
                val title = detail.title
                vTask(downloadVideoUrl, viewIds, subTitle, title)
                aTask(downloadAudioUrl, viewIds, subTitle, title)
            }

            TaskType.VIDEO -> vTask(downloadVideoUrl, viewIds, subTitle, title)
            TaskType.AUDIO -> aTask(downloadAudioUrl, viewIds, subTitle, title)
        }
    }

    private fun createTask(
        url: String,
        ids: ViewIds,
        title: String,
        subTitle: String,
        type: FileType,
    ) {
        scope.launch {
            val (mimeType, extension) = when (type) {
                FileType.VIDEO -> MimeType.VIDEO to ".mp4"
                FileType.AUDIO -> MimeType.AUDIO to ".acc"
            }
            val uri = createFile(
                System.currentTimeMillis().toString() + extension,
                ids,
                title,
                subTitle,
                mimeType,
                extension,
            )
            if (uri == null) {
                errorMonitor.addShortErrorMessage("创建文件失败")
                throw CreateFileFailedException("创建文件失败")
            }
            val task = AsDownloadTask(ids, title, subTitle, type, url, uri)
            listener.add(task)
        }
    }

    private suspend fun createFile(
        defaultFilename: String,
        ids: ViewIds,
        title: String,
        subTitle: String,
        mimeType: String,
        extension: String,
    ): Uri? {
        val userData = asPreferencesDataSource.userData.first()
        val path = userData.storageFolder
        Napier.d { "存储路径: $path" }
        return if (path == null) {
            File(context.downloadDir, defaultFilename).toUri()
        } else {
            createFoldersAndFiles(ids, title, subTitle, mimeType, extension, path)
        }
    }

    private suspend fun createFoldersAndFiles(
        viewInfo: ViewIds,
        title: String,
        subTitle: String,
        mimeType: String,
        extension: String,
        path: String,
    ): Uri {
        val (folderName, filename) = generateFileNameByNamingConventions(viewInfo, title, subTitle)
        val filenameWithExtension = filename + extension
        val tree = DocumentFileCompat.fromTreeUri(context, path.toUri())!!
        val folder = tree.createDirectory(folderName) ?: throw CreateFileFailedException("创建文件夹失败")
        val file = folder.createFile(mimeType, filenameWithExtension) ?: throw CreateFileFailedException("创建文件失败")

        Napier.d { "file: $folder/$file" }
        return file.uri
    }

    private suspend fun generateFileNameByNamingConventions(
        ids: ViewIds,
        title: String,
        subTitle: String,
    ): Pair<String, String> {
        val userData = asPreferencesDataSource.userData.first()
        var template = userData.fileNamesConvention ?: DEFAULT_NAMING_RULE
        Napier.d { "命名规则: $template" }
        // 如果文件名的尾部是 / 则去掉
        if (template.endsWith("/")) {
            template = template.dropLast(1)
        }
        val path = template
            .replace("{AV}", ids.aid.toString())
            .replace("{BV}", ids.bvid)
            .replace("{CID}", ids.cid.toString())
            .replace("{TITLE}", title)
            .replace("{P_TITLE}", subTitle)
        val index = path.indexOfLast { it == '/' }
        val folder = path.substring(0, index)
        val filename = path.substring(index + 1, path.length)
        Napier.d { "文件夹: $folder, 文件: $filename, 路径: $path" }
        return folder to filename
    }

    fun delete(ids: List<Int>) {
        scope.launch {
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
        if (files.size == 0) {
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
