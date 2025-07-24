package com.imcys.bilibilias.dwonload

import com.imcys.bilibilias.data.model.download.DownloadSubTask
import com.imcys.bilibilias.data.model.download.DownloadTaskTree
import com.imcys.bilibilias.data.model.download.DownloadTreeNode
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadStage
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.database.entity.download.DownloadSubTaskType
import com.imcys.bilibilias.database.entity.download.DownloadTaskNodeType
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json


class DownloadManager(
    private val downloadTaskRepository: DownloadTaskRepository,
    private val videoInfoRepository: VideoInfoRepository,
    private val json: Json
) {

    // 内存中的下载任务存储，不持久化子任务
    private val _downloadTasks = MutableStateFlow<List<AppDownloadTask>>(emptyList())

    private var isInit = false

    /**
     * 初始化下载列表，将所有任务状态设为暂停
     */
    suspend fun initDownloadList() {
        if (isInit) return
        isInit = true

        // 没下载完成的就直接删掉
        val segments = downloadTaskRepository.getSegmentAll().last()
        segments.forEach { segment ->
            // 更新数据库中的状态为暂停
            if (segment.downloadState != DownloadState.PAUSE &&
                segment.downloadState != DownloadState.COMPLETED
            ) {
                downloadTaskRepository.deleteSegment(segment.segmentId)
            }
        }
    }

    /**
     * 获取所有下载任务的Flow
     */
    fun getAllDownloadTasks(): StateFlow<List<AppDownloadTask>> {
        return _downloadTasks.asStateFlow()
    }

    /**
     * 获取指定任务
     */
    fun getDownloadTask(segmentId: Long): Flow<AppDownloadTask?> {
        return _downloadTasks.map { tasks ->
            tasks.find { it.downloadSegment.segmentId == segmentId }
        }
    }


    /**
     * 添加下载任务
     */
    suspend fun addDownloadTask(
        asLinkResultType: ASLinkResultType,
        downloadViewInfo: DownloadViewInfo
    ) {
        // 使用重构后的API创建任务结构
        val taskResult =
            downloadTaskRepository.createDownloadTask(asLinkResultType, downloadViewInfo)

        taskResult.onSuccess { taskTree ->
            // 将新创建的segments添加到内存中
            processDownloadTree(taskTree, downloadViewInfo)
        }.onFailure { error ->
            // 处理错误，可以记录日志或通知用户
            throw error
        }
    }

    /**
     * 处理下载任务树 - 将新segment添加到内存管理
     */
    private suspend fun processDownloadTree(
        taskTree: DownloadTaskTree,
        downloadViewInfo: DownloadViewInfo
    ) {
        val currentTasks = _downloadTasks.value.toMutableList()

        // 递归处理所有节点的segments
        suspend fun processNode(node: DownloadTreeNode) {
            node.segments.forEach lastCheck@{ segment ->
                // 检查是否已存在，避免重复添加
                if (!currentTasks.any{ it.downloadSegment.platformId == segment.platformId}) {
                    val downloadSubTasks =
                        createSubTasksForSegment(segment, node.node.nodeType, downloadViewInfo)
                    var cover = segment.cover
                    if (segment.taskId != null && segment.taskId != 0L) {
                        cover = downloadTaskRepository.getTaskById(segment.taskId!!)?.cover
                    }
                    val newTask = AppDownloadTask(
                        downloadTask = taskTree.task,
                        downloadSegment = segment,
                        downloadSubTasks = downloadSubTasks,
                        downloadStage = DownloadStage.DOWNLOAD,
                        cover = cover
                    )
                    currentTasks.add(newTask)
                }
            }
            node.children.forEach { childNode ->
                processNode(childNode)
            }
        }

        taskTree.roots.forEach { rootNode ->
            processNode(rootNode)
        }

        _downloadTasks.value = currentTasks
    }


    private suspend fun createSubTasksForSegment(
        segment: DownloadSegment,
        nodeType: DownloadTaskNodeType,
        downloadViewInfo: DownloadViewInfo
    ): List<DownloadSubTask> {
        // 获取视频播放信息
        val videoInfo = when (nodeType) {
            DownloadTaskNodeType.BILI_VIDEO_PAGE,
            DownloadTaskNodeType.BILI_VIDEO_SECTION_EPISODES -> {
                videoInfoRepository.getVideoPlayerInfo(
                    cid = segment.platformId.toLong(),
                    bvId = getSegmentBvId(segment)
                ).last()
            }

            DownloadTaskNodeType.BILI_DONGHUA_SEASON,
            DownloadTaskNodeType.BILI_DONGHUA_SECTION -> {
                videoInfoRepository.getDonghuaPlayerInfo(
                    epId = segment.platformId.toLong(),
                    null
                ).last()
            }

            else -> throw IllegalStateException("缓存类型异常")
        }

        if (videoInfo.status != ApiStatus.SUCCESS) {
            throw IllegalStateException("视频接口异常")
        }

        val videoData = when (val result = videoInfo.data) {
            is BILIDonghuaPlayerInfo -> result.dash
            is BILIVideoPlayerInfo -> result.dash
            else -> null
        } ?: return emptyList()

        // 根据segment的下载模式选择流
        val subTasks = when (segment.downloadMode) {
            DownloadMode.AUDIO_VIDEO -> {
                val selectedVideo = selectVideoQuality(videoData.video, downloadViewInfo)
                val selectedAudio = selectAudioQuality(videoData, downloadViewInfo)
                listOf(
                    createSubTask(segment, selectedVideo.baseUrl, DownloadSubTaskType.VIDEO),
                    createSubTask(segment, selectedAudio.baseUrl, DownloadSubTaskType.AUDIO)
                )
            }

            DownloadMode.VIDEO_ONLY -> {
                val selectedVideo = selectVideoQuality(videoData.video, downloadViewInfo)
                listOf(
                    createSubTask(segment, selectedVideo.baseUrl, DownloadSubTaskType.VIDEO)
                )
            }

            DownloadMode.AUDIO_ONLY -> {
                val selectedAudio = selectAudioQuality(videoData, downloadViewInfo)
                listOf(
                    createSubTask(segment, selectedAudio.baseUrl, DownloadSubTaskType.AUDIO)
                )
            }
        }

        delay(500L) // 避免请求过快
        return subTasks
    }

    /**
     * 获取segment对应的bvId
     */
    private suspend fun getSegmentBvId(segment: DownloadSegment): String? {
        // 如果segment有关联的taskId，优先使用关联task的platformId
        return if (segment.taskId != null) {
            val task = downloadTaskRepository.getTaskById(segment.taskId!!)
            task?.platformId
        } else {
            // 对于番剧，需要通过epId获取对应的bvid
            // 这里需要根据实际的数据结构来解析platformInfo
            try {
                val platformInfo = json.decodeFromString<Map<String, Any>>(segment.platformInfo)
                platformInfo["bvid"] as? String ?: extractBvidFromPlatformInfo(segment)
            } catch (e: Exception) {
                // fallback: 通过nodeId找到对应的task
                val node = downloadTaskRepository.getTaskByNodeId(segment.nodeId)
                if (node != null) {
                    val task = downloadTaskRepository.getTaskById(node.taskId)
                    task?.platformId
                } else null
            }
        }
    }

    /**
     * 从平台信息中提取bvid（用于番剧等特殊情况）
     */
    private fun extractBvidFromPlatformInfo(segment: DownloadSegment): String? {
        return try {
            // 尝试解析JSON，获取cid对应的bvid
            // 这里可能需要调用额外的API来获取番剧episode对应的bvid
            null // 暂时返回null，实际使用时需要根据具体的API来实现
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 选择视频质量 - 根据用户偏好选择
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
     * 选择音频质量 - 根据用户偏好选择
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


    private fun createSubTask(
        segment: DownloadSegment,
        url: String,
        type: DownloadSubTaskType
    ) = DownloadSubTask(
        segmentId = segment.segmentId,
        savePath = "",          // 由下载器或后续流程再填充
        downloadUrl = url,
        subTaskType = type,
        downloadState = DownloadState.WAITING
    )
}
