package com.imcys.bilibilias.core.download

import android.content.Context
import androidx.collection.mutableObjectListOf
import androidx.core.net.toUri
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import com.imcys.bilibilias.core.download.chore.DefaultGroupTaskCall
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.AudioTask
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.download.task.VideoTask
import com.imcys.bilibilias.core.download.task.getState
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.network.repository.DanmakuRepository
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.kotlin.listener.createListener1
import dev.DevUtils
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

val Context.downloadDir
    get() = File(filesDir.parent, "download").apply {
        mkdirs()
    }

@Singleton
class DownloadManager @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val videoRepository: VideoRepository,
    private val danmakuRepository: DanmakuRepository,
    private val downloadTaskDao: DownloadTaskDao,
    private val userPreferences: AsPreferencesDataSource,
    private val defaultGroupTaskCall: DefaultGroupTaskCall,
) {
    private val taskQueue = mutableObjectListOf<AsDownloadTask>()

    init {
        if (BuildConfig.DEBUG) {
            Util.enableConsoleLog()
        }
    }

    private val listener = createListener1(
        taskStart = { task, _ ->
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
        taskEnd = { task, cause, realCause, _ ->
            Napier.d(tag = "listener") { "任务结束 $task" }
            scope.launch {
                downloadTaskDao.updateState(task.uri, getState(task))
                val asDownloadTask = taskQueue.first { it.okTask === task }
                val info = asDownloadTask.viewInfo
                val tasks = downloadTaskDao.getTaskByInfo(info.aid, info.bvid, info.cid)
                val v = tasks.find { it.fileType == FileType.VIDEO }
                val a = tasks.find { it.fileType == FileType.AUDIO }
                Napier.d(cause.name, realCause, "下载完成")
                v?.let { v1 ->
                    a?.let { a1 ->
                        defaultGroupTaskCall.execute(GroupTask(v1, a1))
                    }
                }
//                if (v != null && a != null) {
//                    defaultGroupTaskCall.execute(GroupTask(v, a))
//                }
            }
        }
    )

    fun download(request: DownloadRequest) {
        Napier.d { "下载任务详情: $request" }
        scope.launch {
            try {
                val (detail, streamUrl) = get视频详情和流链接(request)
                dispatcherTaskType(
                    streamUrl,
                    request,
                    detail
                )
            } catch (e: Exception) {
                Napier.e(e, tag = "download") { "下载发生错误" }
            }

//            persistedFile(detail, request)
        }
    }

    private fun dispatcherTaskType(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail
    ) {
        scope.launch {
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
        val page = viewDetail.pages.single { it.cid == request.viewInfo.cid }
        val path = if (viewDetail.pages.size == 1) {
            "${viewDetail.title}"
        } else {
            "${viewDetail.title}${File.separator}${page.part}"
        }
        val fullPath = "${DevUtils.getContext().downloadDir}${File.separator}$path"
        return when (fileType) {
            FileType.VIDEO -> VideoTask(streamUrl, request, page, fullPath)
            FileType.AUDIO -> AudioTask(streamUrl, request, page, fullPath)
        }.also(taskQueue::add)
    }

    private fun handleAllTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail
    ) {
        val video = createTask(streamUrl, request, viewDetail, FileType.VIDEO)
        val audio = createTask(streamUrl, request, viewDetail, FileType.AUDIO)
        DownloadTask.enqueue(arrayOf(video.okTask, audio.okTask), listener)
    }

    private fun handleAudioTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail
    ) {
        val task = createTask(streamUrl, request, viewDetail, FileType.AUDIO)
        task.okTask.enqueue(listener)
    }

    private fun handleVideoTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        viewDetail: ViewDetail
    ) {
        val task = createTask(streamUrl, request, viewDetail, FileType.VIDEO)
        task.okTask.enqueue(listener)
    }

    /**
     * todo 优化路径逻辑，当视频没有子集时可以考虑直接使用当前视频名
     * {AV} {BV} {CID} {TITLE} {P_TITLE}
     */
    private fun customFoldername(request: DownloadRequest, part: String): String {
        val info = request.viewInfo

        val customPath = runBlocking {
            userPreferences.userData.first().fileNameRule
                .replace("{AV}", info.aid.toString())
                .replace("{BV}", info.bvid)
                .replace("{CID}", info.cid.toString())
                .replace("{TITLE}", info.title)
                .replace("{P_TITLE}", part)
        }
        return "${DevUtils.getContext().downloadDir}${File.separator}$customPath"
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
}
