package com.imcys.bilibilias.download

import com.imcys.bilibilias.common.utils.autoRequestRetry
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadSubTaskType
import com.imcys.bilibilias.database.entity.download.DownloadTaskNodeType
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.video.BILIDonghuaOgvPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerSynthesize
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurl
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import kotlinx.serialization.json.Json

/**
 * 视频信息获取器
 * 负责从Bilibili API获取视频播放信息并提取下载URL
 */
class VideoInfoFetcher(
    private val videoInfoRepository: VideoInfoRepository,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val json: Json
) {
    /**
     * 获取视频播放信息
     */
    suspend fun fetchVideoPlayerInfo(
        segment: DownloadSegment,
        nodeType: DownloadTaskNodeType,
        downloadViewInfo: DownloadViewInfo
    ): NetWorkResult<Any?> {
        return when (nodeType) {
            DownloadTaskNodeType.BILI_VIDEO_INTERACTIVE,
            DownloadTaskNodeType.BILI_VIDEO_PAGE,
            DownloadTaskNodeType.BILI_VIDEO_SECTION_EPISODES -> {
                autoRequestRetry {
                    videoInfoRepository.getVideoPlayerInfo(
                        cid = segment.platformId.toLong(),
                        bvId = getSegmentBvId(segment),
                        curLanguage = downloadViewInfo.selectAudioLanguage?.lang,
                        curProductionType = downloadViewInfo.selectAudioLanguage?.productionType
                    )
                }
            }

            DownloadTaskNodeType.BILI_DONGHUA_EPISOD,
            DownloadTaskNodeType.BILI_DONGHUA_SEASON,
            DownloadTaskNodeType.BILI_DONGHUA_SECTION -> {
                autoRequestRetry {
                    videoInfoRepository.getDonghuaPlayerInfo(
                        epId = segment.platformId.toLong(),
                        null
                    )
                }
            }

            else -> throw IllegalStateException("缓存类型不支持: ${nodeType.name}")
        }
    }

    /**
     * 从播放信息中提取视频数据（Dash或Durl）
     */
    fun extractVideoData(
        playerInfo: NetWorkResult<Any?>,
        downloadViewInfo: DownloadViewInfo
    ): Any? {
        if (playerInfo.status != ApiStatus.SUCCESS) {
            return null
        }

        return when (val result = playerInfo.data) {
            is BILIDonghuaPlayerInfo -> result.dash ?: result.durls?.firstOrNull {
                it.quality == downloadViewInfo.selectVideoQualityId
            }?.durl?.first() ?: result.durls?.first()?.durl?.first()

            is BILIDonghuaOgvPlayerInfo -> result.videoInfo.dash
                ?: result.videoInfo.durls?.firstOrNull {
                    it.quality == downloadViewInfo.selectVideoQualityId
                }?.durl?.first() ?: result.videoInfo.durls?.first()?.durl?.first()

            is BILIDonghuaPlayerSynthesize -> {
                result.dash
                    ?: result.durls?.firstOrNull {
                        it.quality == downloadViewInfo.selectVideoQualityId
                    }?.durl?.first() ?: result.durls?.first()?.durl?.first()
            }

            is BILIVideoPlayerInfo -> result.dash ?: result.durls?.firstOrNull {
                it.quality == downloadViewInfo.selectVideoQualityId
            }?.durl?.first() ?: result.durls?.first()?.durl?.first()

            else -> null
        }
    }

    /**
     * 从视频数据中获取下载URL
     */
    fun getDownloadUrl(
        videoData: Any,
        subTaskType: DownloadSubTaskType,
        downloadViewInfo: DownloadViewInfo
    ): String? {
        return when (videoData) {
            is BILIVideoDash -> {
                when (subTaskType) {
                    DownloadSubTaskType.VIDEO -> selectVideoQuality(
                        videoData.video,
                        downloadViewInfo
                    ).finalUrl

                    DownloadSubTaskType.AUDIO -> selectAudioQuality(
                        videoData,
                        downloadViewInfo
                    ).finalUrl
                }
            }

            is BILIVideoDurl -> {
                when (subTaskType) {
                    DownloadSubTaskType.VIDEO -> videoData.url
                    DownloadSubTaskType.AUDIO -> null
                }
            }

            else -> null
        }
    }

    /**
     * 选择视频质量
     */
    private fun selectVideoQuality(
        videos: List<BILIVideoDash.Video>,
        downloadViewInfo: DownloadViewInfo
    ): BILIVideoDash.Video {
        return videos.filter {
            it.id == downloadViewInfo.selectVideoQualityId
        }.firstOrNull {
            it.codecs.contains(downloadViewInfo.selectVideoCode)
        } ?: videos.firstOrNull() ?: throw IllegalStateException("无可用视频流")
    }

    /**
     * 选择音频质量
     */
    private fun selectAudioQuality(
        dash: BILIVideoDash,
        downloadViewInfo: DownloadViewInfo
    ): BILIVideoDash.Audio {
        return dash.audio.firstOrNull {
            it.id == downloadViewInfo.selectAudioQualityId
        } ?: dash.dolby?.audio?.firstOrNull {
            it.id == downloadViewInfo.selectAudioQualityId
        } ?: dash.flac?.audio?.takeIf {
            it.id == downloadViewInfo.selectAudioQualityId
        } ?: dash.audio.firstOrNull() ?: throw IllegalStateException("无可用音频流")
    }

    /**
     * 获取segment对应的bvId
     */
    private suspend fun getSegmentBvId(segment: DownloadSegment): String? {
        return if (segment.taskId != null) {
            val task = downloadTaskRepository.getTaskById(segment.taskId!!)
            task?.platformId
        } else {
            try {
                val platformInfo = json.decodeFromString<Map<String, Any>>(segment.platformInfo)
                platformInfo["bvid"] as? String
            } catch (e: Exception) {
                val node = downloadTaskRepository.getTaskByNodeId(segment.nodeId)
                if (node != null) {
                    val task = downloadTaskRepository.getTaskById(node.taskId)
                    task?.platformId
                } else null
            }
        }
    }
}
