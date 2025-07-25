package com.imcys.bilibilias.data.repository

import com.imcys.bilibilias.data.model.download.DownloadTaskTree
import com.imcys.bilibilias.data.model.download.DownloadTreeNode
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.database.dao.DownloadTaskDao
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadPlatform
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadTask
import com.imcys.bilibilias.database.entity.download.DownloadTaskNode
import com.imcys.bilibilias.database.entity.download.DownloadTaskNodeType
import com.imcys.bilibilias.database.entity.download.DownloadTaskType
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import kotlinx.coroutines.flow.last
import kotlinx.serialization.json.Json
import java.util.Date

class DownloadTaskRepository(
    private val json: Json,
    private val downloadTaskDao: DownloadTaskDao,
    private val videoInfoRepository: VideoInfoRepository
) {

    /**
     * 统一入口：根据链接类型创建下载任务
     */
    suspend fun createDownloadTask(
        asLinkResultType: ASLinkResultType,
        downloadViewInfo: DownloadViewInfo
    ): Result<DownloadTaskTree> {
        return when (asLinkResultType) {
            is ASLinkResultType.BILI.Donghua -> {
                createDonghuaDownloadTask(
                    downloadViewInfo.downloadMode,
                    asLinkResultType.currentEpId,
                    downloadViewInfo.selectedEpId
                )
            }

            is ASLinkResultType.BILI.Video -> {
                createVideoDownloadTask(
                    downloadViewInfo.downloadMode,
                    asLinkResultType.currentBvId,
                    downloadViewInfo.selectedCid
                )
            }

            else -> Result.failure(IllegalArgumentException("不支持的链接类型"))
        }
    }

    /**
     * 创建番剧下载任务
     */
    private suspend fun createDonghuaDownloadTask(
        downloadMode: DownloadMode,
        currentEpId: Long,
        selectedEpId: List<Long>
    ): Result<DownloadTaskTree> = runCatching {
        val donghuaInfo = videoInfoRepository.getDonghuaSeasonViewInfo(currentEpId).last()
        if (donghuaInfo.status != ApiStatus.SUCCESS) error("番剧接口异常")
        val data = donghuaInfo.data!!

        val task = getOrCreateTask(
            platformId = data.seasonId.toString(),
            title = data.title,
            cover = data.cover,
            type = DownloadTaskType.BILI_DONGHUA
        )

        val roots = buildDonghuaTree(task.taskId, data, selectedEpId, downloadMode)
        DownloadTaskTree(task = task, roots = roots)
    }

    /**
     * 创建视频下载任务
     */
    private suspend fun createVideoDownloadTask(
        downloadMode: DownloadMode,
        bvid: String,
        selectedCid: List<Long>
    ): Result<DownloadTaskTree> = runCatching {
        val videoInfo = videoInfoRepository.getVideoView(bvid).last()
        if (videoInfo.status != ApiStatus.SUCCESS) error("视频接口异常")
        val data = videoInfo.data!!

        // 判断是否为合集
        val isUgcSeason = !data.ugcSeason?.sections.isNullOrEmpty()

        val (task, taskType) = if (isUgcSeason) {
            // 合集
            getOrCreateTask(
                platformId = data.ugcSeason!!.id.toString(),
                title = data.ugcSeason!!.title,
                cover = data.ugcSeason!!.cover,
                type = DownloadTaskType.BILI_VIDEO_SECTION
            ) to DownloadTaskType.BILI_VIDEO_SECTION
        } else {
            // 普通视频
            getOrCreateTask(
                platformId = data.bvid,
                title = data.title,
                cover = data.pic,
                type = DownloadTaskType.BILI_VIDEO
            ) to DownloadTaskType.BILI_VIDEO
        }

        val roots = if (isUgcSeason) {
            buildUgcSeasonTree(task.taskId, data, selectedCid, downloadMode)
        } else {
            val pages = data.pages?.filter { selectedCid.contains(it.cid) }.orEmpty()
            if (pages.isEmpty()) {
                emptyList()
            } else {
                listOf(buildVideoPageTree(task.taskId,task, data, pages, downloadMode))
            }
        }

        DownloadTaskTree(task, roots)
    }

    /**
     * 获取或创建下载任务
     */
    private suspend fun getOrCreateTask(
        platformId: String,
        title: String,
        cover: String,
        type: DownloadTaskType
    ): DownloadTask {
        return downloadTaskDao.getTaskByPlatformId(platformId)?.copy(updateTime = Date())?.also {
            downloadTaskDao.updateTask(it)
        } ?: DownloadTask(
            title = title,
            platformId = platformId,
            downloadPlatform = DownloadPlatform.BILIBILI,
            cover = cover,
            type = type
        ).let {
            val taskId = downloadTaskDao.insertTask(it)
            it.copy(taskId = taskId)
        }
    }

    /**
     * 构建番剧树结构：season -> episode
     */
    private suspend fun buildDonghuaTree(
        taskId: Long,
        data: BILIDonghuaSeasonInfo,
        selectedEpId: List<Long>,
        downloadMode: DownloadMode
    ): List<DownloadTreeNode> {
        val roots = mutableListOf<DownloadTreeNode>()

        // 1. 构建季度节点
        data.seasons.forEach { season ->
            val episodes = if (season.seasonId == data.seasonId) {
                data.episodes
            } else {
                // 获取其他季度的剧集信息
                val seasonInfo =
                    videoInfoRepository.getDonghuaSeasonViewInfo(seasonId = season.seasonId).last()
                seasonInfo.data?.episodes ?: emptyList()
            }

            val filteredEpisodes = episodes.filter { selectedEpId.contains(it.epId) }
            if (filteredEpisodes.isNotEmpty()) {
                roots += buildSeasonNode(taskId, season, filteredEpisodes, downloadMode)
            }
        }

        // 2. 构建预告章节节点
        data.section.forEach { section ->
            val filteredEpisodes = section.episodes.filter { selectedEpId.contains(it.epId) }
            if (filteredEpisodes.isNotEmpty()) {
                roots += buildSectionNode(taskId, section, filteredEpisodes, downloadMode)
            }
        }

        return roots
    }

    /**
     * 构建合集树结构：section -> episode
     */
    private suspend fun buildUgcSeasonTree(
        taskId: Long,
        data: BILIVideoViewInfo,
        selectedCid: List<Long>,
        downloadMode: DownloadMode
    ): List<DownloadTreeNode> {
        return data.ugcSeason?.sections?.mapNotNull { section ->
            val filteredEpisodes = section.episodes.filter { selectedCid.contains(it.cid) }
            if (filteredEpisodes.isNotEmpty()) {
                buildUgcSectionNode(taskId, section, filteredEpisodes, downloadMode)
            } else null
        } ?: emptyList()
    }

    /**
     * 构建普通视频树结构：video -> page
     */
    private suspend fun buildVideoPageTree(
        taskId: Long,
        task: DownloadTask,
        data: BILIVideoViewInfo,
        pages: List<BILIVideoViewInfo.Page>,
        downloadMode: DownloadMode,
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = data.bvid,
            title = data.title,
            nodeType = DownloadTaskNodeType.BILI_VIDEO_PAGE,
            pic = data.pic
        )
        val segments = pages.map { page ->
            createSegment(
                nodeId = node.nodeId,
                cover = task.cover,
                title = page.part,
                platformId = page.cid.toString(),
                segmentOrder = page.page.toLong(),
                platformInfo = json.encodeToString(page),
                duration = page.duration,
                downloadMode = downloadMode,
                childTaskId = null  // 普通视频不需要子任务
            )
        }

        return DownloadTreeNode(node, segments, emptyList())
    }

    /**
     * 构建番剧季度节点
     */
    private suspend fun buildSeasonNode(
        taskId: Long,
        season: BILIDonghuaSeasonInfo.Season,
        episodes: List<BILIDonghuaSeasonInfo.Episode>,
        downloadMode: DownloadMode,
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = season.seasonId.toString(),
            title = season.seasonTitle,
            nodeType = DownloadTaskNodeType.BILI_DONGHUA_SEASON
        )

        val segments = episodes.map { episode ->

            createSegment(
                nodeId = node.nodeId,
                title = episode.longTitle.ifEmpty { episode.title },
                cover = episode.cover,
                platformId = episode.epId.toString(),
                segmentOrder = episode.longTitle.ifEmpty { episode.title }.filter { it.isDigit() }
                    .toLongOrNull() ?: 0L,
                platformInfo = json.encodeToString(episode),
                duration = episode.duration,
                downloadMode = downloadMode,
                childTaskId = null  // 番剧不需要子任务
            )
        }

        return DownloadTreeNode(node, segments, emptyList())
    }

    /**
     * 构建番剧预告章节节点
     */
    private suspend fun buildSectionNode(
        taskId: Long,
        section: BILIDonghuaSeasonInfo.Section,
        episodes: List<BILIDonghuaSeasonInfo.Episode>,
        downloadMode: DownloadMode,
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = section.id.toString(),
            title = section.title,
            nodeType = DownloadTaskNodeType.BILI_DONGHUA_SECTION
        )

        val segments = episodes.map { episode ->

            createSegment(
                nodeId = node.nodeId,
                title = episode.longTitle.ifEmpty { episode.title },
                cover = episode.cover,
                platformId = episode.epId.toString(),
                segmentOrder = 0L,
                platformInfo = json.encodeToString(episode),
                duration = episode.duration,
                downloadMode = downloadMode,
                childTaskId = null  // 番剧预告不需要子任务
            )
        }

        return DownloadTreeNode(node, segments, emptyList())
    }

    /**
     * 构建合集章节节点
     */
    private suspend fun buildUgcSectionNode(
        taskId: Long,
        section: BILIVideoViewInfo.UgcSeason.Section,
        episodes: List<BILIVideoViewInfo.UgcSeason.Section.Episode>,
        downloadMode: DownloadMode
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = section.id.toString(),
            title = section.title,
            nodeType = DownloadTaskNodeType.BILI_VIDEO_SECTION_EPISODES
        )

        val segments = episodes.map { episode ->
            // 为每个子视频创建独立的下载任务
            val childTask = getOrCreateTask(
                platformId = episode.bvid,
                title = episode.title,
                cover = episode.arc.pic,
                type = DownloadTaskType.BILI_VIDEO
            )

            // 创建关联到子任务的segment
            createSegment(
                nodeId = node.nodeId,
                title = episode.title,
                cover = episode.arc.pic,
                platformId = episode.cid.toString(),
                segmentOrder = episode.page?.page ?: 0L,
                platformInfo = json.encodeToString(episode),
                duration = episode.page?.duration,
                downloadMode = downloadMode,
                childTaskId = childTask.taskId  // 关联子任务ID
            )
        }

        return DownloadTreeNode(node, segments, emptyList())
    }

    /**
     * 获取或创建节点
     */
    private suspend fun getOrCreateNode(
        taskId: Long,
        platformId: String,
        title: String,
        nodeType: DownloadTaskNodeType,
        pic: String? = null
    ): DownloadTaskNode {
        return downloadTaskDao.getTaskNodeByTaskIdAndPlatformId(taskId, platformId)?.copy(
            updateTime = Date()
        )?.also {
            downloadTaskDao.updateNode(it)
        } ?: DownloadTaskNode(
            taskId = taskId,
            platformId = platformId,
            title = title,
            nodeType = nodeType,
            pic = pic
        ).let {
            val nodeId = downloadTaskDao.insertNode(it)
            it.copy(nodeId = nodeId)
        }
    }

    /**
     * 创建下载片段
     */
    private suspend fun createSegment(
        nodeId: Long,
        title: String,
        cover: String? = null,
        platformId: String,
        segmentOrder: Long,
        platformInfo: String,
        duration: Long?,
        downloadMode: DownloadMode,
        childTaskId: Long? = null  // 新增：子任务ID参数
    ): DownloadSegment {
        return downloadTaskDao.getSegmentByNodeIdAndPlatformId(nodeId, platformId)?.copy(
            title = title,
            cover = cover,
            segmentOrder = segmentOrder,
            platformInfo = platformInfo,
            updateTime = Date(),
            downloadMode = downloadMode,
            taskId = childTaskId  // 更新子任务关联
        )?.also {
            downloadTaskDao.updateSegment(it)
        } ?: DownloadSegment(
            nodeId = nodeId,
            taskId = childTaskId,  // 关联子任务
            title = title,
            cover = cover,
            segmentOrder = segmentOrder,
            platformId = platformId,
            platformInfo = platformInfo,
            duration = duration,
            downloadMode = downloadMode,
            savePath = "",
            fileSize = 0
        ).let {
            val segmentId = downloadTaskDao.insertSegment(it)
            it.copy(segmentId = segmentId)
        }
    }


    // ================== 数据库操作方法 ==================

    suspend fun getTaskByPlatformId(platformId: String): DownloadTask? =
        downloadTaskDao.getTaskByPlatformId(platformId)

    suspend fun getTaskById(taskId: Long): DownloadTask? =
        downloadTaskDao.getTaskById(taskId)


    suspend fun getTaskByNodeId(nodeId: Long): DownloadTask? =
        downloadTaskDao.getTaskByNodeId(nodeId)


    suspend fun getTaskNodeByTaskIdAndPlatformId(
        taskId: Long,
        platformId: String
    ): DownloadTaskNode? =
        downloadTaskDao.getTaskNodeByTaskIdAndPlatformId(taskId, platformId)

    suspend fun getSegmentByNodeIdAndPlatformId(
        nodeId: Long,
        platformId: String
    ): DownloadSegment? =
        downloadTaskDao.getSegmentByNodeIdAndPlatformId(nodeId, platformId)

    fun getSegmentAll() =
        downloadTaskDao.getSegmentAll()


    suspend fun insertTask(task: DownloadTask): Long =
        downloadTaskDao.insertTask(task)

    suspend fun insertNode(node: DownloadTaskNode): Long =
        downloadTaskDao.insertNode(node)

    suspend fun insertSegment(segment: DownloadSegment): Long =
        downloadTaskDao.insertSegment(segment)

    suspend fun updateTask(task: DownloadTask) =
        downloadTaskDao.updateTask(task)

    suspend fun updateNode(node: DownloadTaskNode) =
        downloadTaskDao.updateNode(node)

    suspend fun updateSegment(segment: DownloadSegment) =
        downloadTaskDao.updateSegment(segment)


    suspend fun deleteTask(taskId: Long) =
        downloadTaskDao.deleteTask(taskId)

    suspend fun deleteNode(nodeId: Long) =
        downloadTaskDao.deleteNode(nodeId)

    suspend fun deleteSegment(segmentId: Long) =
        downloadTaskDao.deleteSegment(segmentId)

}