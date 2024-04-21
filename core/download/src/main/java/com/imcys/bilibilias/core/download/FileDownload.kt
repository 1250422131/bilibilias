package com.imcys.bilibilias.core.download

import android.content.Context
import androidx.collection.mutableScatterMapOf
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.download.task.AudioTask
import com.imcys.bilibilias.core.download.task.GroupCallback
import com.imcys.bilibilias.core.download.task.GroupTask
import com.imcys.bilibilias.core.download.task.VideoTask
import com.imcys.bilibilias.core.model.video.Audio
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.Video
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.core.network.repository.DanmakuRepository
import com.imcys.bilibilias.core.network.repository.VideoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private val downloadTaskExecute: DownloadTaskExecute,
//    private val groupCallback: GroupCallback
) {
    private val taskQueue = mutableScatterMapOf<Cid, Array<AsDownloadTask>>()
    val taskFlow = MutableStateFlow<ImmutableList<AsDownloadTask>>(persistentListOf())

    fun download(request: DownloadRequest) {
        Napier.d { request.toString() }
        scope.launch {
            val (detail, streamUrl) = get视频详情和流链接(request)
            dispatcherTaskType(streamUrl, request)
            persistedFile(detail, request)
        }
    }

    private fun dispatcherTaskType(streamUrl: VideoStreamUrl, request: DownloadRequest) {
        when (request.format.taskType) {
            TaskType.ALL -> handleAllTask(streamUrl, request)
            TaskType.VIDEO -> handleVideoTask(streamUrl, request)
            TaskType.AUDIO -> handleAudioTask(streamUrl, request)
        }
    }

    private fun handleAllTask(streamUrl: VideoStreamUrl, request: DownloadRequest) {
        val video = createVideoTask(streamUrl, request)
        val audio = createAudioTask(streamUrl, request)
        putQueue(request, video, audio)
        downloadTaskExecute.enqueue(video, audio) { viewinfo, type ->
            val groupTask = 查询任务(viewinfo, type)

            Napier.d { "完成任务 $type-$groupTask" }
        }
    }

    private fun 查询任务(info: ViewInfo, currentType: FileType): GroupTask {
        val tasks = taskQueue[info.cid] ?: error("没有任务 $currentType-$info")
        val v = tasks.single { it.fileType == FileType.VIDEO }
        val a = tasks.single { it.fileType == FileType.AUDIO }
        return GroupTask(v, a)
    }

    private fun handleAudioTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        onComplete: TaskEnd = { _, _ -> }
    ) {
        val task = createVideoTask(streamUrl, request)
        putQueue(request, task)
        downloadTaskExecute.enqueue(task, onComplete)
    }

    private fun createAudioTask(streamUrl: VideoStreamUrl, request: DownloadRequest): AudioTask {
        val audio = getAudioStrategy(streamUrl.dash.audio, request)
        return AudioTask(
            audio.baseUrl,
            request.buildFullPath(),
            request.viewInfo
        )
    }

    private fun handleVideoTask(
        streamUrl: VideoStreamUrl,
        request: DownloadRequest,
        onComplete: TaskEnd = { _, _ -> }
    ) {
        val task = createVideoTask(streamUrl, request)
        putQueue(request, task)
        downloadTaskExecute.enqueue(task, onComplete)
    }

    private fun putQueue(request: DownloadRequest, vararg task: AsDownloadTask) {
        taskQueue[request.viewInfo.cid] = arrayOf(*task)
        taskFlow.update {
            taskQueue.asMap().flatMap { (_, arr) -> arr.map { it } }.toImmutableList()
        }
    }

    private fun createVideoTask(streamUrl: VideoStreamUrl, request: DownloadRequest): VideoTask {
        val audio = getVideoStrategy(streamUrl.dash.video, request)
        return VideoTask(
            audio.baseUrl,
            request.buildFullPath(),
            request.viewInfo
        )
    }

    private fun getVideoStrategy(video: List<Video>, parameter: DownloadRequest): Video {
        val videos = video.groupBy { it.id }[parameter.format.quality] ?: error("没有所选清晰度")
        return videos.single { it.codecid == parameter.format.codecid }
    }

    private fun getAudioStrategy(audio: List<Audio>, request: DownloadRequest): Audio {
        return audio.maxBy { it.id }
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

    private fun DownloadRequest.buildFullPath() =
        "${buildBasePath()}${File.separator}${format.quality}"

    private fun DownloadRequest.buildBasePath(): String {
        return "${context.downloadDir}${File.separator}${viewInfo.aid}${File.separator}c_${viewInfo.cid}"
    }

    private fun persistedFile(detail: ViewDetail, request: DownloadRequest) {
        val cid = detail.cid
        val path = request.buildBasePath()
        saveDmFile(path, cid)
        saveEntryFile(detail, cid, path)
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
}
