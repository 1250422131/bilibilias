package com.imcys.bilibilias.download

import android.Manifest
import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.imcys.bilibilias.common.utils.download.DanmakuXmlUtil
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.download.DownloadSubTask
import com.imcys.bilibilias.data.model.download.DownloadTaskTree
import com.imcys.bilibilias.data.model.download.DownloadTreeNode
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.download.MediaContainerConfig
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadStage
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.database.entity.download.DownloadSubTaskType
import com.imcys.bilibilias.database.entity.download.DownloadTaskNodeType
import com.imcys.bilibilias.database.entity.download.DownloadTaskType
import com.imcys.bilibilias.download.service.DownloadService
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 新的下载管理器 - 使用重构后的组件
 */
class NewDownloadManager(
    private val context: Application,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val videoInfoRepository: VideoInfoRepository,
    private val httpClient: HttpClient,
    private val okHttpClient: OkHttpClient,
    private val appSettingsRepository: AppSettingsRepository,
    // 重构后的组件
    private val videoInfoFetcher: VideoInfoFetcher,
    private val fileOutputManager: FileOutputManager,
    private val downloadExecutor: DownloadExecutor,
    private val ffmpegMerger: FfmpegMerger,
    private val namingConventionHandler: NamingConventionHandler,
    private val subtitleDownloader: SubtitleDownloader
) {
    companion object {
        private const val MAX_CONCURRENT_DOWNLOADS = 1
        private const val QUEUE_CHECK_INTERVAL_MS = 1000L
    }

    private val _downloadTasks = MutableStateFlow<List<AppDownloadTask>>(emptyList())
    private var isInit = false
    private var isDownloading = false
    private val activeDownloadJobs = ConcurrentHashMap<Long, Job>()
    private val downloadScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var downloadService: DownloadService? = null
    private val downloadConn = object : ServiceConnection {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            val binder = iBinder as DownloadService.DownloadBinder
            downloadService = binder.service
            GlobalScope.launch(Dispatchers.IO) {
                binder.service?.let { startDownloadQueue(it) }
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            downloadService = null
        }
    }

    suspend fun initDownloadList() {
        if (isInit) return
        isInit = true

        val segments = downloadTaskRepository.getSegmentAll().last()
        segments.forEach { segment ->
            if (segment.downloadState !in listOf(DownloadState.PAUSE, DownloadState.COMPLETED)) {
                downloadTaskRepository.deleteSegment(segment.segmentId)
            }
        }
    }

    fun getAllDownloadTasks(): StateFlow<List<AppDownloadTask>> = _downloadTasks.asStateFlow()

    fun getDownloadTask(segmentId: Long): Flow<AppDownloadTask?> {
        return _downloadTasks.map { tasks ->
            tasks.find { it.downloadSegment.segmentId == segmentId }
        }
    }

    suspend fun addDownloadTask(
        asLinkResultType: ASLinkResultType,
        downloadViewInfo: DownloadViewInfo
    ) {
        val taskResult =
            downloadTaskRepository.createDownloadTask(asLinkResultType, downloadViewInfo)

        taskResult.onSuccess { taskTree ->
            processDownloadTree(taskTree, downloadViewInfo)
        }.onFailure { error ->
            throw error
        }

        if (!isDownloading) {
            startDownloadQueueService()
        }
    }

    suspend fun pauseTask(segmentId: Long) {
        val task = findTaskById(segmentId) ?: return
        if (task.downloadState != DownloadState.DOWNLOADING) return

        cancelActiveJob(segmentId)
        updateTaskState(task, DownloadState.PAUSE)
        downloadTaskRepository.updateSegment(task.downloadSegment.copy(downloadState = DownloadState.PAUSE))
    }

    suspend fun cancelTask(segmentId: Long) {
        val task = findTaskById(segmentId) ?: return

        cancelActiveJob(segmentId)
        deleteTaskFiles(task)
        updateTaskState(task, DownloadState.CANCELLED)
        downloadTaskRepository.updateSegment(task.downloadSegment.copy(downloadState = DownloadState.CANCELLED))
        removeTaskFromList(segmentId)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    suspend fun resumeTask(segmentId: Long) {
        val task = findTaskById(segmentId) ?: return
        if (task.downloadState != DownloadState.PAUSE) return

        updateTaskState(task, DownloadState.WAITING)
        downloadTaskRepository.updateSegment(task.downloadSegment.copy(downloadState = DownloadState.WAITING))

        if (!isDownloading) {
            isDownloading = true
        }
        checkAndStartNextDownload()
    }

    suspend fun pauseAllTasks() {
        val downloadingTasks = _downloadTasks.value.filter {
            it.downloadState in listOf(DownloadState.DOWNLOADING, DownloadState.MERGING)
        }
        downloadingTasks.forEach { pauseTask(it.downloadSegment.segmentId) }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    suspend fun resumeAllTasks() {
        val pausedTasks = _downloadTasks.value.filter { it.downloadState == DownloadState.PAUSE }
        pausedTasks.forEach { resumeTask(it.downloadSegment.segmentId) }
    }

    suspend fun downloadImageToAlbum(imageUrl: String, fileName: String, saveDirName: String) =
        withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(
                okhttp3.Request.Builder().url(imageUrl).build()
            ).execute()

            if (!response.isSuccessful) return@withContext

            val body = response.body ?: return@withContext
            val imageBytes = body.bytes()

            fileOutputManager.downloadImageToAlbum(imageBytes, fileName, saveDirName)
        }

    fun startDownloadQueueService() {
        if (isDownloading) return
        if (!isAppInForeground(context)) return

        val intent = Intent(context, DownloadService::class.java)

        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        val bindResult = context.bindService(intent, downloadConn, Context.BIND_AUTO_CREATE)
        if (!bindResult) {
            downloadService?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    startDownloadQueue(it)
                }
            }
        }
    }

    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcess in appProcesses) {
            if (appProcess.processName == packageName &&
                appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            ) {
                return true
            }
        }
        return false
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun startDownloadQueue(downloadService: DownloadService) {
        isDownloading = true

        while (true) {
            checkAndStartNextDownload()
            delay(QUEUE_CHECK_INTERVAL_MS)

            val allTasksFinished = _downloadTasks.value.all {
                it.downloadState in listOf(
                    DownloadState.COMPLETED,
                    DownloadState.ERROR,
                    DownloadState.CANCELLED,
                    DownloadState.PAUSE
                )
            }
            val noActiveJobs = activeDownloadJobs.isEmpty()
            val noWaitingTasks =
                _downloadTasks.value.none { it.downloadState == DownloadState.WAITING }

            if (noActiveJobs && noWaitingTasks && (allTasksFinished || _downloadTasks.value.isEmpty())) {
                break
            }
        }

        downloadService.onDownloadFinished()
        runCatching { context.unbindService(downloadConn) }
        isDownloading = false
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun checkAndStartNextDownload() {
        if (activeDownloadJobs.size >= MAX_CONCURRENT_DOWNLOADS) return

        val nextTask = _downloadTasks.value.firstOrNull {
            it.downloadState == DownloadState.WAITING &&
                    !activeDownloadJobs.containsKey(it.downloadSegment.segmentId)
        } ?: return

        val job = downloadScope.launch {
            try {
                executeTaskDownload(nextTask)
            } catch (e: CancellationException) {
                // 协程被取消
            } catch (e: Exception) {
                handleTaskError(nextTask, e)
            } finally {
                activeDownloadJobs.remove(nextTask.downloadSegment.segmentId)
                checkAndStartNextDownload()
            }
        }

        activeDownloadJobs[nextTask.downloadSegment.segmentId] = job
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun executeTaskDownload(task: AppDownloadTask) {
        downloadService?.let { service ->
            // 前置任务
            handlePredecessor(task, service)


            // 下载
            var quality: String? = null
            if (task.downloadViewInfo.downloadMedia) {
                val (downloadResult, downloadQuality) = downloadAppTask(task, service)
                if (!downloadResult) throw Exception("下载失败")
                quality = downloadQuality
            }

            // 后置任务
            handleSuccessor(task, service, quality)

            val finalTask = findTaskById(task.downloadSegment.segmentId)
            if (finalTask?.downloadState == DownloadState.COMPLETED) {
                removeTaskFromList(task.downloadSegment.segmentId)
            }
        }
    }

    private suspend fun handlePredecessor(task: AppDownloadTask, service: DownloadService) {
        updateTaskState(task, DownloadState.PRE_TASK)

        // 下载嵌入字幕
        if (task.downloadViewInfo.embedCC) {
            val subtitles = subtitleDownloader.downloadSubtitlesForEmbed(
                task.downloadViewInfo.videoPlayerInfoV2,
                task.downloadSegment.segmentId
            )
            task.updateRuntimeInfo(task.taskRuntimeInfo.copy(subtitles = subtitles))
        }

        // 下载嵌入封面
        if (task.downloadViewInfo.embedCover) {
            val coverBytes = httpClient.get(task.cover?.toHttps() ?: "").bodyAsBytes()
            val tempDir = File(context.externalCacheDir, "cover")
            if (!tempDir.exists()) tempDir.mkdirs()
            val tempFile = File(tempDir, "embed_cover_${task.downloadSegment.segmentId}.jpg")
            tempFile.writeBytes(coverBytes)
            task.updateRuntimeInfo(task.taskRuntimeInfo.copy(coverPath = tempFile.absolutePath))
        }

        // 下载封面到相册
        if (task.downloadViewInfo.downloadCover) {
            downloadCoverImageForTask(task)
        }

        // 下载弹幕
        if (task.downloadViewInfo.downloadDanmaku) {
            downloadDanmakuForTask(task)
        }

        // 下载字幕文件
        if (task.downloadViewInfo.downloadCC) {
            subtitleDownloader.downloadSubtitlesToFile(
                task.downloadViewInfo.videoPlayerInfoV2,
                task.downloadSegment.title,
                task.downloadViewInfo.ccFileType
            )
        }

        updateTaskState(task, DownloadState.WAITING)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun downloadAppTask(
        task: AppDownloadTask,
        downloadService: DownloadService
    ): Pair<Boolean, String?> {
        if (task.downloadSubTasks.isEmpty()) return Pair(false, null)

        val progressCallback = createProgressCallback(task, downloadService, "下载阶段")

        return if (task.downloadSubTasks.size >= 2) {
            downloadMultipleSubTasks(task, progressCallback)
        } else {
            downloadSingleSubTask(task, progressCallback)
        }
    }

    private suspend fun downloadMultipleSubTasks(
        task: AppDownloadTask,
        progressCallback: (Float) -> Unit
    ): Pair<Boolean, String?> = withContext(Dispatchers.IO) {
        val videoTask = task.downloadSubTasks.first()
        val audioTask = task.downloadSubTasks.last()

        var videoProgress = 0f
        var audioProgress = 0f

        val videoResult = async {
            downloadSubTask(videoTask, task) {
                videoProgress = it
                progressCallback((videoProgress + audioProgress) / 2f)
            }
        }

        val audioResult = async {
            downloadSubTask(audioTask, task) {
                audioProgress = it
                progressCallback((videoProgress + audioProgress) / 2f)
            }
        }

        val (videoSuccess, videoQuality) = videoResult.await()
        val (audioSuccess, audioQuality) = audioResult.await()

        val quality = videoQuality ?: audioQuality
        Pair(videoSuccess && audioSuccess, quality)
    }

    private suspend fun downloadSingleSubTask(
        task: AppDownloadTask,
        progressCallback: (Float) -> Unit
    ): Pair<Boolean, String?> {
        val subTask = task.downloadSubTasks.first()
        val (result, quality) = downloadSubTask(subTask, task, progressCallback)

        if (result) {
            val newTask = task.copy(
                downloadSegment = task.downloadSegment.copy(
                    savePath = subTask.savePath,
                    qualityDescription = quality,
                    downloadState = DownloadState.COMPLETED
                )
            )
            updateTaskState(newTask, DownloadState.COMPLETED)
            downloadTaskRepository.updateSegment(newTask.downloadSegment)
        }

        return Pair(result, quality)
    }

    private suspend fun downloadSubTask(
        subTask: DownloadSubTask,
        task: AppDownloadTask,
        onUpdateProgress: (Float) -> Unit
    ): Pair<Boolean, String?> = withContext(Dispatchers.IO) {
        val nodeType =
            downloadTaskRepository.getTaskNodeByNodeId(task.downloadSegment.nodeId)?.nodeType
                ?: return@withContext Pair(false, null)

        val playerInfo = videoInfoFetcher.fetchVideoPlayerInfo(
            task.downloadSegment,
            nodeType,
            task.downloadViewInfo
        )

        val videoData = videoInfoFetcher.extractVideoData(playerInfo, task.downloadViewInfo)
            ?: return@withContext Pair(false, null)

        var quality: String? = null
        val downloadUrl = videoInfoFetcher.getDownloadUrl(
            videoData,
            subTask.subTaskType,
            task.downloadViewInfo,
            onQuality = { quality = it }
        ) ?: return@withContext Pair(false, null)

        Log.d("downloadUrl", "下载地址: $downloadUrl -- ${subTask.subTaskType}")

        val referer = buildRefererUrl(task)

        val result = downloadExecutor.downloadFile(
            downloadUrl = downloadUrl,
            savePath = subTask.savePath,
            referer = referer,
            onProgress = onUpdateProgress
        )

        Pair(result, quality)
    }

    private suspend fun handleSuccessor(task: AppDownloadTask, service: DownloadService, quality: String?) {
        val progressCallback = createProgressCallback(task, service, "合并阶段")
        val tempOutputFile = createTempOutputFile(task)

        try {
            ffmpegMerger.mergeMedia(
                subTasks = task.downloadSubTasks,
                downloadMode = task.downloadSegment.downloadMode,
                outputFile = tempOutputFile,
                subtitles = task.taskRuntimeInfo.subtitles,
                coverPath = task.taskRuntimeInfo.coverPath,
                duration = task.downloadSegment.duration,
                onProgress = progressCallback,
                task.downloadViewInfo.mediaContainerConfig,
            )

            updateTaskAndCleanup(task, tempOutputFile)
        } catch (e: Exception) {
            tempOutputFile.deleteIfExists()
            throw e
        }

        val segment = downloadTaskRepository.getSegmentBySegmentId(task.downloadSegment.segmentId)
        if (segment == null) {
            updateTaskState(task, DownloadState.ERROR)
            return
        }

        val lastFile = File(segment.savePath)
        val mimeType =
            getMimeType(task.downloadSegment.downloadMode, task.downloadViewInfo.mediaContainerConfig)
        val extension = getResExtension(task.downloadSegment.downloadMode, task.downloadViewInfo.mediaContainerConfig)
        val lastFileName = namingConventionHandler.buildFileName(
            task.downloadSegment.namingConventionInfo,
            extension
        )

        val uriStr = fileOutputManager.moveToDownloadAndRegister(lastFile, lastFileName, mimeType)
        if (uriStr != null) {
            val newTask = task.copy(
                downloadSegment = segment.copy(
                    savePath = uriStr,
                    qualityDescription = quality,
                    downloadState = DownloadState.COMPLETED
                )
            )
            downloadTaskRepository.updateSegment(newTask.downloadSegment)
            updateTaskState(newTask, DownloadState.COMPLETED)
        } else {
            updateTaskState(task, DownloadState.ERROR)
        }
    }

    private fun createTempOutputFile(task: AppDownloadTask): File {
        val saveDir = task.downloadSubTasks.first().savePath.substringBeforeLast("/")
        val extension =
            getResExtension(
                task.downloadSegment.downloadMode,
                task.downloadViewInfo.mediaContainerConfig
            )
        val timestamp = System.currentTimeMillis()

        return File(saveDir, "${task.downloadSegment.segmentId}_$timestamp.$extension").apply {
            parentFile?.mkdirs()
        }
    }

    private suspend fun updateTaskAndCleanup(task: AppDownloadTask, outputFile: File) {
        downloadTaskRepository.updateSegment(
            task.downloadSegment.copy(savePath = outputFile.absolutePath)
        )
        task.downloadSubTasks.forEach {
            File(it.savePath).deleteIfExists()
        }
    }

    private suspend fun buildRefererUrl(task: AppDownloadTask): String {
        return when (task.downloadTask.type) {
            DownloadTaskType.BILI_VIDEO,
            DownloadTaskType.BILI_DONGHUA -> {
                val platformId = task.downloadTask.platformId
                if (platformId.all { it.isDigit() }) {
                    "https://www.bilibili.com/bangumi/play/ss$platformId"
                } else {
                    "https://www.bilibili.com/video/$platformId"
                }
            }

            DownloadTaskType.BILI_VIDEO_SECTION if task.downloadSegment.taskId != 0L -> {
                val realTask = downloadTaskRepository.getTaskById(task.downloadSegment.taskId ?: 0L)
                "https://www.bilibili.com/video/${realTask?.platformId}"
            }

            DownloadTaskType.BILI_VIDEO_SECTION -> error("构造Referer URL失败")
        }
    }

    private fun getMimeType(mode: DownloadMode, mediaContainerConfig: MediaContainerConfig): String {
        return when (mode) {
            DownloadMode.AUDIO_VIDEO,
            DownloadMode.VIDEO_ONLY -> mediaContainerConfig.videoContainer.mimeType
            DownloadMode.AUDIO_ONLY -> mediaContainerConfig.audioContainer.mimeType
        }
    }

    /**
     * 获取资源后缀
     */
    private fun getResExtension(mode: DownloadMode, mediaContainerConfig: MediaContainerConfig): String {
        return when (mode) {
            DownloadMode.AUDIO_VIDEO,
            DownloadMode.VIDEO_ONLY -> mediaContainerConfig.videoContainer.extension
            DownloadMode.AUDIO_ONLY -> mediaContainerConfig.audioContainer.extension
        }
    }

    private suspend fun downloadDanmakuForTask(task: AppDownloadTask) {
        val oid = task.downloadSegment.platformUniqueId.toLong()
        val title = namingConventionHandler.buildFileName(
            task.downloadSegment.namingConventionInfo,
            "xml"
        )

        val elms = flow {
            var page = 0
            while (true) {
                val list = videoInfoRepository.getDanmaku(oid = oid, segmentIndex = page)
                    .getOrNull()?.elems
                if (list.isNullOrEmpty()) break
                emitAll(list.asFlow())
                page++
            }
        }.toList()

        val xml = DanmakuXmlUtil.toBilibiliDanmakuXml(elms, oid)
        fileOutputManager.createDanmakuOutputStream(title).use { os ->
            os.write(xml.toByteArray(Charsets.UTF_8))
        }
    }

    private suspend fun downloadCoverImageForTask(task: AppDownloadTask) {
        val type = task.cover?.substringAfterLast(".")
        val fileName = when (task.downloadTask.type) {
            DownloadTaskType.BILI_DONGHUA,
            DownloadTaskType.BILI_VIDEO -> "${task.downloadSegment.platformId}_pic.$type"

            DownloadTaskType.BILI_VIDEO_SECTION if task.downloadSegment.taskId != null -> {
                val realTask = downloadTaskRepository.getTaskById(task.downloadSegment.taskId ?: 0L)
                "${realTask?.platformId}_pic.$type"
            }

            DownloadTaskType.BILI_VIDEO_SECTION -> error("封面所属任务类型异常")
        }
        downloadImageToAlbum(task.cover?.toHttps() ?: "", fileName, "BILIBILIAS")
    }

    private suspend fun processDownloadTree(
        taskTree: DownloadTaskTree,
        downloadViewInfo: DownloadViewInfo
    ) {
        val currentTasks = _downloadTasks.value.toMutableList()

        suspend fun processNode(node: DownloadTreeNode) {
            node.segments.forEach { segment ->
                if (!currentTasks.any { it.downloadSegment.platformId == segment.platformId }) {
                    val downloadSubTasks =
                        createSubTasksForSegment(segment, node.node.nodeType, downloadViewInfo)
                    val cover = getCoverForSegment(segment)

                    val newTask = AppDownloadTask(
                        downloadTask = taskTree.task,
                        downloadSegment = segment,
                        downloadSubTasks = downloadSubTasks,
                        downloadViewInfo = downloadViewInfo,
                        downloadStage = DownloadStage.DOWNLOAD,
                        cover = cover,
                    )
                    currentTasks.add(newTask)
                }
            }
            node.children.forEach { processNode(it) }
        }

        taskTree.roots.forEach { processNode(it) }

        _downloadTasks.value = currentTasks
    }

    private suspend fun createSubTasksForSegment(
        segment: DownloadSegment,
        nodeType: DownloadTaskNodeType,
        downloadViewInfo: DownloadViewInfo
    ): List<DownloadSubTask> {
        val playerInfo = videoInfoFetcher.fetchVideoPlayerInfo(segment, nodeType, downloadViewInfo)
        val videoData = videoInfoFetcher.extractVideoData(playerInfo, downloadViewInfo)
            ?: return emptyList()

        val subTasks = when (videoData) {
            is BILIVideoDash -> {
                when (segment.downloadMode) {
                    DownloadMode.AUDIO_VIDEO -> listOf(
                        createSubTask(
                            segment,
                            DownloadSubTaskType.VIDEO,
                            downloadViewInfo.mediaContainerConfig
                        ),
                        createSubTask(
                            segment,
                            DownloadSubTaskType.AUDIO,
                            downloadViewInfo.mediaContainerConfig
                        )
                    )

                    DownloadMode.VIDEO_ONLY -> listOf(
                        createSubTask(
                            segment,
                            DownloadSubTaskType.VIDEO,
                            downloadViewInfo.mediaContainerConfig
                        )
                    )

                    DownloadMode.AUDIO_ONLY -> listOf(
                        createSubTask(
                            segment,
                            DownloadSubTaskType.AUDIO,
                            downloadViewInfo.mediaContainerConfig
                        )
                    )
                }
            }

            is BILIVideoDurl -> {
                downloadTaskRepository.updateSegment(segment.copy(downloadMode = DownloadMode.VIDEO_ONLY))
                listOf(
                    createSubTask(
                        segment,
                        DownloadSubTaskType.VIDEO,
                        downloadViewInfo.mediaContainerConfig
                    )
                )
            }

            else -> throw IllegalStateException("不支持的下载数据类型")
        }

        delay(500L)
        return subTasks
    }

    private fun createSubTask(
        segment: DownloadSegment,
        type: DownloadSubTaskType,
        mediaContainerConfig: MediaContainerConfig
    ): DownloadSubTask {
        val savePath = getSaveTempSubTaskPath(type) + "/${segment.platformId}_${type.name}.${
            when (type) {
                DownloadSubTaskType.VIDEO -> mediaContainerConfig.videoContainer.extension
                DownloadSubTaskType.AUDIO -> mediaContainerConfig.audioContainer.extension
            }
        }"
        return DownloadSubTask(
            segmentId = segment.segmentId,
            savePath = savePath,
            subTaskType = type,
            downloadState = DownloadState.WAITING
        )
    }

    private fun getSaveTempSubTaskPath(subTaskType: DownloadSubTaskType): String {
        val dirName = when (subTaskType) {
            DownloadSubTaskType.VIDEO -> "video"
            DownloadSubTaskType.AUDIO -> "audio"
        }
        return context.getExternalFilesDir(dirName)?.absolutePath!!
    }

    private suspend fun getCoverForSegment(segment: DownloadSegment): String? {
        return if (segment.taskId != null && segment.taskId != 0L) {
            downloadTaskRepository.getTaskById(segment.taskId!!)?.cover
        } else {
            segment.cover
        }
    }

    private fun createProgressCallback(
        task: AppDownloadTask,
        downloadService: DownloadService,
        stage: String
    ): (Float) -> Unit = { progress ->
        downloadService.updateNotification(
            task.downloadSegment.title,
            stage,
            (progress * 100).toInt()
        )
        val state = if (stage == "合并阶段") DownloadState.MERGING else DownloadState.DOWNLOADING
        updateTaskState(task.copy(progress = progress), state)
    }

    private fun findTaskById(segmentId: Long): AppDownloadTask? {
        return _downloadTasks.value.find { it.downloadSegment.segmentId == segmentId }
    }

    private suspend fun cancelActiveJob(segmentId: Long) {
        activeDownloadJobs[segmentId]?.cancelAndJoin()
        activeDownloadJobs.remove(segmentId)
    }

    private fun deleteTaskFiles(task: AppDownloadTask) {
        task.downloadSubTasks.forEach { subTask ->
            File(subTask.savePath).delete()
            File("${subTask.savePath}.downloading").delete()
        }
    }

    private fun handleTaskError(task: AppDownloadTask, error: Exception) {
        updateTaskState(task, DownloadState.ERROR)
        error.printStackTrace()
    }

    private fun updateTaskState(task: AppDownloadTask, state: DownloadState) {
        _downloadTasks.value = _downloadTasks.value.map { existingTask ->
            if (existingTask.downloadSegment.segmentId == task.downloadSegment.segmentId) {
                task.copy(
                    downloadSegment = task.downloadSegment.copy(downloadState = state),
                    downloadState = state
                )
            } else {
                existingTask
            }
        }
    }

    private fun removeTaskFromList(segmentId: Long) {
        val currentTasks = _downloadTasks.value.toMutableList()
        currentTasks.removeAll { it.downloadSegment.segmentId == segmentId }
        _downloadTasks.value = currentTasks
    }

    private fun File.deleteIfExists() {
        if (exists()) delete()
    }
}
