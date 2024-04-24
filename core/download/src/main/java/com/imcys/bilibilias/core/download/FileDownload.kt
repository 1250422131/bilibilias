package com.imcys.bilibilias.core.download

import android.content.Context
import androidx.collection.mutableObjectListOf
import androidx.core.net.toUri
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.AudioTask
import com.imcys.bilibilias.core.download.task.VideoTask
import com.imcys.bilibilias.core.download.task.getState
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.network.repository.DanmakuRepository
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.kotlin.listener.createListener1
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

val Context.downloadDir
    get() = File(filesDir.parent, "download").apply {
        mkdirs()
    }

@Singleton
class FileDownload @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    @ApplicationContext private val context: Context,
    private val videoRepository: VideoRepository,
    private val danmakuRepository: DanmakuRepository,
    private val downloadTaskDao: DownloadTaskDao,
) {
    private val taskQueue = mutableObjectListOf<AsDownloadTask>()

    init {
        if (BuildConfig.DEBUG) {
            Util.enableConsoleLog()
        }
    }

    val taskFlow = MutableStateFlow<ImmutableList<AsDownloadTask>>(persistentListOf())
    private val listener = createListener1(
        taskStart = { task, model ->
            Napier.d(tag = "listener") { "任务开始 $task" }
            val asTask = taskQueue.first { it.okTask === task }
            scope.launch {
                val info = asTask.viewInfo
                val taskEntity = DownloadTaskEntity(
                    uri = asTask.destFile.toUri(),
                    created = Clock.System.now(),
                    aid = info.aid,
                    bvid = info.bvid,
                    cid = info.cid,
                    fileType = asTask.fileType,
                    subTitle = asTask.subTitle,
                    title = info.title,
                    state = getState(task),
                )
                downloadTaskDao.insertOrUpdate(taskEntity)
            }
        },
        progress = { task, currentOffset, totalLength ->
            Napier.d(tag = "listener") { "任务中 $task $currentOffset-$totalLength" }
            scope.launch {
                downloadTaskDao.updateProgressAndState(
                    task.uri,
                    getState(task),
                    currentOffset,
                    totalLength
                )
            }
        },
        taskEnd = { task, cause, realCause, model ->
            Napier.d(tag = "listener") { "任务结束 $task" }
            scope.launch {
                val status = cause.toString()
                task.addTag(0, status)
                // 手动更新断点信息到数据库
                task.info?.let { OkDownload.with().breakpointStore().update(it) }
                downloadTaskDao.updateState(task.uri, getState(task))
            }
        }
    )

    fun download(request: DownloadRequest) {
        Napier.d { "下载任务详情: $request" }
        scope.launch {
            val (detail, streamUrl) = get视频详情和流链接(request)
            dispatcherTaskType(
                streamUrl,
                request,
                detail.pages.single { it.cid == request.viewInfo.cid }
            )
            persistedFile(detail, request)
        }
    }

    private fun dispatcherTaskType(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        page: ViewDetail.Pages
    ) {
        scope.launch {
            when (request.format.taskType) {
                TaskType.ALL -> handleAllTask(streamUrl, request, page)
                TaskType.VIDEO -> handleVideoTask(streamUrl, request, page)
                TaskType.AUDIO -> handleAudioTask(streamUrl, request, page)
            }
        }
    }

    private fun handleAllTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        page: ViewDetail.Pages
    ) {
        val video = createVideoTask(streamUrl, request, page)
        val audio = createAudioTask(streamUrl, request, page)
        DownloadTask.enqueue(arrayOf(video.okTask, audio.okTask), listener)
    }

    private fun handleAudioTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        page: ViewDetail.Pages
    ) {
        val task = createVideoTask(streamUrl, request, page)
        task.okTask.enqueue(listener)
    }

    private fun createAudioTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        page: ViewDetail.Pages
    ): AudioTask {
        val task = AudioTask(streamUrl, request, page)
        taskQueue.add(task)
        return task
    }

    private fun handleVideoTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        page: ViewDetail.Pages
    ) {
        val task = createVideoTask(streamUrl, request, page)
        task.okTask.enqueue(listener)
    }

    private fun createVideoTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        page: ViewDetail.Pages
    ): VideoTask {
        val task = VideoTask(streamUrl, request, page)
        taskQueue.add(task)
        return task
    }

    private suspend fun get视频详情和流链接(request: DownloadRequest): Pair<ViewDetail, VideoStreamUrl> {
        val detail = scope.async { videoRepository.获取视频详细信息(request.viewInfo.bvid) }
        val streamUrl =
            scope.async {
                videoRepository.videoStreamingURL(
                    request.viewInfo.aid,
                    request.viewInfo.bvid,
                    request.viewInfo.cid
                )
            }
        return detail.await() to streamUrl.await()
    }

    private fun persistedFile(detail: ViewDetail, request: DownloadRequest) {
        scope.launch {
            val cid = detail.cid
            val path = request.buildBasePath()
            saveDmFile(path, cid)
            saveEntryFile(detail, cid, path)
        }
    }

    private fun saveDmFile(path: String, cid: Long) {
        scope.launch {
            val bytes = danmakuRepository.getRealTimeDanmaku(cid)
            File(path, "danmaku.xml").writeBytes(bytes)
        }
    }

    // region entry.json
    private fun saveEntryFile(detail: ViewDetail, cid: Long, path: String) {
        val page = detail.pages.single { it.cid == cid }
        val content = """
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
    fun cancle(task: AsDownloadTask) {
    }

    private fun reverseType(fileType: FileType) = when (fileType) {
        FileType.VIDEO -> FileType.AUDIO
        FileType.AUDIO -> FileType.VIDEO
    }
}
