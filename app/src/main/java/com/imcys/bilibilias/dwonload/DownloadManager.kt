package com.imcys.bilibilias.dwonload

import android.Manifest
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import com.imcys.bilibilias.BILIBILIASApplication
import com.imcys.bilibilias.common.utils.DOWNLOAD_NOTIFICATION_ID
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
import com.imcys.bilibilias.dwonload.service.DownloadService
import com.imcys.bilibilias.ffmpeg.FFmpegManger
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurl
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileOutputStream


class DownloadManager(
    private val context: BILIBILIASApplication,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val videoInfoRepository: VideoInfoRepository,
    private val json: Json,
    private val okHttpClient: OkHttpClient,
) {

    // 内存中的下载任务存储，不持久化子任务
    private val _downloadTasks = MutableStateFlow<List<AppDownloadTask>>(emptyList())

    private var isInit = false

    // 下载队列调度相关
    private var isDownloading = false

    // ====== 1. 初始化与任务管理 ======

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

        // 用协程异步启动下载队列，避免阻塞当前协程
        if (!isDownloading) startDownloadQueueService()
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
                if (!currentTasks.any { it.downloadSegment.platformId == segment.platformId }) {
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

    /**
     * 移除队首任务
     */
    private fun removeCurrentTask() {
        val currentTasks = _downloadTasks.value.toMutableList()
        if (currentTasks.isNotEmpty()) {
            currentTasks.removeAt(0)
            _downloadTasks.value = currentTasks
        }
    }

    /**
     * 更新任务状态
     */
    private fun updateTaskState(task: AppDownloadTask, state: DownloadState) {
        val currentTasks = _downloadTasks.value.toMutableList()
        val index =
            currentTasks.indexOfFirst { it.downloadSegment.segmentId == task.downloadSegment.segmentId }
        if (index != -1) {
            currentTasks[index] = task.copy(
                downloadSegment = task.downloadSegment.copy(downloadState = state),
                downloadState = state
            )
            _downloadTasks.value = currentTasks
        }
    }

    /**
     * 更新任务阶段
     */
    private fun updateTaskStage(task: AppDownloadTask, stage: DownloadStage) {
        val currentTasks = _downloadTasks.value.toMutableList()
        val index =
            currentTasks.indexOfFirst { it.downloadSegment.segmentId == task.downloadSegment.segmentId }
        if (index != -1) {
            currentTasks[index] = task.copy(downloadStage = stage)
            _downloadTasks.value = currentTasks
        }
    }


    private var downloadService: DownloadService? = null
    private val downloadConn = object : ServiceConnection {
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        override fun onServiceConnected(
            p0: ComponentName?,
            iBinder: IBinder?
        ) {
            val binder = iBinder as DownloadService.DownloadBinder
            downloadService = binder.service
            // 绑定成功后，获取服务实例
            GlobalScope.launch(Dispatchers.IO) {
                binder.service?.let {
                    startDownloadQueue(it)
                }
            }

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }

    }

    /**
     * 启动下载队列
     */
    fun startDownloadQueueService() {
        if (isDownloading) return
        val intent = Intent(context, DownloadService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
        val result = context.bindService(intent, downloadConn, Context.BIND_AUTO_CREATE)
        if (!result) {
            downloadService?.let {

                GlobalScope.launch(Dispatchers.IO) {
                    startDownloadQueue(it)
                }
            }

        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    suspend fun startDownloadQueue(
        downloadService: DownloadService,
    ) {
        isDownloading = true
        while (_downloadTasks.value.isNotEmpty()) {
            val currentTask = _downloadTasks.value.first()
            if (currentTask.downloadState == DownloadState.COMPLETED || currentTask.downloadState == DownloadState.ERROR) {
                removeCurrentTask()
                continue
            }

            // 下载当前任务
            val result = downloadAppTask(currentTask, downloadService)
            if (result) {
                if (currentTask.downloadSubTasks.size >= 2) {
                    // 进入合并阶段
                    mergeTask(currentTask, downloadService)
                }
                removeCurrentTask()
            } else {
                removeCurrentTask()
            }
        }

        downloadService.onDownloadFinished()
        context.unbindService(downloadConn)
        isDownloading = false
    }


    /**
     * 下载AppDownloadTask的所有子任务，全部成功返回true，否则false
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun downloadAppTask(
        task: AppDownloadTask,
        downloadService: DownloadService,
    ): Boolean {
        if (task.downloadSubTasks.isEmpty()) return false

        // 进度回调，统一处理通知和状态
        fun updateProgress(progress: Float) {
            downloadService.updateNotification(
                task.downloadSegment.title,
                "下载阶段",
                (progress * 100).toInt(),
            )
            updateTaskState(task.copy(progress = progress), DownloadState.DOWNLOADING)
        }

        return if (task.downloadSubTasks.size >= 2) {
            val oneTask = task.downloadSubTasks.first()
            val twoTask = task.downloadSubTasks.last()
            withContext(Dispatchers.IO) {
                var oneProgress = 0f
                var twoProgress = 0f

                val oneResult = async {
                    downloadSubTask(oneTask, task) {
                        oneProgress = it
                        updateProgress((oneProgress + twoProgress) / 2f)
                    }
                }
                val twoResult = async {
                    downloadSubTask(twoTask, task) {
                        twoProgress = it
                        updateProgress((oneProgress + twoProgress) / 2f)
                    }
                }
                oneResult.await() && twoResult.await()
            }
        } else {
            val oneTask = task.downloadSubTasks.first()
            val result = downloadSubTask(oneTask, task) {
                updateProgress(it)
            }

            if (result) {
                val fileName =
                    task.downloadSegment.title + if (oneTask.subTaskType == DownloadSubTaskType.VIDEO) ".mp4" else ".m4a"
                val mimeType =
                    if (oneTask.subTaskType == DownloadSubTaskType.VIDEO) "video/mp4" else "audio/mp4"
                val file = File(oneTask.savePath)
                val uriStr = moveToDownloadAndRegister(file, fileName, mimeType)
                if (uriStr != null) {
                    val newTask = task.copy(
                        downloadSegment = task.downloadSegment.copy(
                            savePath = uriStr,
                            downloadState = DownloadState.COMPLETED
                        )
                    )
                    downloadTaskRepository.updateSegment(newTask.downloadSegment)
                    updateTaskState(newTask, DownloadState.COMPLETED)
                } else {
                    updateTaskState(task, DownloadState.ERROR)
                }

            }
            result
        }
    }

    private suspend fun downloadSubTask(
        subTask: DownloadSubTask,
        task: AppDownloadTask,
        onUpdateProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        val file = File(subTask.savePath)
        val tempFile = File("${subTask.savePath}.downloading")
        val maxRetry = 5
        var attempt = 0
        var success = false

        // 获取已下载长度
        fun getDownloadedLength(): Long {
            return if (tempFile.exists()) tempFile.length() else 0L
        }

        while (attempt < maxRetry && !success) {
            try {
                val ref = if (task.downloadTask.platformId.all { it.isDigit() }) {
                    "https://www.bilibili.com/bangumi/play/ss${task.downloadTask.platformId}"
                } else {
                    "https://www.bilibili.com/video/${task.downloadTask.platformId}"
                }

                val downloaded = getDownloadedLength()
                val requestBuilder = okhttp3.Request.Builder()
                    .url(subTask.downloadUrl)
                    .addHeader("Referer", ref)
                if (downloaded > 0) {
                    requestBuilder.addHeader("Range", "bytes=$downloaded-")
                }
                val request = requestBuilder.build()
                val response = okHttpClient.newCall(request).execute()
                val body = response.body ?: return@withContext false
                val total = if (downloaded > 0) {
                    downloaded + body.contentLength()
                } else {
                    body.contentLength()
                }
                if (total == 0L) return@withContext true

                body.byteStream().use { input ->
                    // 用追加模式写入断点续传
                    tempFile.parentFile?.mkdirs()
                    FileOutputStream(tempFile, downloaded > 0).use { output ->
                        val buffer = ByteArray(64 * 1024)
                        var currentDownloaded = downloaded
                        var read: Int
                        while (input.read(buffer).also { read = it } != -1) {
                            output.write(buffer, 0, read)
                            currentDownloaded += read
                            onUpdateProgress(currentDownloaded.toFloat() / total)
                        }
                    }
                }

                if (file.exists()) file.delete()
                tempFile.renameTo(file)
                success = true
            } catch (e: Exception) {
                attempt++
                e.printStackTrace()
                if (attempt < maxRetry) {
                    delay(3000L)
                }
            }
        }
        success
    }

    /**
     * 合并任务（协程挂起，支持进度与结束回调）
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun mergeTask(
        task: AppDownloadTask,
        downloadService: DownloadService,
    ) {
        updateTaskStage(task, DownloadStage.MERGE)

        fun updateProgress(progress: Int) {
            downloadService.updateNotification(
                task.downloadSegment.title,
                "合并阶段",
                progress,
            )
            updateTaskState(task.copy(progress = progress / 100f), DownloadState.MERGING)
        }

        FFmpegManger.mergeVideoAndAudioSuspend(
            task.downloadSubTasks[0].savePath,
            task.downloadSubTasks[1].savePath,
            task.downloadSubTasks[0].savePath + "_merged.mp4",
            object : FFmpegManger.FFmpegMergeListener {
                override fun onProgress(progress: Int) {
                    print("合并进度: $progress")
                    updateProgress(progress)
                }

                override fun onError(errorMsg: String) {
                    updateTaskState(task, DownloadState.ERROR)
                }

                override fun onComplete() {
                    updateTaskState(task, DownloadState.COMPLETED)
                }
            }).onSuccess {
            val fileName = task.downloadSegment.title + "_merged.mp4"
            val mimeType = "video/mp4"
            val mergedFile = File(task.downloadSubTasks[0].savePath + "_merged.mp4")
            val uriStr = moveToDownloadAndRegister(mergedFile, fileName, mimeType)
            File(task.downloadSubTasks[0].savePath).delete()
            File(task.downloadSubTasks[1].savePath).delete()
            if (uriStr != null) {
                val newTask = task.copy(
                    downloadSegment = task.downloadSegment.copy(
                        savePath = uriStr,
                        downloadState = DownloadState.COMPLETED
                    )
                )
                downloadTaskRepository.updateSegment(newTask.downloadSegment)
                updateTaskState(newTask, DownloadState.COMPLETED)
            } else {
                updateTaskState(task, DownloadState.ERROR)
            }
        }
    }


    /**
     * 将下载完成的文件移动到公共Download目录，并注册到媒体库（协程挂起）
     */
    private suspend fun moveToDownloadAndRegister(
        file: File,
        fileName: String,
        mimeType: String
    ): String? = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(
                    MediaStore.Downloads.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS + "/BILIBILIAS"
                )
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        file.inputStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    file.delete()
                    return@withContext uri.toString()
                } catch (e: Exception) {
                    return@withContext null
                }
            }
            return@withContext null
        } else {
            // Android Q以下代码不变
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val targetDir = File(downloadsDir, "BILIBILIAS")
            if (!targetDir.exists()) targetDir.mkdirs()
            val targetFile = File(targetDir, fileName)
            return@withContext try {
                file.inputStream().use { inputStream ->
                    targetFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                file.delete()
                targetFile.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun getSaveTempSubTaskPath(
        subTaskType: DownloadSubTaskType
    ): String {
        val dirName = when (subTaskType) {
            DownloadSubTaskType.VIDEO -> "video"
            DownloadSubTaskType.AUDIO -> "audio"
        }

        val fileDir = context.getExternalFilesDir(dirName)
        return fileDir?.absolutePath!!
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
            throw IllegalStateException("""
                视频接口异常:
                平台ID：${segment.platformId}
                任务平台ID：${getSegmentBvId(segment)}
            """.trimIndent())
        }

        val videoData = when (val result = videoInfo.data) {
            is BILIDonghuaPlayerInfo -> result.dash ?: result.durls?.firstOrNull {
                it.quality == downloadViewInfo.selectVideoQualityId
            }?.durl?.first() ?: result.durls?.first()?.durl?.first()

            is BILIVideoPlayerInfo -> result.dash ?: result.durls?.firstOrNull {
                it.quality == downloadViewInfo.selectVideoQualityId
            }?.durl?.first() ?: result.durls?.first()?.durl?.first()

            else -> {
                null
            }
        } ?: return emptyList()

        // 根据segment的下载模式选择流


        val subTasks = when (videoData) {
            is BILIVideoDash -> {
                when (segment.downloadMode) {
                    DownloadMode.AUDIO_VIDEO -> {
                        val selectedVideo = selectVideoQuality(videoData.video, downloadViewInfo)
                        val selectedAudio = selectAudioQuality(videoData, downloadViewInfo)
                        listOf(
                            createSubTask(
                                segment,
                                selectedVideo.baseUrl,
                                DownloadSubTaskType.VIDEO
                            ),
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
            }

            is BILIVideoDurl -> {
                downloadTaskRepository.updateSegment(
                    segment.copy(downloadMode = DownloadMode.VIDEO_ONLY)
                )
                listOf(
                    createSubTask(segment, videoData.url, DownloadSubTaskType.VIDEO)
                )
            }

            else -> throw IllegalStateException("不支持的下载数据类型")
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
    ): DownloadSubTask {
        val savePath = getSaveTempSubTaskPath(
            subTaskType = type
        ) + "/${segment.platformId}_${segment.title}.${
            when (type) {
                DownloadSubTaskType.VIDEO -> "mp4"
                DownloadSubTaskType.AUDIO -> "m4a"
            }
        }"
        return DownloadSubTask(
            segmentId = segment.segmentId,
            savePath = savePath,
            downloadUrl = url,
            subTaskType = type,
            downloadState = DownloadState.WAITING
        )
    }

}
