package com.imcys.bilibilias.core.download

import android.content.Context
import androidx.core.net.toFile
import com.imcys.bilibilias.core.common.download.DefaultConfig.DEFAULT_NAMING_RULE
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.AudioTask
import com.imcys.bilibilias.core.download.task.VideoTask
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.core.network.repository.DanmakuRepository
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.Util
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.DevUtils
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

val Context.downloadDir
    get() =
        File(filesDir.parent, "download").apply {
            mkdirs()
        }

@Suppress("LongParameterList")
@Singleton
class DownloadManager @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    @ApplicationContext private val context: Context,
    private val videoRepository: VideoRepository,
    private val danmakuRepository: DanmakuRepository,
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
                val (detail, streamUrl) = get视频详情和流链接(request)
                dispatcherTaskType(
                    streamUrl,
                    request,
                    detail,
                )
            } catch (e: Exception) {
                Napier.e(e, tag = "download") { "下载发生错误" }
            }
        }
    }

    private fun dispatcherTaskType(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail,
    ) {
        scope.launch {
            Napier.d { "任务类型 ${request.format.taskType}" }
            when (request.format.taskType) {
                TaskType.ALL -> handleAllTask(streamUrl, request, viewDetail)
                TaskType.VIDEO -> handleVideoTask(streamUrl, request, viewDetail)
                TaskType.AUDIO -> handleAudioTask(streamUrl, request, viewDetail)
            }
        }
    }

    private fun createTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail,
        fileType: FileType,
    ): AsDownloadTask {
        val info = request.viewInfo
        val page = viewDetail.pages.single { it.cid == info.cid }
        val path = getStoragePath() + File.separator + getSubfolder(viewDetail)
        return when (fileType) {
            FileType.VIDEO -> VideoTask(
                streamUrl,
                info,
                page,
                path,
                request.format.quality,
                request.format.codecid
            )

            FileType.AUDIO -> AudioTask(streamUrl, request.viewInfo, page, path)
        }.also(listener::add)
    }

    private fun getStoragePath() = runBlocking {
        val userData = asPreferencesDataSource.userData.first()
        userData.storagePath?.let {
            return@runBlocking it
        }
        context.downloadDir.path
    }

    /**
     *  AV号: {AV}
     *  BV号: {BV}
     *  CID号: {CID}
     *  视频标题: {TITLE}
     *  分P标题: {P_TITLE}
     */
    private fun getSubfolder(detail: ViewDetail): String = runBlocking {
        val userData = asPreferencesDataSource.userData.first()
        val namingRule = userData.namingRule
        if (namingRule == null) {
            replaceTemplate(DEFAULT_NAMING_RULE, detail)
        } else {
            replaceTemplate(namingRule, detail)
        }
    }

    private fun replaceTemplate(template: String, info: ViewDetail) = template
        .replace("{AV}", info.aid.toString())
        .replace("{BV}", info.bvid)
        .replace("{CID}", info.cid.toString())
        .replace("{TITLE}", info.title.toString())
        .replace("{P_TITLE}", info.toString())

    private fun handleAllTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail,
    ) {
        val video = createTask(streamUrl, request, viewDetail, FileType.VIDEO)
        val audio = createTask(streamUrl, request, viewDetail, FileType.AUDIO)
        DownloadTask.enqueue(arrayOf(video.okTask, audio.okTask), listener)
    }

    private fun handleAudioTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail,
    ) {
        val task = createTask(streamUrl, request, viewDetail, FileType.AUDIO)
        task.okTask.enqueue(listener)
    }

    private fun handleVideoTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail,
    ) {
        val task = createTask(streamUrl, request, viewDetail, FileType.VIDEO)
        task.okTask.enqueue(listener)
    }

    private suspend fun get视频详情和流链接(request: DownloadRequest): Pair<ViewDetail, VideoStreamUrl> {
        val detail = scope.async { videoRepository.获取视频详细信息(request.viewInfo.bvid) }
        val streamUrl =
            scope.async {
                videoRepository.videoStreamingURL(
                    request.viewInfo.aid,
                    request.viewInfo.bvid,
                    request.viewInfo.cid,
                )
            }
        return detail.await() to streamUrl.await()
    }

    private fun persistedFile(
        detail: ViewDetail,
        request: DownloadRequest,
    ) {
        scope.launch {
            val cid = detail.cid
            val path = request.buildBasePath()
            saveDmFile(path, cid)
            saveEntryFile(detail, cid, path)
        }
    }

    private fun saveDmFile(
        path: String,
        cid: Long,
    ) {
        scope.launch {
            val bytes = danmakuRepository.getRealTimeDanmaku(cid)
            File(path, "danmaku.xml").writeBytes(bytes)
        }
    }

    // region entry.json
    private fun saveEntryFile(
        detail: ViewDetail,
        cid: Long,
        path: String,
    ) {
        val page = detail.pages.single { it.cid == cid }
        val content =
            """
                {
                    "media_type": 2,
                    "has_dash_audio": true,
                    "is_completed": true,
                    "total_bytes": ${Int.MIN_VALUE},
                    "downloaded_bytes": ${Int.MAX_VALUE},
                    "title": "${detail.title}",
                    "type_tag": "80",
                    "cover": "${detail.pic}",
                    "video_quality": 80,
                    "prefered_video_quality": 80,
                    "guessed_total_bytes": 0,
                    "total_time_milli": ${detail.duration},
                    "danmaku_count": ${detail.stat.danmaku},
                    "time_update_stamp": ${System.currentTimeMillis()},
                    "time_create_stamp": ${System.currentTimeMillis()},
                    "can_play_in_advance": true,
                    "interrupt_transform_temp_file": false,
                    "quality_pithy_description": "4K",
                    "quality_superscript": "",
                    "cache_version_code": 7630200,
                    "preferred_audio_quality": 0,
                    "audio_quality": 0,
                    "avid": ${detail.aid},
                    "spid": 0,
                    "seasion_id": 0,
                    "bvid": "${detail.bvid}",
                    "owner_id": ${detail.owner.mid},
                    "owner_name": "${detail.owner.name}",
                    "owner_avatar": "${detail.owner.face}",
                    "page_data": {
                        "cid": ${page.cid},
                        "page": 1,
                        "from": "${page.from}",
                        "part": "${page.part}",
                        "link": "${page.weblink}",
                        "rich_vid": "",
                        "vid": "${page.vid}",
                        "has_alias": false,
                        "tid": ${detail.tid},
                        "width": ${page.dimension.width},
                        "height": ${page.dimension.height},
                        "rotate": ${page.dimension.rotate},
                        "download_title": "视频已缓存完成",
                        "download_subtitle": "${page.part}"
                    }
                }
            """.trimIndent()
        File(path, "entry.json").writeText(content)
    }

    // endregion

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
