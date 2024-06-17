package com.imcys.bilibilias.core.download

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.imcys.bilibilias.core.common.download.DefaultConfig.DEFAULT_NAMING_RULE
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
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
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

val Context.downloadDir
    get() =
        File(filesDir.parent, "download").apply {
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

    fun download(request: DownloadRequest) {
        Napier.d { "下载任务详情: $request" }
        scope.launch {
            try {
                download(
                    taksType = request.format.taskType,
                    getDetail = { bvid: Bvid ->
                        videoRepository.获取视频详细信息(bvid)
                    },
                    getDownloadUrl = { aid: Aid, bvid: Bvid, cid: Cid ->
                        videoRepository.videoStreamingURL(
                            aid,
                            bvid,
                            cid,
                        )
                    },
                    videoStrategy = { sources, detail, page ->
                        val format = request.format
                        generate(sources, detail, page, format.quality, format.codecid)
                    },
                    audioStrategy = { sources, detail, page ->
                        generate(sources, detail, page)
                    }
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
            val newTaskType = when (taksType) {
                TaskType.ALL -> arrayOf(TaskType.VIDEO, TaskType.AUDIO)
                TaskType.VIDEO -> arrayOf(TaskType.VIDEO)
                TaskType.AUDIO -> arrayOf(TaskType.AUDIO)
            }
            newTaskType.forEach {
                when (it) {
                    TaskType.VIDEO -> {
                        val task = videoStrategy(downloadUrl.dash.video, detail, page)
                        if (task != null) task.also(listener::add)
                        else {
                            result = TaskResult.Failure
                        }
                    }

                    TaskType.AUDIO -> {
                        val task = audioStrategy(downloadUrl.dash.audio, detail, page)
                        if (task != null) task.also(listener::add)
                        else {
                            result = TaskResult.Failure
                        }
                    }

                    TaskType.ALL -> throw UnsupportedOperationException()
                }
            }
            result
        }
    }

    private suspend fun generate(
        sources: List<Audio>,
        detail: ViewDetail,
        page: ViewDetail.Pages
    ): AsDownloadTask? {
        val url = sources.maxBy { it.id }.baseUrl
        val info = ViewInfo(detail.aid, detail.bvid, detail.cid, detail.title)
        val file = createFile(
            System.currentTimeMillis().toString() + ".acc",
            info,
            page.part,
            MimeType.AUDIO
        )
        return if (file != null) AsDownloadTask(info, page.part, FileType.AUDIO, url, file)
        else null

    }

    private suspend fun generate(
        sources: List<Video>,
        detail: ViewDetail,
        page: ViewDetail.Pages,
        quality: Int,
        codecid: Int
    ): AsDownloadTask? {
        val videos = sources.groupBy { it.id }[quality] ?: error("没有所选清晰度")
        val v = videos.singleOrNull { it.codecid == codecid }
            ?: videos.maxBy { it.codecid }
        val info = ViewInfo(detail.aid, detail.bvid, detail.cid, detail.title)
        val file = createFile(
            System.currentTimeMillis().toString() + ".mp4",
            info,
            page.part,
            MimeType.VIDEO
        )
        return if (file != null) AsDownloadTask(info, page.part, FileType.AUDIO, v.baseUrl, file)
        else null
    }

    private suspend fun createFile(
        defaultFilename: String,
        viewInfo: ViewInfo,
        subTitle: String,
        mimeType: String,
    ): Uri? {
        val userData = asPreferencesDataSource.userData.first()
        val path = userData.storagePath
        return if (path == null) {
            File(context.downloadDir, defaultFilename).toUri()
        } else {
            val tree = DocumentFileCompat.fromTreeUri(context, path.toUri())!!
            val (foldername, filename) = generateFolderWithFile(viewInfo, subTitle)
            tree.createDirectory(foldername)
            val file = tree.createFile(mimeType, filename)
            file?.uri
        }
    }

    private suspend fun generateFolderWithFile(
        info: ViewInfo,
        subTitle: String
    ): Pair<String, String> {
        val userData = asPreferencesDataSource.userData.first()
        var template = userData.namingRule ?: DEFAULT_NAMING_RULE
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

        val index = template.indexOfFirst { it == '/' }
        val foldername = path.substring(0, index)
        val filename = path.substring(index + 1, template.length)
        return foldername to filename
    }

    fun delete(
        info: ViewInfo,
        fileType: FileType,
    ) {
        scope.launch {
            val taskByInfo = downloadTaskDao.getTaskBy(info.aid, info.bvid, info.cid, fileType)
            taskByInfo?.uri?.toFile()?.delete()
            deleteEmptyDirectoriesOfFolder(DevUtils.getContext().downloadDir)
            if (taskByInfo != null) {
                downloadTaskDao.delete(taskByInfo)
            }
        }
    }

    private fun deleteEmptyDirectoriesOfFolder(folder: File) {
        if (folder.listFiles()?.size == 0) {
            folder.delete()
        } else {
            for (fileEntry in folder.listFiles()) {
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
