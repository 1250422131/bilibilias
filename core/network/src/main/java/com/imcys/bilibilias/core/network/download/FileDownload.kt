package com.imcys.bilibilias.core.network.download

import android.content.Context
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.model.video.Audio
import com.imcys.bilibilias.core.model.video.Video
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.network.repository.DanmakuRepository
import com.imcys.bilibilias.core.network.repository.VideoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
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
    private val downloader: Downloader
) {
    fun allTaskFlow(): Flow<List<Task>> = downloader.taskFlow
    fun allTask() = downloader.allTask()
    fun enqueue(parameter: DownloadParameter) {
        scope.launch {
            val (detail, streamUrl) = get视频详情和流链接(parameter)
            handleTaskType(streamUrl, parameter)
            persistedFile(detail, parameter.cid)
        }
    }

    private suspend fun get视频详情和流链接(parameter: DownloadParameter): Pair<ViewDetail, VideoStreamUrl> {
        val detail = scope.async { videoRepository.获取视频详细信息(parameter.bvid) }
        val streamUrl =
            scope.async {
                videoRepository.videoStreamingURL(
                    parameter.aid,
                    parameter.bvid,
                    parameter.cid
                )
            }
        return detail.await() to streamUrl.await()
    }

    private fun handleTaskType(
        streamUrl: VideoStreamUrl,
        parameter: DownloadParameter
    ) {
        when (val type = parameter.format.taskType) {
            TaskType.ALL -> handleAllTask(streamUrl, parameter, type.extension)
            TaskType.VIDEO -> handleVideoTask(streamUrl, parameter, type.extension)
            TaskType.AUDIO -> handleAudioTask(streamUrl, parameter, type.extension)
        }
    }

    private fun handleAllTask(
        streamUrl: VideoStreamUrl,
        parameter: DownloadParameter,
        extension: String
    ) {
        handleVideoTask(streamUrl, parameter, extension)
        handleAudioTask(streamUrl, parameter, extension)
    }

    private fun handleAudioTask(
        streamUrl: VideoStreamUrl,
        parameter: DownloadParameter,
        extension: String
    ) {
        val audio = getAudioStrategy(streamUrl.dash.audio, parameter)
        download(audio, parameter, extension)
    }

    private fun handleVideoTask(
        streamUrl: VideoStreamUrl,
        parameter: DownloadParameter,
        extension: String
    ) {
        val video = getVideoStrategy(streamUrl.dash.video, parameter)
        download(video, parameter, extension)
    }

    private fun download(
        video: Video,
        parameter: DownloadParameter,
        extension: String
    ) {
        val task = createTask(video.baseUrl, "video.$extension", parameter, FileType.VIDEO)
        downloader.dispatch(task)
    }

    private fun download(
        audio: Audio,
        parameter: DownloadParameter,
        extension: String
    ) {
        val task = createTask(audio.baseUrl, "audio.$extension", parameter, FileType.AUDIO)
        downloader.dispatch(task)
    }

    private fun createTask(
        url: String,
        name: String,
        parameter: DownloadParameter,
        type: FileType,
    ): Task {
        val path = buildDownloadFullPath(parameter.aid, parameter.cid, parameter.format.quality)
        val file = File(path, name).apply { parentFile!!.mkdirs(); createNewFile() }
        return Task(
            url,
            file,
            type,
            parameter.title,
            parameter.cid,
            parameter.bvid
        )
    }

    private fun getVideoStrategy(video: List<Video>, parameter: DownloadParameter): Video {
        val videos = video.groupBy { it.id }[parameter.format.quality] ?: error("没有所选清晰度")
        return videos.single { it.codecid == parameter.format.codecid }
    }

    private fun getAudioStrategy(audio: List<Audio>, parameter: DownloadParameter): Audio {
        return audio.maxBy { it.id }
    }

    private fun persistedFile(detail: ViewDetail, cid: Long) {
        val path = buildDownloadBasePath(detail.aid, detail.cid)
        saveDmFile(path, cid)
        saveEntryFile(detail, cid, path)
    }

    private fun saveDmFile(path: String, cid: Long) {
        scope.launch {
            val bytes = danmakuRepository.getRealTimeDanmaku(cid)
            File(path).writeBytes(bytes)
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
        File(path).writeText(content)
    }

    // endregion
    private fun buildDownloadFullPath(aid: Long, cid: Long, quality: Int): String {
        return "${buildDownloadBasePath(aid, cid)}${File.separator}$quality"
    }

    private fun buildDownloadBasePath(aid: Long, cid: Long): String {
        return "${context.downloadDir}${File.separator}$aid${File.separator}c_$cid"
    }

    fun cancel(type: FileType, bvid: String, cid: Long) {
        val task =
            allTask().single { it.groupId == cid && it.type == type && it.groupTag == bvid }
        downloader.cancel(task)
        task.path.delete()
    }
}
