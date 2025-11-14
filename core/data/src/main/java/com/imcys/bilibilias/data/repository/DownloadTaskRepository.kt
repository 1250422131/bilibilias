package com.imcys.bilibilias.data.repository

import com.imcys.bilibilias.common.data.commonAnalyticsSafe
import com.imcys.bilibilias.data.model.download.DownloadTaskTree
import com.imcys.bilibilias.data.model.download.DownloadTreeNode
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.dao.DownloadTaskDao
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadPlatform
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadTask
import com.imcys.bilibilias.database.entity.download.DownloadTaskNode
import com.imcys.bilibilias.database.entity.download.DownloadTaskNodeType
import com.imcys.bilibilias.database.entity.download.DownloadTaskType
import com.imcys.bilibilias.database.entity.download.NamingConventionInfo
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import com.imcys.bilibilias.network.model.video.filterWithSinglePage
import com.imcys.bilibilias.network.service.AppAPIService
import kotlinx.coroutines.flow.last
import kotlinx.serialization.json.Json
import java.util.Date
import kotlin.text.ifEmpty

class DownloadTaskRepository(
    private val json: Json,
    private val downloadTaskDao: DownloadTaskDao,
    private val videoInfoRepository: VideoInfoRepository,
    private val appAPIService: AppAPIService,
    private val appSettingsRepository: AppSettingsRepository,
    private val usersDataSource: UsersDataSource,
    private val biliUsersDao: BILIUsersDao
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
     * 提交下载数据到后台备份，统一处理隐私协议和用户信息
     */
    private suspend fun submitDownloadAnalytics(
        aid: Long,
        bvid: String,
        copyright: Int,
        tName: String,
        upName: String,
        mid: Long
    ) {
        commonAnalyticsSafe {
            if (!appSettingsRepository.hasAgreedPrivacyPolicy()) return@commonAnalyticsSafe

            var userName: String? = null
            var userId: Long? = null
            if (usersDataSource.isLogin()) {
                val userInfo = biliUsersDao.getBILIUserByUid(usersDataSource.getUserId())
                userName = userInfo?.name
                userId = userInfo?.mid
            }
            appAPIService.submitASDownloadData(
                aid = aid,
                bvid = bvid,
                copy = copyright,
                tName = tName,
                upName = upName,
                mid = mid,
                userName = userName,
                userId = userId
            )
        }
    }

    private suspend fun <T> autoRequestRetry(
        onErrorTip: (NetWorkResult<T?>?) -> String,
        block: suspend () -> FlowNetWorkResult<T>
    ): T? {
        var count = 1
        var lastResult: NetWorkResult<T?>? = null
        while (count < 3) {
            val result = block().last()
            lastResult = result
            if (result.status == ApiStatus.SUCCESS) {
                return result.data
            }
            count++
        }
        error(onErrorTip(lastResult))
    }

    /**
     * 创建番剧下载任务
     */
    private suspend fun createDonghuaDownloadTask(
        downloadMode: DownloadMode,
        currentEpId: Long,
        selectedEpId: List<Long>
    ): Result<DownloadTaskTree> = runCatching {
        val donghuaInfo = autoRequestRetry(onErrorTip = { donghuaInfo ->
            "番剧接口异常:${donghuaInfo?.errorMsg}"
        }) { videoInfoRepository.getDonghuaSeasonViewInfo(currentEpId) }
        val data = donghuaInfo!!

        val task = getOrCreateTask(
            platformId = data.seasonId.toString(),
            title = data.title,
            description = data.evaluate,
            cover = data.cover,
            type = DownloadTaskType.BILI_DONGHUA
        )

        val namingConventionInfo = NamingConventionInfo.Donghua(
            title = data.title,
        )
        val roots =
            buildDonghuaTree(task.taskId, data, selectedEpId, downloadMode, namingConventionInfo)


        // 提交下载数据到后台备份
        if (data.episodes.firstOrNull()?.aid != null) {
            runCatching {
                val videoInfo =
                    autoRequestRetry(onErrorTip = { "网络异常，提交视频信息获取失败" }) {
                        videoInfoRepository.getVideoView(
                            aid = data.episodes.firstOrNull().toString()
                        )
                    }
                val videoData = videoInfo!!
                submitDownloadAnalytics(
                    aid = videoData.aid,
                    bvid = videoData.bvid,
                    copyright = videoData.copyright.toInt(),
                    tName = videoData.tname,
                    upName = videoData.owner.name,
                    mid = videoData.owner.mid
                )
            }
        }

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
        if (videoInfo.status != ApiStatus.SUCCESS) error("视频接口异常:${videoInfo.errorMsg}")
        val data = videoInfo.data!!

        // 判断是否为合集
        val isUgcSeason = !data.ugcSeason?.sections.isNullOrEmpty()
        val isSteinGate = data.rights?.isSteinGate != 0L

        val (task, taskType) = when {
            isUgcSeason ->// 合集
                getOrCreateTask(
                    platformId = data.ugcSeason!!.id.toString(),
                    title = data.ugcSeason!!.title,
                    description = data.desc,
                    cover = data.ugcSeason!!.cover,
                    type = DownloadTaskType.BILI_VIDEO_SECTION
                ) to DownloadTaskType.BILI_VIDEO_SECTION

            else -> {
                // 普通视频
                getOrCreateTask(
                    platformId = data.bvid,
                    title = data.title,
                    description = data.desc,
                    cover = data.pic,
                    type = DownloadTaskType.BILI_VIDEO
                ) to DownloadTaskType.BILI_VIDEO
            }
        }

        val namingConventionInfo = when {
            isUgcSeason -> {
                NamingConventionInfo.Video(
                    title = data.ugcSeason!!.title,
                )
            }

            else -> {
                NamingConventionInfo.Video(
                    title = data.title,
                )
            }
        }.copy(
            bvId = data.bvid,
            aid = data.aid.toString(),
            author = data.owner.name
        )

        val roots = when {
            isUgcSeason -> {
                buildUgcSeasonTree(
                    task.taskId,
                    task,
                    data,
                    selectedCid,
                    downloadMode,
                    namingConventionInfo
                )
            }

            isSteinGate -> {
                buildSteinGateTree(
                    task.taskId,
                    task,
                    data,
                    selectedCid,
                    downloadMode,
                    namingConventionInfo
                )
            }

            else -> {
                val pages = data.pages?.filter { selectedCid.contains(it.cid) }.orEmpty()
                if (pages.isEmpty()) {
                    emptyList()
                } else {
                    listOf(
                        buildVideoPageTree(
                            task.taskId,
                            task,
                            data,
                            pages,
                            downloadMode,
                            namingConventionInfo = namingConventionInfo,
                            allPages = data.pages ?: emptyList()
                        )
                    )
                }
            }
        }

        // 提交下载数据到后台备份
        submitDownloadAnalytics(
            aid = data.aid,
            bvid = data.bvid,
            copyright = data.copyright.toInt(),
            tName = data.tname,
            upName = data.owner.name,
            mid = data.owner.mid
        )

        DownloadTaskTree(task, roots)
    }

    /**
     * 获取或创建下载任务
     */
    private suspend fun getOrCreateTask(
        platformId: String,
        title: String,
        description: String,
        cover: String,
        type: DownloadTaskType
    ): DownloadTask {
        return downloadTaskDao.getTaskByPlatformId(platformId)?.copy(updateTime = Date())?.also {
            downloadTaskDao.updateTask(it)
        } ?: DownloadTask(
            title = title,
            description = description,
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
        downloadMode: DownloadMode,
        namingConventionInfo: NamingConventionInfo.Donghua
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
                roots += buildSeasonNode(
                    taskId,
                    season,
                    filteredEpisodes,
                    downloadMode,
                    namingConventionInfo,
                    episodes,
                )
            }
        }

        // 2. 构建预告章节节点
        data.section.forEach { section ->
            val filteredEpisodes = section.episodes.filter { selectedEpId.contains(it.epId) }
            if (filteredEpisodes.isNotEmpty()) {
                roots += buildSectionNode(
                    taskId,
                    section,
                    filteredEpisodes,
                    downloadMode,
                    namingConventionInfo
                )
            }
        }


        // 3. 构建特殊的正片
        val epList = data.episodes.filter { selectedEpId.contains(it.epId) }
        if (epList.isNotEmpty()) {
            roots += buildNoeEpisodeNode(taskId, data, epList, downloadMode, namingConventionInfo)
        }
        return roots
    }

    /**
     * 构建合集树结构：section -> episode
     */
    private suspend fun buildUgcSeasonTree(
        taskId: Long,
        task: DownloadTask,
        data: BILIVideoViewInfo,
        selectedCid: List<Long>,
        downloadMode: DownloadMode,
        namingConventionInfo: NamingConventionInfo.Video
    ): List<DownloadTreeNode> {


        return data.ugcSeason?.sections?.mapNotNull { section ->
            // 视频章节
            val filteredEpisodes = section.episodes
                .filter {
                    if (it.pages.isNotEmpty()) it.pages.any { page -> page.cid in selectedCid } else selectedCid.contains(
                        it.cid
                    )
                }
            if (filteredEpisodes.isNotEmpty()) {
                buildUgcSectionNode(
                    taskId,
                    task,
                    section,
                    filteredEpisodes,
                    downloadMode,
                    selectedCid,
                    namingConventionInfo,
                )
            } else null
        } ?: emptyList()
    }

    /**
     * 构建互动视频树树结构：node -> node -> page
     * 暂未实现节点绑定
     */
    private suspend fun DownloadTaskRepository.buildSteinGateTree(
        taskId: Long,
        task: DownloadTask,
        data: BILIVideoViewInfo,
        selectedCid: List<Long>,
        downloadMode: DownloadMode,
        namingConventionInfo: NamingConventionInfo.Video
    ): List<DownloadTreeNode> {
        val playerInfoV2 = videoInfoRepository.getVideoPlayerInfoV2(
            cid = data.pages?.firstOrNull()?.cid ?: 0,
            bvId = data.bvid,
            aid = data.aid
        ).last()
        val steinGateInfo = videoInfoRepository.getSteinEdgeInfoV2(
            bvId = data.bvid,
            aid = data.aid.toString(),
            graphVersion = playerInfoV2.data?.interaction?.graphVersion ?: 0L
        ).last()

        val nodeList = mutableListOf<DownloadTreeNode>()

        steinGateInfo.data?.storyList?.filter { it.cid in selectedCid }
            ?.forEach { story ->
                val node = getOrCreateNode(
                    taskId = taskId,
                    platformId = story.nodeId.toString(),
                    title = story.title,
                    nodeType = DownloadTaskNodeType.BILI_VIDEO_INTERACTIVE,
                    pic = story.cover,
                )

                val mNamingConvention = namingConventionInfo.copy(
                    pTitle = story.title,
                    p = ((steinGateInfo.data?.storyList?.indexOf(story) ?: 1)).toString(),
                    cid = story.cid.toString(),
                )

                val segment = createSegment(
                    nodeId = node.nodeId,
                    title = story.title,
                    cover = story.cover,
                    platformId = story.cid.toString(),
                    platformUniqueId = story.cid.toString(),
                    segmentOrder = 0L,
                    platformInfo = json.encodeToString(story),
                    duration = 0,
                    downloadMode = downloadMode,
                    mNamingConventionInfo = mNamingConvention  // 互动视频不需要子任务
                )

                nodeList.add(
                    DownloadTreeNode(
                        node = node,
                        segments = listOf(segment),
                        children = emptyList()
                    )
                )
            }


        return nodeList

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
        parentNodeId: Long? = null,
        childTaskId: Long? = null,
        namingConventionInfo: NamingConventionInfo.Video,
        allPages: List<BILIVideoViewInfo.Page>
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = data.bvid,
            title = data.title,
            nodeType = DownloadTaskNodeType.BILI_VIDEO_PAGE,
            pic = data.pic,
            parentNodeId = parentNodeId
        )
        val segments = pages.map { page ->

            val mNamingConvention = namingConventionInfo.copy(
                pTitle = page.part,
                p = (allPages.indexOf(page) + 1).toString(),
                cid = page.cid.toString(),
            )

            createSegment(
                nodeId = node.nodeId,
                title = page.part,
                cover = task.cover,
                platformId = page.cid.toString(),
                platformUniqueId = page.cid.toString(),
                segmentOrder = page.page.toLong(),
                platformInfo = json.encodeToString(page),
                duration = page.duration,
                downloadMode = downloadMode,
                childTaskId = childTaskId,
                mNamingConventionInfo = mNamingConvention  // 普通视频不需要子任务
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
        namingConventionInfo: NamingConventionInfo.Donghua,
        allEpisodes: List<BILIDonghuaSeasonInfo.Episode>,
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = season.seasonId.toString(),
            title = season.seasonTitle,
            nodeType = DownloadTaskNodeType.BILI_DONGHUA_SEASON
        )

        val segments = episodes.map { episode ->

            val mNamingConvention = namingConventionInfo.copy(
                episodeTitle = episode.longTitle.ifEmpty { episode.title },
                cid = episode.cid.toString(),
                episodeNumber = (allEpisodes.indexOf(episode) + 1).toString()
            )

            createSegment(
                nodeId = node.nodeId,
                title = episode.longTitle.ifEmpty { episode.title },
                cover = episode.cover,
                platformId = episode.epId.toString(),
                platformUniqueId = episode.cid.toString(),
                segmentOrder = episode.longTitle.ifEmpty { episode.title }.filter { it.isDigit() }
                    .toLongOrNull() ?: 0L,
                platformInfo = json.encodeToString(episode),
                duration = episode.duration,
                downloadMode = downloadMode,
                mNamingConventionInfo = mNamingConvention  // 番剧不需要子任务
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
        namingConventionInfo: NamingConventionInfo.Donghua,
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = section.id.toString(),
            title = section.title,
            nodeType = DownloadTaskNodeType.BILI_DONGHUA_SECTION
        )
        val segments = episodes.map { episode ->
            val mNamingConvention = namingConventionInfo.copy(
                episodeNumber = (section.episodes.indexOf(episode) + 1).toString(),
                episodeTitle = episode.longTitle.ifEmpty { episode.title },
                cid = episode.cid.toString(),
            )

            createSegment(
                nodeId = node.nodeId,
                title = episode.longTitle.ifEmpty { episode.title },
                cover = episode.cover,
                platformId = episode.epId.toString(),
                platformUniqueId = episode.cid.toString(),
                segmentOrder = 0L,
                platformInfo = json.encodeToString(episode),
                duration = episode.duration,
                downloadMode = downloadMode,
                mNamingConventionInfo = mNamingConvention  // 番剧预告不需要子任务
            )
        }

        return DownloadTreeNode(node, segments, emptyList())
    }


    /**
     * 构建番剧正片节点
     */
    private suspend fun buildNoeEpisodeNode(
        taskId: Long,
        data: BILIDonghuaSeasonInfo,
        episodes: List<BILIDonghuaSeasonInfo.Episode>,
        downloadMode: DownloadMode,
        namingConventionInfo: NamingConventionInfo.Donghua
    ): DownloadTreeNode {

        val node = getOrCreateNode(
            taskId = taskId,
            platformId = "${data.seasonId}",
            title = data.title,
            nodeType = DownloadTaskNodeType.BILI_DONGHUA_EPISOD
        )

        val segments = episodes.map { episode ->
            val mNamingConvention = namingConventionInfo.copy(
                episodeNumber = (data.episodes.indexOf(episode) + 1).toString(),
                episodeTitle = episode.longTitle.ifEmpty { episode.title },
                cid = episode.cid.toString(),
            )

            createSegment(
                nodeId = node.nodeId,
                title = episode.longTitle.ifEmpty { episode.title },
                cover = episode.cover,
                platformId = episode.epId.toString(),
                platformUniqueId = episode.cid.toString(),
                segmentOrder = 0L,
                platformInfo = json.encodeToString(episode),
                duration = episode.duration,
                downloadMode = downloadMode,
                mNamingConventionInfo = mNamingConvention  // 番剧正片不需要子任务
            )
        }

        return DownloadTreeNode(node, segments, emptyList())
    }

    /**
     * 构建合集章节节点
     */
    private suspend fun buildUgcSectionNode(
        taskId: Long,
        task: DownloadTask,
        section: BILIVideoViewInfo.UgcSeason.Section,
        episodes: List<BILIVideoViewInfo.UgcSeason.Section.Episode>,
        downloadMode: DownloadMode,
        selectedCid: List<Long>,
        namingConventionInfo: NamingConventionInfo.Video
    ): DownloadTreeNode {
        val node = getOrCreateNode(
            taskId = taskId,
            platformId = section.id.toString(),
            title = section.title,
            nodeType = DownloadTaskNodeType.BILI_VIDEO_SECTION_EPISODES
        )

        val pageEpList = episodes.mapNotNull { episode ->

            if (episode.pages.size > 1) {
                val videoInfo = videoInfoRepository.getVideoView(episode.bvid).last().data
                if (videoInfo == null) throw IllegalStateException("合集分P视频信息获取失败: bv：${episode.bvid} cid：${episode.cid}")
                // 普通视频
                val newTask = getOrCreateTask(
                    platformId = videoInfo.bvid,
                    title = videoInfo.title,
                    description = videoInfo.desc,
                    cover = videoInfo.pic,
                    type = DownloadTaskType.BILI_VIDEO
                )

                buildVideoPageTree(
                    task.taskId,
                    task,
                    videoInfo,
                    episode.pages.filter { page -> page.cid in selectedCid },
                    downloadMode,
                    parentNodeId = node.nodeId,
                    childTaskId = newTask.taskId,  // 关联子任务ID
                    namingConventionInfo,
                    episode.pages,
                ).segments

            } else null
        }

        val segments = episodes.filterWithSinglePage().map { episode ->

            // 为每个子视频创建独立的下载任务
            val childTask = getOrCreateTask(
                platformId = episode.bvid,
                title = episode.title,
                cover = episode.arc.pic,
                description = episode.arc.desc,
                type = DownloadTaskType.BILI_VIDEO
            )

            val mNamingConvention = namingConventionInfo.copy(
                pTitle = episode.title,
                p = (section.episodes.indexOf(episode) + 1).toString(),
                cid = episode.cid.toString(),
            )

            // 创建关联到子任务的segment
            createSegment(
                nodeId = node.nodeId,
                title = episode.title,
                cover = episode.arc.pic,
                platformId = episode.cid.toString(),
                platformUniqueId = episode.cid.toString(),
                segmentOrder = episode.page?.page ?: 0L,
                platformInfo = json.encodeToString(episode),
                duration = episode.page?.duration,
                downloadMode = downloadMode,
                childTaskId = childTask.taskId,  // 关联子任务ID
                mNamingConvention
            )
        }

        return DownloadTreeNode(node, segments + pageEpList.flatten(), emptyList())
    }

    /**
     * 获取或创建节点
     */
    private suspend fun getOrCreateNode(
        taskId: Long,
        platformId: String,
        title: String,
        nodeType: DownloadTaskNodeType,
        pic: String? = null,
        parentNodeId: Long? = null
    ): DownloadTaskNode {
        return downloadTaskDao.getTaskNodeByTaskIdAndPlatformId(taskId, platformId)?.copy(
            updateTime = Date()
        )?.also {
            downloadTaskDao.updateNode(it)
        } ?: DownloadTaskNode(
            parentNodeId = parentNodeId,
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
        platformUniqueId: String,
        segmentOrder: Long,
        platformInfo: String,
        duration: Long?,
        downloadMode: DownloadMode,
        childTaskId: Long? = null,  // 新增：子任务ID参数
        mNamingConventionInfo: NamingConventionInfo
    ): DownloadSegment {
        return downloadTaskDao.getSegmentByNodeIdAndPlatformId(nodeId, platformId)?.copy(
            title = title,
            cover = cover,
            segmentOrder = segmentOrder,
            platformUniqueId = platformUniqueId,
            platformInfo = platformInfo,
            updateTime = Date(),
            downloadMode = downloadMode,
            taskId = childTaskId,  // 更新子任务关联
            namingConventionInfo = mNamingConventionInfo
        )?.also {
            downloadTaskDao.updateSegment(it)
        } ?: DownloadSegment(
            nodeId = nodeId,
            taskId = childTaskId,  // 关联子任务
            title = title,
            cover = cover,
            segmentOrder = segmentOrder,
            platformId = platformId,
            platformUniqueId = platformUniqueId,
            platformInfo = platformInfo,
            duration = duration,
            downloadMode = downloadMode,
            savePath = "",
            fileSize = 0,
            namingConventionInfo = mNamingConventionInfo
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
