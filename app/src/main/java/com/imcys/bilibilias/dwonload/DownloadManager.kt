package com.imcys.bilibilias.dwonload

import android.Manifest
import android.content.ComponentName
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import com.imcys.bilibilias.BILIBILIASApplication
import com.imcys.bilibilias.common.utils.toHttps
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
import com.imcys.bilibilias.database.entity.download.DownloadTaskType
import com.imcys.bilibilias.dwonload.service.DownloadService
import com.imcys.bilibilias.ffmpeg.FFmpegManger
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurl
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.isActive
import kotlinx.coroutines.currentCoroutineContext

/**
 * 下载管理器
 *
 * 主要功能：
 * 1. 任务管理：添加、暂停、恢复、取消下载任务
 * 2. 并发控制：支持多任务并发下载
 * 3. 断点续传：支持网络中断后继续下载
 * 4. 文件管理：自动合并音视频，移动到下载目录
 */
class DownloadManager(
    private val context: BILIBILIASApplication,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val videoInfoRepository: VideoInfoRepository,
    private val json: Json,
    private val okHttpClient: OkHttpClient,
) {

    companion object {
        // 最大并发
        private const val MAX_CONCURRENT_DOWNLOADS = 1

        // 重试次数
        private const val MAX_RETRY_ATTEMPTS = 5

        // 重试间隔
        private const val RETRY_DELAY_MS = 3000L

        // 文件缓存大小
        private const val DOWNLOAD_BUFFER_SIZE = 64 * 1024

        // 下载队列检查间隔
        private const val QUEUE_CHECK_INTERVAL_MS = 1000L
    }

    private val _downloadTasks = MutableStateFlow<List<AppDownloadTask>>(emptyList())
    private var isInit = false
    private var isDownloading = false

    // 活跃的下载任务
    private val activeDownloadJobs = ConcurrentHashMap<Long, Job>()

    // 用于下载的协程作用域
    private val downloadScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    // 下载通知服务连接
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


    /**
     * 初始化下载管理器
     */
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

    /**
     * 获取所有下载任务
     */
    fun getAllDownloadTasks(): StateFlow<List<AppDownloadTask>> = _downloadTasks.asStateFlow()

    /**
     * 获取指定下载任务
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


    /**
     * 暂停指定任务
     */
    suspend fun pauseTask(segmentId: Long) {
        val task = findTaskById(segmentId) ?: return
        if (task.downloadState != DownloadState.DOWNLOADING) return

        cancelActiveJob(segmentId)
        updateTaskState(task, DownloadState.PAUSE)
        downloadTaskRepository.updateSegment(task.downloadSegment.copy(downloadState = DownloadState.PAUSE))
    }

    /**
     * 取消指定任务
     */
    suspend fun cancelTask(segmentId: Long) {
        val task = findTaskById(segmentId) ?: return

        cancelActiveJob(segmentId)
        deleteTaskFiles(task)
        updateTaskState(task, DownloadState.CANCELLED)
        downloadTaskRepository.updateSegment(task.downloadSegment.copy(downloadState = DownloadState.CANCELLED))
        removeTaskFromList(segmentId)
    }

    /**
     * 恢复指定任务
     */
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

    /**
     * 暂停所有任务
     */
    suspend fun pauseAllTasks() {
        val downloadingTasks = _downloadTasks.value.filter {
            it.downloadState in listOf(DownloadState.DOWNLOADING, DownloadState.MERGING)
        }
        downloadingTasks.forEach { pauseTask(it.downloadSegment.segmentId) }
    }

    /**
     * 恢复所有暂停的任务
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    suspend fun resumeAllTasks() {
        val pausedTasks = _downloadTasks.value.filter { it.downloadState == DownloadState.PAUSE }
        pausedTasks.forEach { resumeTask(it.downloadSegment.segmentId) }
    }


    /**
     * 下载封面图片
     */
    suspend fun downloadImageToAlbum(imageUrl: String, fileName: String, saveDirName: String) =
        withContext(Dispatchers.IO) {
            val response = okHttpClient.newCall(
                okhttp3.Request.Builder()
                    .url(imageUrl)
                    .build()
            ).execute()


            if (!response.isSuccessful) {
                return@withContext
            }

            val body = response.body ?: return@withContext
            val inputStream = body.byteStream()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 通过 MediaStore 保存
                val resolver = context.contentResolver
                val relativeRoot = Environment.DIRECTORY_PICTURES
                val relativePath = "$relativeRoot/$saveDirName"

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(
                        MediaStore.Images.Media.MIME_TYPE,
                        "image/${fileName.substringAfterLast('.')}"
                    )
                    put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: throw IllegalStateException("Failed to insert into MediaStore")

                try {
                    resolver.openOutputStream(uri, "w")?.use { out ->
                        inputStream.copyTo(out)
                        out.flush()
                    } ?: throw IllegalStateException("Failed to open output stream for $uri")
                } catch (e: Exception) {
                    // 写失败时删除占位
                    runCatching { resolver.delete(uri, null, null) }
                    throw e
                } finally {
                    // 标记写入完成
                    val done = ContentValues().apply {
                        put(MediaStore.Images.Media.IS_PENDING, 0)
                    }
                    resolver.update(uri, done, null, null)
                }

            } else {
                // Android 9 及以下：写入公共目录 + 扫描
                val baseDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val albumDir = File(baseDir, fileName).apply { if (!exists()) mkdirs() }
                val outFile = File(albumDir, saveDirName)

                FileOutputStream(outFile).use { out ->
                    inputStream.copyTo(out)
                    out.flush()
                }

                // 通知相册扫描
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(outFile.absolutePath),
                    arrayOf("image/${fileName.substringAfterLast('.')}"),
                    null
                )

            }
        }


    /**
     * 启动下载队列服务
     */
    fun startDownloadQueueService() {
        if (isDownloading) return

        val intent = Intent(context, DownloadService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
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

    /**
     * 执行下载队列
     */
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

        // 清理工作
        downloadService.onDownloadFinished()
        context.unbindService(downloadConn)
        isDownloading = false
    }

    /**
     * 检查并启动下一个下载任务
     */
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
                // 协程被取消（暂停或取消任务），不需要处理为错误
                // 任务状态已经在pauseTask或cancelTask中设置
            } catch (e: Exception) {
                handleTaskError(nextTask, e)
            } finally {
                activeDownloadJobs.remove(nextTask.downloadSegment.segmentId)
                checkAndStartNextDownload()
            }
        }

        activeDownloadJobs[nextTask.downloadSegment.segmentId] = job
    }


    /**
     * 执行任务下载
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun executeTaskDownload(task: AppDownloadTask) {
        downloadService?.let { service ->
            val downloadResult = downloadAppTask(task, service)

            if (downloadResult && task.downloadSubTasks.size >= 2) {
                mergeTask(task, service)
            }

            val finalTask = findTaskById(task.downloadSegment.segmentId)
            if (finalTask?.downloadState == DownloadState.COMPLETED) {
                removeTaskFromList(task.downloadSegment.segmentId)
            }
        }
    }

    /**
     * 下载任务的所有子任务
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun downloadAppTask(
        task: AppDownloadTask,
        downloadService: DownloadService,
    ): Boolean {
        if (task.downloadSubTasks.isEmpty()) return false

        val progressCallback = createProgressCallback(task, downloadService, "下载阶段")

        return if (task.downloadSubTasks.size >= 2) {
            downloadMultipleSubTasks(task, progressCallback)
        } else {
            downloadSingleSubTask(task, progressCallback)
        }
    }

    /**
     * 下载多个子任务（音频+视频）
     */
    private suspend fun downloadMultipleSubTasks(
        task: AppDownloadTask,
        progressCallback: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
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

        videoResult.await() && audioResult.await()
    }

    /**
     * 下载单个子任务
     */
    private suspend fun downloadSingleSubTask(
        task: AppDownloadTask,
        progressCallback: (Float) -> Unit
    ): Boolean {
        val subTask = task.downloadSubTasks.first()
        val result = downloadSubTask(subTask, task, progressCallback)

        if (result) {
            val fileName = "${task.downloadSegment.title}${getFileExtension(subTask.subTaskType)}"
            val mimeType = getMimeType(subTask.subTaskType)
            val file = File(subTask.savePath)

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

        return result
    }

    /**
     * 下载单个子任务的具体实现
     */
    private suspend fun downloadSubTask(
        subTask: DownloadSubTask,
        task: AppDownloadTask,
        onUpdateProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        val file = File(subTask.savePath)
        val tempFile = File("${subTask.savePath}.downloading")

        repeat(MAX_RETRY_ATTEMPTS) { attempt ->
            try {
                val success = performDownload(subTask, task, tempFile, onUpdateProgress)
                if (success) {
                    if (file.exists()) file.delete()
                    tempFile.renameTo(file)
                    return@withContext true
                }
            } catch (e: CancellationException) {
                // 协程被取消（暂停或取消任务），直接重新抛出异常，不进行重试
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                if (attempt < MAX_RETRY_ATTEMPTS - 1) {
                    delay(RETRY_DELAY_MS)
                }
            }
        }
        false
    }

    /**
     * 执行实际的HTTP下载
     */
    private suspend fun performDownload(
        subTask: DownloadSubTask,
        task: AppDownloadTask,
        tempFile: File,
        onUpdateProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        val downloaded = if (tempFile.exists()) tempFile.length() else 0L
        val referer = buildRefererUrl(task)

        val requestBuilder = okhttp3.Request.Builder()
            .url(subTask.downloadUrl)
            .addHeader("Referer", referer)

        if (downloaded > 0) {
            requestBuilder.addHeader("Range", "bytes=$downloaded-")
        }

        val response = okHttpClient.newCall(requestBuilder.build()).execute()
        val body = response.body ?: return@withContext false

        val total = if (downloaded > 0) {
            downloaded + body.contentLength()
        } else {
            body.contentLength()
        }

        if (total == 0L) return@withContext true

        body.byteStream().use { input ->
            tempFile.parentFile?.mkdirs()
            FileOutputStream(tempFile, downloaded > 0).use { output ->
                val buffer = ByteArray(DOWNLOAD_BUFFER_SIZE)
                var currentDownloaded = downloaded
                var read: Int

                while (input.read(buffer).also { read = it } != -1) {
                    // 检查协程是否被取消（暂停或取消任务时会取消协程）
                    if (!currentCoroutineContext().isActive) {
                        throw CancellationException("Download was cancelled or paused")
                    }

                    output.write(buffer, 0, read)
                    currentDownloaded += read
                    onUpdateProgress(currentDownloaded.toFloat() / total)
                }
            }
        }

        return@withContext true
    }

    /**
     * 合并音视频文件
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun mergeTask(
        task: AppDownloadTask,
        downloadService: DownloadService,
    ) {
        updateTaskStage(task, DownloadStage.MERGE)
        val progressCallback = createProgressCallback(task, downloadService, "合并阶段")

        val mergeResult = FFmpegManger.mergeVideoAndAudioSuspend(
            task.downloadSubTasks[0].savePath,
            task.downloadSubTasks[1].savePath,
            "${task.downloadSubTasks[0].savePath}_merged.mp4",
            task.downloadSegment.title,
            task.downloadTask.description,
            buildRefererUrl(task),
            createMergeListener(task, progressCallback)
        )

        mergeResult.onSuccess {
            handleMergeSuccess(task)
        }
    }

    /**
     * 创建合并监听器
     */
    private fun createMergeListener(
        task: AppDownloadTask,
        progressCallback: (Float) -> Unit
    ) = object : FFmpegManger.FFmpegMergeListener {
        override fun onProgress(progress: Int) {
            progressCallback(progress / 100f)
        }

        override fun onError(errorMsg: String) {
            updateTaskState(task, DownloadState.ERROR)
        }

        override fun onComplete() {
            updateTaskState(task, DownloadState.COMPLETED)
        }
    }

    /**
     * 处理合并成功
     */
    private suspend fun handleMergeSuccess(task: AppDownloadTask) {
        val fileName = "${task.downloadSegment.title}_merged.mp4"
        val mergedFile = File("${task.downloadSubTasks[0].savePath}_merged.mp4")
        val uriStr = moveToDownloadAndRegister(mergedFile, fileName, "video/mp4")

        // 清理临时文件
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


    /**
     * 创建进度回调
     */
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

    /**
     * 查找任务
     */
    private fun findTaskById(segmentId: Long): AppDownloadTask? {
        return _downloadTasks.value.find { it.downloadSegment.segmentId == segmentId }
    }

    /**
     * 取消活跃的下载任务
     */
    private suspend fun cancelActiveJob(segmentId: Long) {
        activeDownloadJobs[segmentId]?.cancelAndJoin()
        activeDownloadJobs.remove(segmentId)
    }

    /**
     * 删除任务相关文件
     */
    private fun deleteTaskFiles(task: AppDownloadTask) {
        task.downloadSubTasks.forEach { subTask ->
            File(subTask.savePath).delete()
            File("${subTask.savePath}.downloading").delete()
        }
    }

    /**
     * 处理任务错误
     */
    private fun handleTaskError(task: AppDownloadTask, error: Exception) {
        updateTaskState(task, DownloadState.ERROR)
        error.printStackTrace()
    }

    /**
     * 构建Referer URL
     */
    private suspend fun buildRefererUrl(task: AppDownloadTask): String {

        return when(task.downloadTask.type){
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
            DownloadTaskType.BILI_VIDEO_SECTION -> error("构造Referer URL失败，任务类型不支持")
        }
    }

    /**
     * 获取文件扩展名
     */
    private fun getFileExtension(type: DownloadSubTaskType): String {
        return when (type) {
            DownloadSubTaskType.VIDEO -> ".mp4"
            DownloadSubTaskType.AUDIO -> ".m4a"
        }
    }

    /**
     * 获取MIME类型
     */
    private fun getMimeType(type: DownloadSubTaskType): String {
        return when (type) {
            DownloadSubTaskType.VIDEO -> "video/mp4"
            DownloadSubTaskType.AUDIO -> "audio/mp4"
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

    /**
     * 从任务列表中移除任务
     */
    private fun removeTaskFromList(segmentId: Long) {
        val currentTasks = _downloadTasks.value.toMutableList()
        currentTasks.removeAll { it.downloadSegment.segmentId == segmentId }
        _downloadTasks.value = currentTasks
    }


    /**
     * 将文件移动到下载目录并注册到媒体库
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

            // 检查是否已存在同名文件，存在则先删除
            val query = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val selection =
                "${MediaStore.Downloads.DISPLAY_NAME}=? AND ${MediaStore.Downloads.RELATIVE_PATH}=?"
            val selectionArgs = arrayOf(fileName, Environment.DIRECTORY_DOWNLOADS + "/BILIBILIAS")
            resolver.query(query, arrayOf(MediaStore.Downloads._ID), selection, selectionArgs, null)
                ?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val id =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
                        val existUri = ContentUris.withAppendedId(
                            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                            id
                        )
                        resolver.delete(existUri, null, null)
                    }
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

    /**
     * 获取临时存储路径
     */
    private fun getSaveTempSubTaskPath(subTaskType: DownloadSubTaskType): String {
        val dirName = when (subTaskType) {
            DownloadSubTaskType.VIDEO -> "video"
            DownloadSubTaskType.AUDIO -> "audio"
        }
        return context.getExternalFilesDir(dirName)?.absolutePath!!
    }

    /**
     * 处理下载任务树
     */
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
                        downloadStage = DownloadStage.DOWNLOAD,
                        cover = cover,
                    )
                    // 下载封面图片
                    if (downloadViewInfo.downloadCover) {
                        downloadCoverImageForTask(newTask)
                    }
                    currentTasks.add(newTask)
                }
            }
            node.children.forEach { processNode(it) }
        }

        taskTree.roots.forEach { processNode(it) }
        _downloadTasks.value = currentTasks
    }

    /**
     * 下载任务的封面图片
     */
    private fun downloadCoverImageForTask(
        task: AppDownloadTask,
    ) {

        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val type = task.cover?.substringAfterLast(".")
            val fileName = when (task.downloadTask.type) {
                DownloadTaskType.BILI_DONGHUA,
                DownloadTaskType.BILI_VIDEO -> "${task.downloadSegment.platformId}_pic.${type}"

                // 如果是合集，封面得找到对应的任务
                DownloadTaskType.BILI_VIDEO_SECTION if task.downloadSegment.taskId != null -> {
                    val realTask = downloadTaskRepository.getTaskById(task.downloadSegment.taskId ?: 0L)
                    "${realTask?.platformId}_pic.${type}"
                }

                DownloadTaskType.BILI_VIDEO_SECTION -> error("封面所属任务类型异常")
            }
            downloadImageToAlbum(task.cover?.toHttps() ?: "", fileName, "BILIBILIAS")
        }

    }

    /**
     * 获取segment的封面
     */
    private suspend fun getCoverForSegment(segment: DownloadSegment): String? {
        return if (segment.taskId != null && segment.taskId != 0L) {
            downloadTaskRepository.getTaskById(segment.taskId!!)?.cover
        } else {
            segment.cover
        }
    }

    /**
     * 创建子任务
     */
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

            DownloadTaskNodeType.BILI_DONGHUA_EPISOD,
            DownloadTaskNodeType.BILI_DONGHUA_SEASON,
            DownloadTaskNodeType.BILI_DONGHUA_SECTION -> {
                videoInfoRepository.getDonghuaPlayerInfo(
                    epId = segment.platformId.toLong(),
                    null
                ).last()
            }

            else -> throw IllegalStateException("缓存类型不支持: ${nodeType.name}")
        }

        if (videoInfo.status != ApiStatus.SUCCESS) {
            throw IllegalStateException(
                """
                视频接口异常:
                平台ID：${segment.platformId}
                任务平台ID：${getSegmentBvId(segment)}
            """.trimIndent()
            )
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
