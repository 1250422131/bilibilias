package com.imcys.bilibilias.core.network.download

import android.content.Context
import android.preference.PreferenceManager
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.model.video.Audio
import com.imcys.bilibilias.core.model.video.Video
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.network.repository.DanmakuRepository
import com.imcys.bilibilias.core.network.repository.VideoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
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
            val detail = videoRepository.获取视频详细信息(parameter.bvid)
//            persistedFile
            val streamUrl =
                videoRepository.videoStreamingURL(parameter.aid, parameter.bvid, parameter.cid)
            download(streamUrl, detail, parameter)
        }
    }

    private fun download(
        streamUrl: VideoStreamUrl,
        detail: ViewDetail,
        parameter: DownloadParameter
    ) {
        startVideoDownload(
            getVideoStrategy(streamUrl.dash.video, parameter.quality),
            detail, parameter.quality
        )
        startAudioDownload(
            getAudioStrategy(streamUrl.dash.audio),
            detail, parameter.quality
        )
    }

    // TODO: 两个函数重复了
    private fun startVideoDownload(video: Video, parameter: ViewDetail, quality: Int) {
        val path = buildDownloadFullPath(parameter.aid, parameter.cid, quality)
        val file = File(path, "video.m4s").apply { parentFile!!.mkdirs(); createNewFile() }
        val task = Task(
            video.baseUrl,
            file,
            FileType.VIDEO,
            parameter.title,
            parameter.cid,
            parameter.bvid
        )
        downloader.dispatch(task)
    }

    private fun startAudioDownload(audio: Audio, parameter: ViewDetail, quality: Int) {
        val path = buildDownloadFullPath(parameter.aid, parameter.cid, quality)
        val file = File(path, "audio.m4s").apply { parentFile!!.mkdirs(); createNewFile() }
        val task = Task(
            audio.baseUrl,
            file,
            FileType.AUDIO,
            parameter.title,
            parameter.cid,
            parameter.bvid
        )
        downloader.dispatch(task)
    }

    private fun getVideoStrategy(video: List<Video>, quality: Int): Video {
        val videos = video.groupBy { it.id }[quality] ?: error("没有所选清晰度")
        return videos.maxBy { it.codecid }
    }

    private fun getAudioStrategy(audio: List<Audio>): Audio {
        return audio.maxBy { it.id }
    }

    private fun persistedFile(aid: Long, cid: Long) {
        val path = buildDownloadBasePath(aid, cid)
        saveDmFile(path, cid)
    }

    private fun saveDmFile(path: String, cid: Long) {
        scope.launch {
            val bytes = danmakuRepository.getRealTimeDanmaku(cid)
            File(path).writeBytes(bytes)
        }
    }

    // region entry.json
    private fun saveEntryFile() {
        """
            {
                "media_type": 2,
                "has_dash_audio": true,
                "is_completed": true,
                "total_bytes": 623167143,
                "downloaded_bytes": 623167143,
                "title": "cos申鹤去墨尔本city海边一日游",
                "type_tag": "120",
                "cover": "http:\/\/i2.hdslb.com\/bfs\/archive\/ce46dc072f226c219d3e8269890e8fc18ea316b8.jpg",
                "video_quality": 120,
                "prefered_video_quality": 120,
                "guessed_total_bytes": 0,
                "total_time_milli": 195242,
                "danmaku_count": 55,
                "time_update_stamp": 1709807482370,
                "time_create_stamp": 1709807393236,
                "can_play_in_advance": true,
                "interrupt_transform_temp_file": false,
                "quality_pithy_description": "4K",
                "quality_superscript": "",
                "cache_version_code": 7630200,
                "preferred_audio_quality": 0,
                "audio_quality": 0,
                "avid": 1751258192,
                "spid": 0,
                "seasion_id": 0,
                "bvid": "BV1px42127ry",
                "owner_id": 3746949,
                "owner_name": "圆脸馨儿",
                "owner_avatar": "https:\/\/i0.hdslb.com\/bfs\/face\/c2798d04ba4bceeea388c36083e931630f18d3ed.jpg",
                "page_data": {
                    "cid": 1462053131,
                    "page": 1,
                    "from": "vupload",
                    "part": "cos申鹤去墨尔本city海边一日游",
                    "link": "",
                    "rich_vid": "",
                    "vid": "",
                    "has_alias": false,
                    "tid": 21,
                    "width": 3840,
                    "height": 2160,
                    "rotate": 0,
                    "download_title": "视频已缓存完成",
                    "download_subtitle": "cos申鹤去墨尔本city海边一日游"
                }
            }
        """.trimIndent()
    }

    // endregion
    private fun getUserDownloadOption(): String {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(
            "user_download_file_name_editText",
            "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}",
        ) ?: error("获取保存路径错误")
    }

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
    }
}

data class DownloadParameter(
    val aid: Long,
    val bvid: String,
    val cid: Long,
    val quality: Int,
)
