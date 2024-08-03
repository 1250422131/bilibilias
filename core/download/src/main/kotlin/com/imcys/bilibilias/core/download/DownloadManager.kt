package com.imcys.bilibilias.core.download

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.hjq.toast.Toaster
import com.imcys.bilibilias.core.common.download.DefaultConfig.DEFAULT_NAMING_RULE
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.media.MimeType
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Audio
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.Video
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.core.network.repository.VideoRepository
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
    private val videoRepository: VideoRepository,
    private val downloadTaskDao: DownloadTaskDao,
    private val listener: AsDownloadListener,
    private val asPreferencesDataSource: AsPreferencesDataSource,
) {
    init {
        if (BuildConfig.DEBUG) {
            Util.enableConsoleLog()
        }
    }

    fun download(ids: List<Bvid>) {
        Napier.d { ids.joinToString() }
    }

    // todo 也许要重构
    fun download(request: DownloadRequest) {
        Napier.d { "下载任务详情: $request" }
        scope.launch {
            try {
                val result = download(
                    taksType = request.format.taskType,
                    getDetail = { bvid: Bvid ->
                        videoRepository.获取视频详细信息(bvid)
                    },
                    getDownloadUrl = { aid, bvid, cid ->
                        videoRepository.videoStreamingURL(aid, bvid, cid)
                    },
                    videoStrategy = { sources, detail, page ->
                        val format = request.format
                        generate(sources, detail, page, format.quality, format.codecid)
                    },
                    audioStrategy = { sources, detail, page ->
                        generate(sources, detail, page)
                    },
                ).invoke(request.viewInfo)
            } catch (e: Exception) {
                Napier.e(e, TAG) { "下载发生错误" }
            }
        }
    }

    suspend fun download(
        taksType: TaskType,
        getDetail: suspend (bvid: Bvid) -> ViewDetail,
        getDownloadUrl: suspend (aid: Aid, bvid: Bvid, cid: Cid) -> VideoStreamUrl,
        videoStrategy: suspend (sources: List<Video>, detail: ViewDetail, page: ViewDetail.Pages) -> AsDownloadTask?,
        audioStrategy: suspend (sources: List<Audio>, detail: ViewDetail, page: ViewDetail.Pages) -> AsDownloadTask?,
    ): suspend (info: ViewInfo) -> TaskResult {
        return {
            var result: TaskResult = TaskResult.Success
            val detail = getDetail(it.bvid)
            val downloadUrl = getDownloadUrl(it.aid, it.bvid, it.cid)
            val page = detail.pages.single { it.cid == it.cid }
            Napier.d { "选中的子集 $page" }
            val newTaskType = when (taksType) {
                TaskType.ALL -> arrayOf(TaskType.VIDEO, TaskType.AUDIO)
                TaskType.VIDEO -> arrayOf(TaskType.VIDEO)
                TaskType.AUDIO -> arrayOf(TaskType.AUDIO)
            }
            newTaskType.forEach {
                when (it) {
                    TaskType.VIDEO -> {
                        val task = videoStrategy(downloadUrl.dash.video, detail, page)
                        Napier.d { "视频任务 $task" }
                        if (task != null) {
                            task.also(listener::add)
                        } else {
                            result = TaskResult.Failure
                        }
                    }

                    TaskType.AUDIO -> {
                        val task = audioStrategy(downloadUrl.dash.audio, detail, page)
                        Napier.d { "音频任务 $task" }
                        if (task != null) {
                            task.also(listener::add)
                        } else {
                            result = TaskResult.Failure
                        }
                    }

                    TaskType.ALL -> throw UnsupportedOperationException()
                }
            }
            Napier.d { "任务是否成功: $result, 任务类型: $taksType" }
            result
        }
    }

    private suspend fun generate(
        sources: List<Audio>,
        detail: ViewDetail,
        page: ViewDetail.Pages,
    ): AsDownloadTask? {
        val url = sources.maxBy { it.id }.baseUrl
        val info = ViewInfo(detail.aid, detail.bvid, detail.cid, detail.title)
        val file = createFile(
            System.currentTimeMillis().toString() + ".aac",
            info,
            page.part,
            MimeType.AUDIO,
            ".aac",
        )
        Napier.d { "下载链接 $url" }
        return if (file != null) {
            AsDownloadTask(info, page.part, FileType.AUDIO, url, file)
        } else {
            null
        }
    }

    private suspend fun generate(
        sources: List<Video>,
        detail: ViewDetail,
        page: ViewDetail.Pages,
        quality: Int,
        codecid: Int,
    ): AsDownloadTask? {
        val map = sources.groupBy { it.id }
        val videos = map[quality] ?: map.maxBy { it.key }.value
        val v = videos.singleOrNull { it.codecid == codecid }
            ?: videos.maxBy { it.codecid }
        val info = ViewInfo(detail.aid, detail.bvid, detail.cid, detail.title)
        val file = createFile(
            System.currentTimeMillis().toString() + ".mp4",
            info,
            page.part,
            MimeType.VIDEO,
            ".mp4",
        )
        Napier.d { "清晰度 $quality, 编码器 $codecid, 下载链接 ${v.baseUrl}" }
        return if (file != null) {
            AsDownloadTask(info, page.part, FileType.VIDEO, v.baseUrl, file)
        } else {
            null
        }
    }

    private suspend fun createFile(
        defaultFilename: String,
        viewInfo: ViewInfo,
        subTitle: String,
        mimeType: String,
        extension: String,
    ): Uri? {
        val userData = asPreferencesDataSource.userData.first()
        val path = userData.storagePath
        Napier.d { "存储路径: $path" }
        return if (path == null) {
            File(context.downloadDir, defaultFilename).toUri()
        } else {
            val tree = DocumentFileCompat.fromTreeUri(context, path.toUri())!!
            val (foldername, filename) = generateFolderWithFile(viewInfo, subTitle)
            val folderFile = tree.findFile(foldername)
            val filenameWithExtension = filename + extension
            if (folderFile == null) {
                Napier.d { "未创建文件夹 $foldername" }
                val folder = tree.createDirectory(foldername)
                folder?.findFile(filenameWithExtension)
            } else {
                Napier.d { "已创建文件夹 $foldername" }
                val findFile = folderFile.findFile(filenameWithExtension)
                Napier.d { "发现文件: ${findFile?.name}" }
                if (findFile == null) {
                    folderFile.createFile(mimeType, filenameWithExtension)
                } else {
                    findFile
                }
            }?.uri
        }
    }

    private suspend fun generateFolderWithFile(
        info: ViewInfo,
        subTitle: String,
    ): Pair<String, String> {
        val userData = asPreferencesDataSource.userData.first()
        var template = userData.namingRule ?: DEFAULT_NAMING_RULE
        Napier.d { "命名规则: $template" }
        // 如果文件名的尾部是 / 则去掉
        if (template.endsWith("/")) {
            template = template.dropLast(1)
        }
        val path = template
            .replace("{AV}", info.aid.toString())
            .replace("{BV}", info.bvid)
            .replace("{CID}", info.cid.toString())
            .replace("{TITLE}", info.title)
            .replace("{P_TITLE}", subTitle)
        val index = path.indexOfLast { it == '/' }
        val foldername = path.substring(0, index)
        val filename = path.substring(index + 1, path.length)
        Napier.d { "文件夹: $foldername, 文件: $filename, 路径: $path" }
        return foldername to filename
    }

    fun delete(ids: List<Int>) {
        scope.launch {
            downloadTaskDao.findByIds(ids).forEach {
                delete(it)
            }
        }
    }

    fun delete(
        info: ViewInfo,
        fileType: FileType,
    ) {
        scope.launch {
            val task =
                downloadTaskDao.findByIdWithFileType(info.aid, info.bvid, info.cid, fileType)
                    ?: return@launch
            delete(task)
        }
    }

    private suspend fun delete(taskEntity: DownloadTaskEntity) {
        Napier.d { taskEntity.toString() }
        val uri = taskEntity.uri
        if (UriUtils.isFileScheme(uri)) {
            if (uri.toFile().delete()) {
                Toaster.show("删除成功")
            } else {
                Toaster.show("删除失败")
            }
        } else if (UriUtils.isContentScheme(uri)) {
            if (ContentResolverUtils.deleteDocument(uri)) {
                Toaster.show("删除成功")
            } else {
                Toaster.show("删除失败")
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
