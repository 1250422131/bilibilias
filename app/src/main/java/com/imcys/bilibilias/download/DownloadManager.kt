package com.imcys.bilibilias.download

import android.Manifest
import android.app.ActivityManager
import android.app.Application
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
import android.util.Log
import androidx.annotation.RequiresPermission
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFprobeKit
import com.arthenica.ffmpegkit.ReturnCode
import com.imcys.bilibilias.common.utils.autoRequestRetry
import com.imcys.bilibilias.common.utils.download.CCJsonToAss
import com.imcys.bilibilias.common.utils.download.CCJsonToSrt
import com.imcys.bilibilias.common.utils.download.DanmakuXmlUtil
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.download.CCFileType
import com.imcys.bilibilias.data.model.download.DownloadSubTask
import com.imcys.bilibilias.data.model.download.DownloadTaskTree
import com.imcys.bilibilias.data.model.download.DownloadTreeNode
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.download.lowercase
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
import com.imcys.bilibilias.database.entity.download.FileNamePlaceholder
import com.imcys.bilibilias.database.entity.download.NamingConventionInfo
import com.imcys.bilibilias.database.entity.download.donghuaNamingRules
import com.imcys.bilibilias.database.entity.download.videoNamingRules
import com.imcys.bilibilias.download.service.DownloadService
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.video.BILIDonghuaOgvPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerSynthesize
import com.imcys.bilibilias.network.model.video.BILIVideoCCInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurl
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfoV2
import com.imcys.bilibilias.network.service.AppAPIService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.RandomAccessFile
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 下载管理器
 */
@Deprecated("废弃", replaceWith = ReplaceWith("NewDownloadManager"))
class DownloadManager(
    private val context: Application,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val videoInfoRepository: VideoInfoRepository,
    private val appAPIService: AppAPIService,
    private val json: Json,
    private val okHttpClient: OkHttpClient,
    private val httpClient: HttpClient,
    private val appSettingsRepository: AppSettingsRepository
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
     * 主入口：批量保存用户选中的字幕
     */
    private suspend fun handleCCDownload(
        task: AppDownloadTask,
        ccFileType: CCFileType
    ) = withContext(Dispatchers.IO) {
        task.downloadViewInfo.videoPlayerInfoV2.data?.subtitle?.subtitles?.forEach { cc ->
            val url = cc.finalSubtitleUrl
            val finalUrl = if (!url.contains("https")) "https:" else ""
            val videoCCInfo = videoInfoRepository.getVideoCCInfo((finalUrl + url).toHttps())
            val content = convertCc(videoCCInfo, ccFileType)
            val title = "${task.downloadSegment.title}_${cc.lan}_${ccFileType.lowercase()}"
            resolveCcOutputStream(title, ccFileType)
                .use { it.write(content.toByteArray(Charsets.UTF_8)) }
        }
    }


    private fun convertCc(cc: BILIVideoCCInfo, type: CCFileType): String = when (type) {
        CCFileType.ASS -> CCJsonToAss.jsonToAss(cc, "字幕", "1920", "1080")
        CCFileType.SRT -> CCJsonToSrt.jsonToSrt(cc)
    }


    private fun resolveCcOutputStream(fileName: String, type: CCFileType): OutputStream {
        val mime = when (type) {
            CCFileType.ASS -> "text/x-ass"
            CCFileType.SRT -> "application/x-subrip"
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, mime)
                    put(MediaStore.Downloads.RELATIVE_PATH, "Download/BILIBILIAS")
                }
            ) ?: throw Exception("MediaStore insert failed")
            context.contentResolver.openOutputStream(uri)
        } else {
            val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "BILIBILIAS"
            ).apply { mkdirs() }
            FileOutputStream(File(dir, fileName))
        }.let { checkNotNull(it) { "OutputStream == null" } }
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
                uri?.let {
                    try {
                        resolver.openOutputStream(it)?.use { out ->
                            inputStream.copyTo(out)
                            out.flush()
                        }
                    } catch (e: Exception) {
                        // 写入失败：尝试删除刚创建的占位记录
                        try {
                            resolver.delete(it, null, null)
                        } catch (_: Exception) {
                        }
                        return@withContext
                    } finally {
                        // 解除 pending 状态
                        val update =
                            ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }
                        try {
                            resolver.update(it, update, null, null)
                        } catch (_: Exception) {
                        }
                    }
                }

            } else {
                // Android 9 及以下：写入公共目录 + 扫描
                val baseDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/${saveDirName}"
                val albumDir = File(baseDir).apply { if (!exists()) mkdirs() }
                val outFile = File(albumDir, fileName)

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


    /** 判断应用是否在前台 */
    fun isAppInForeground(context: Context): Boolean {
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
        runCatching { context.unbindService(downloadConn) }
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
            // 前置任务
            handlePredecessor(task, service)

            val downloadResult = downloadAppTask(task, service)

            if (!downloadResult) throw Exception("下载失败")

            // 后置任务
            handleSuccessor(task, service)

            val finalTask = findTaskById(task.downloadSegment.segmentId)
            if (finalTask?.downloadState == DownloadState.COMPLETED) {
                removeTaskFromList(task.downloadSegment.segmentId)
            }
        }
    }


    /**
     * 前置下载任务
     */
    private suspend fun handlePredecessor(task: AppDownloadTask, service: DownloadService) {
        updateTaskState(task, DownloadState.PRE_TASK)
        // 处理字幕
        handelEmbedCCDownload(task)
        // 处理封面
        handelEmbedCoverDownload(task)

        handleAdditionalDownload(task)

        updateTaskState(task, DownloadState.WAITING)
    }

    private suspend fun handleAdditionalDownload(task: AppDownloadTask) {
        val downloadViewInfo = task.downloadViewInfo

        // 下载封面图片
        if (downloadViewInfo.downloadCover) {
            downloadCoverImageForTask(task)
        }

        // 下载弹幕
        if (downloadViewInfo.downloadDanmaku) {
            downloadDanmakuForTask(task)
        }

        // 处理单体下载任务
        if (downloadViewInfo.downloadCC) {
            handleCCDownload(
                task,
                downloadViewInfo.ccFileType
            )
        }

    }

    /**
     * 需要内嵌的封面下载
     */
    private suspend fun handelEmbedCoverDownload(task: AppDownloadTask) {
        if (!task.downloadViewInfo.embedCover) return
        val bytes = httpClient.get(task.cover?.toHttps() ?: "").bodyAsBytes()

        // 存储在临时文件夹cacheDir/cover
        val tempDir = File(context.externalCacheDir, "cover")
        if (!tempDir.exists()) tempDir.mkdirs()
        val tempFile = File(tempDir, "embed_cover_${task.downloadSegment.segmentId}.jpg")

        // 完整存储路径
        val filePath = tempFile.absolutePath
        FileOutputStream(tempFile).use { outputStream ->
            outputStream.write(bytes)
            outputStream.flush()
        }
        task.updateRuntimeInfo(
            task.taskRuntimeInfo.copy(
                coverPath = filePath
            )
        )
    }


    /**
     * 需要内嵌的字幕下载（不区分语言）
     */
    private suspend fun handelEmbedCCDownload(task: AppDownloadTask): List<LocalSubtitle> {
        if (!task.downloadViewInfo.embedCC) return emptyList()
        val localSubtitles = mutableListOf<LocalSubtitle>()
        task.downloadViewInfo.videoPlayerInfoV2.data?.let { v2Info ->
            v2Info.subtitle.subtitles.forEach { subtitle ->
                val url = subtitle.finalSubtitleUrl
                val finalUrl = if (!url.contains("https")) "https:" else ""
                val language = subtitle.lan
                val langDoc = subtitle.lanDoc
                runCatching {
                    videoInfoRepository.getVideoCCInfo((finalUrl + url).toHttps())
                }.onSuccess { cCInfo ->
                    // 转化为Srt格式
                    val fileContentStr = CCJsonToSrt.jsonToSrt(cCInfo)
                    // 存储在临时文件夹cacheDir/cc
                    val tempDir = File(context.externalCacheDir, "cc")
                    if (!tempDir.exists()) tempDir.mkdirs()
                    val tempFile =
                        File(tempDir, "embed_cc_${task.downloadSegment.segmentId}_${language}.srt")
                    val filePath = tempFile.absolutePath
                    FileOutputStream(tempFile).use { outputStream ->
                        outputStream.write(fileContentStr.toByteArray(Charsets.UTF_8))
                        outputStream.flush()
                    }
                    localSubtitles.add(LocalSubtitle(language, langDoc = langDoc, filePath))
                }
            }
        }
        task.updateRuntimeInfo(
            task.taskRuntimeInfo.copy(
                subtitles = localSubtitles
            )
        )
        return localSubtitles
    }


    /**
     * 后置下载任务
     */
    private suspend fun handleSuccessor(task: AppDownloadTask, service: DownloadService) {

        // 处理下载资源
        handleDownloadResource(task, service)

        val segment = downloadTaskRepository.getSegmentBySegmentId(task.downloadSegment.segmentId)
        if (segment == null) {
            updateTaskState(task, DownloadState.ERROR)
            return
        }

        val lastFile = File(segment.savePath)
        val lastFileMimeType = getMimeType(task.downloadSegment.downloadMode)
        val lastFileName = getLastFileName(task, lastFileMimeType.split("/").lastOrNull() ?: "")
        val uriStr = moveToDownloadAndRegister(lastFile, lastFileName, lastFileMimeType)
        if (uriStr != null) {
            val newTask = task.copy(
                downloadSegment = segment.copy(
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


    private suspend fun handleDownloadResource(task: AppDownloadTask, service: DownloadService) {
        val progressCallback = createProgressCallback(task, service, "合并阶段")
        val tempOutputFile = createTempOutputFile(task)

        try {
            executeFfmpegMerge(task, tempOutputFile, progressCallback)
            updateTaskAndCleanup(task, tempOutputFile)
        } catch (e: Exception) {
            tempOutputFile.deleteIfExists()
            throw e
        }
    }

    // 创建临时输出文件
    private fun createTempOutputFile(task: AppDownloadTask): File {
        val saveDir = task.downloadSubTasks.first().savePath.substringBeforeLast("/")
        val extension =
            getMimeType(task.downloadSegment.downloadMode).substringAfterLast("/", "mp4")
        val timestamp = System.currentTimeMillis()

        return File(saveDir, "${task.downloadSegment.segmentId}_$timestamp.$extension").apply {
            parentFile?.mkdirs()
        }
    }

    private suspend fun getMediaDuration(task: AppDownloadTask): Long {
        return suspendCancellableCoroutine { continuation ->
            val firstInputFile = task.downloadSubTasks.firstOrNull()?.savePath

            if (firstInputFile.isNullOrBlank()) {
                Log.w("FFmpeg", "没有找到输入文件，使用数据库时长")
                continuation.resume(task.downloadSegment.duration ?: 0L)
                return@suspendCancellableCoroutine
            }

            try {
                val mediaInfoSession = FFprobeKit.getMediaInformation(firstInputFile)

                if (ReturnCode.isSuccess(mediaInfoSession.returnCode)) {
                    val mediaInfo = mediaInfoSession.mediaInformation

                    if (mediaInfo != null) {
                        // 获取时长（秒）
                        val durationInSeconds = mediaInfo.duration?.toDoubleOrNull() ?: 0.0
                        val durationInMillis = (durationInSeconds * 1000).toLong()

                        // 打印详细媒体信息用于调试
                        Log.d(
                            "FFmpeg", """
                        ========== 媒体信息 ==========
                        文件:  ${File(firstInputFile).name}
                        时长: ${durationInSeconds}s (${durationInMillis}ms)
                        格式: ${mediaInfo.format}
                        比特率: ${mediaInfo.bitrate}
                        大小: ${mediaInfo.size} bytes
                        视频流数:  ${mediaInfo.streams?.count { it.type == "video" } ?: 0}
                        音频流数: ${mediaInfo.streams?.count { it.type == "audio" } ?: 0}
                        =============================
                    """.trimIndent())

                        // 可选：打印流详细信息
                        mediaInfo.streams?.forEach { stream ->
                            Log.d(
                                "FFmpeg", """
                            流 #${stream.index}:
                              类型: ${stream.type}
                              编解码器: ${stream.codec}
                              ${if (stream.type == "video") "分辨率:  ${stream.width}x${stream.height}" else ""}
                              ${if (stream.type == "audio") "采样率: ${stream.sampleRate}Hz" else ""}
                        """.trimIndent()
                            )
                        }

                        continuation.resume(durationInMillis)
                    } else {
                        Log.w("FFmpeg", "无法获取媒体信息，使用数据库时长")
                        continuation.resume(task.downloadSegment.duration ?: 0L)
                    }
                } else {
                    Log.w(
                        "FFmpeg",
                        "FFprobe 执行失败:  ${mediaInfoSession.returnCode}, ${mediaInfoSession.failStackTrace}"
                    )
                    continuation.resume(task.downloadSegment.duration ?: 0L)
                }
            } catch (e: Exception) {
                continuation.resume(task.downloadSegment.duration ?: 0L)
            }
        }
    }

    // 执行 FFmpeg 合并
    private suspend fun executeFfmpegMerge(
        task: AppDownloadTask,
        outputFile: File,
        progressCallback: (Float) -> Unit
    ) {
        val commandString = buildFfmpegCommand(task, outputFile)
        val actualDuration = getMediaDuration(task)
        suspendCancellableCoroutine { continuation ->
            val session = FFmpegKit.executeAsync(
                commandString,
                { session ->
                    when {
                        ReturnCode.isSuccess(session.returnCode) -> {
                            if (!outputFile.exists() || outputFile.length() == 0L) {
                                continuation.resumeWithException(Exception("输出文件生成失败"))
                            } else {
                                Log.d("FFmpeg", "合并完成:  ${outputFile.absolutePath}")
                                continuation.resume(Unit)
                            }
                        }

                        ReturnCode.isCancel(session.returnCode) -> {
                            outputFile.deleteIfExists()
                            Log.w("FFmpeg", "任务被取消")
                            continuation.resumeWithException(CancellationException("任务被取消"))
                        }

                        else -> {
                            outputFile.deleteIfExists()
                            Log.e("FFmpeg", "执行失败: ${session.failStackTrace}")
                            continuation.resumeWithException(
                                Exception("FFmpeg 执行失败: ${session.failStackTrace} (rc: ${session.returnCode})")
                            )
                        }
                    }
                },
                { _ -> },
                { statistics ->
                    if (statistics.time > 0 && actualDuration > 0) {
                        val progress = statistics.time.toFloat() / actualDuration.toFloat()
                        val clampedProgress = progress.coerceIn(0f, 1f)
                        Log.d(
                            "FFmpeg",
                            "进度: ${(clampedProgress * 100).toInt()}% (${statistics.time}ms / ${actualDuration}ms)"
                        )
                        progressCallback(clampedProgress)
                    }
                }
            )
            continuation.invokeOnCancellation {
                Log.w("FFmpeg", "协程取消，停止 FFmpeg 会话")
                FFmpegKit.cancel(session.sessionId)
            }
        }
    }

    /**
     * 构建 FFmpeg 命令
     */
    private fun buildFfmpegCommand(task: AppDownloadTask, outputFile: File): String {
        val context = FfmpegCommandContext(task)
        return context.buildCommand(outputFile)
    }

    /**
     * FFmpeg 命令构建上下文
     */
    private data class FfmpegCommandContext(
        val task: AppDownloadTask
    ) {
        private val mediaInputs = task.downloadSubTasks.map { it.savePath }

        private val videoFileCount = task.downloadSubTasks.count {
            it.subTaskType == DownloadSubTaskType.VIDEO
        }
        private val audioFileCount = task.downloadSubTasks.count {
            it.subTaskType == DownloadSubTaskType.AUDIO
        }

        private val subtitles = task.taskRuntimeInfo.subtitles
        private val coverPath = task.taskRuntimeInfo.coverPath

        private val videoEnabled: Boolean
        private val audioEnabled: Boolean

        init {
            when (task.downloadSegment.downloadMode) {
                DownloadMode.VIDEO_ONLY -> {
                    videoEnabled = true
                    audioEnabled = false
                }

                DownloadMode.AUDIO_ONLY -> {
                    videoEnabled = false
                    audioEnabled = true
                }

                DownloadMode.AUDIO_VIDEO -> {
                    videoEnabled = true
                    audioEnabled = true
                }
            }
        }

        private val videoFileIdx = 0
        private val audioFileStartIdx = videoFileCount
        private val subFileStartIdx = mediaInputs.size
        private val coverIdx = if (coverPath.isNotBlank()) mediaInputs.size + subtitles.size else -1

        fun buildCommand(outputFile: File): String = buildList {
            addBaseParams()
            addInputFiles()
            addStreamMappings()
            addCodecConfig()
            addSubtitleMetadata()
            addCoverConfig()
            add(outputFile.absolutePath)
        }.joinToString(" ") { it.escape() }.also {
            // 打印完整命令用于调试
            Log.d("FFmpeg", "执行命令: $it")
        }

        private fun MutableList<String>.addBaseParams() {
            add("-y")
            add("-strict")
            add("-2")
        }

        private fun MutableList<String>.addInputFiles() {
            // 媒体文件
            mediaInputs.forEach {
                add("-i")
                add(it)
            }

            // 字幕文件
            subtitles.forEach {
                add("-i")
                add(it.path)
            }

            // 封面文件
            if (coverIdx >= 0) {
                add("-i")
                add(coverPath)
            }
        }

        private fun MutableList<String>.addStreamMappings() {
            // 视频流
            if (videoEnabled) {
                add("-map")
                add("$videoFileIdx:v:0")
            }

            // 音频流
            if (audioEnabled) {
                repeat(audioFileCount) { i ->
                    add("-map")
                    add("${audioFileStartIdx + i}:a:$i")
                }
            }

            // 字幕流
            subtitles.forEachIndexed { sIdx, _ ->
                add("-map")
                add("${subFileStartIdx + sIdx}:s:0")
            }

            // 封面流
            if (coverIdx >= 0) {
                add("-map")
                add("$coverIdx:v:0")
            }
        }

        private fun MutableList<String>.addCodecConfig() {
            if (videoEnabled) {
                add("-c:v")
                add("copy")
            }

            if (audioEnabled) {
                add("-c:a")
                add("copy")
            }
        }

        private fun MutableList<String>.addSubtitleMetadata() {
            if (subtitles.isEmpty() || !videoEnabled) return

            add("-c:s")
            add("mov_text")

            subtitles.forEachIndexed { sIdx, subtitle ->
                add("-metadata:s:s:$sIdx")
                add("language=${subtitle.lang}")
                add("-metadata:s:s:$sIdx")
                add("title=${subtitle.langDoc}")
            }
        }

        private fun MutableList<String>.addCoverConfig() {
            if (coverIdx < 0) return

            val coverStreamIndex = if (videoEnabled) 1 else 0
            add("-c:v:$coverStreamIndex")
            add("mjpeg")
            add("-disposition:v:$coverStreamIndex")
            add("attached_pic")
            add("-metadata:s:v:$coverStreamIndex")
            add("title=Cover")
        }

        private fun String.escape(): String = when {
            contains(" ") || contains("\"") || contains("'") -> {
                "\"${replace("\"", "\\\"")}\""
            }

            else -> this
        }
    }

    /**
     *  更新任务并清理临时文件
     */
    private suspend fun updateTaskAndCleanup(task: AppDownloadTask, outputFile: File) {
        downloadTaskRepository.updateSegment(
            task.downloadSegment.copy(savePath = outputFile.absolutePath)
        )
        task.downloadSubTasks.forEach {
            File(it.savePath).deleteIfExists()
        }
    }

    /**
     * 安全删除文件
     */
    private fun File.deleteIfExists() {
        if (exists()) {
            delete()
        }
    }

    /**
     * 获取最终导出的文件名
     */
    private suspend fun getLastFileName(task: AppDownloadTask, fileExtension: String): String {
        val videoNamingRule =
            if (task.downloadSegment.namingConventionInfo is NamingConventionInfo.Video) {
                appSettingsRepository.appSettingsFlow.first().videoNamingRule
            } else {
                appSettingsRepository.appSettingsFlow.first().bangumiNamingRule
            }
        val conventionInfo = task.downloadSegment.namingConventionInfo
        val fileExtension = fileExtension
        return buildFileNameWithConvention(videoNamingRule, conventionInfo, fileExtension)
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
            val newTask = task.copy(
                downloadSegment = task.downloadSegment.copy(
                    savePath = subTask.savePath,
                    downloadState = DownloadState.COMPLETED
                )
            )
            updateTaskState(newTask, DownloadState.COMPLETED)
            downloadTaskRepository.updateSegment(newTask.downloadSegment)
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

        val downloadUrl =
            getTaskDownloadUrl(task.downloadSegment, subTask.subTaskType, task.downloadViewInfo)
                ?: ""
        Log.d("downloadUrl", "下载地址: ${downloadUrl} -- ${subTask.subTaskType}")
        // 1. 获取远端文件长度，判断本地是否已经完整
        val remoteLength = try {
            val headResp = httpClient.head(downloadUrl) {
                header("Referer", buildRefererUrl(task))
            }
            headResp.headers["Content-Length"]?.toLongOrNull() ?: -1L
        } catch (e: Exception) {
            -1L // 获取失败也继续尝试下载
        }

        if (remoteLength > 0 && file.exists() && file.length() >= remoteLength) {
            onUpdateProgress(1f)
            return@withContext true
        }

        repeat(MAX_RETRY_ATTEMPTS) { attempt ->
            try {
                val success = performDownload(downloadUrl, task, tempFile, onUpdateProgress)
                if (success) {
                    if (file.exists()) file.delete()
                    tempFile.renameTo(file)
                    return@withContext true
                }
            } catch (e: CancellationException) {
                throw e // 协程取消，直接抛出
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
        downloadUrl: String?,
        task: AppDownloadTask,
        tempFile: File,
        onUpdateProgress: (Float) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        val downloaded = if (tempFile.exists()) tempFile.length() else 0L
        val referer = buildRefererUrl(task)

        val downloadUrl = buildLineDownloadUrl(downloadUrl ?: "")
        // 发起请求
        httpClient.prepareGet(downloadUrl) {
            header("Referer", referer)
            if (downloaded > 0) header("Range", "bytes=$downloaded-")
        }.execute { response ->
            val channel = response.bodyAsChannel()
            val total =
                response.contentLength()?.let { if (downloaded > 0) it + downloaded else it } ?: -1L

            if (total == 0L) {
                onUpdateProgress(1f)
                return@execute true
            }

            tempFile.parentFile?.mkdirs()
            val raf = RandomAccessFile(tempFile, "rw")
            if (downloaded > 0) raf.seek(downloaded)
            var currentDownloaded = downloaded
            val buffer = ByteArray(DOWNLOAD_BUFFER_SIZE)
            var lastEmit = 0L

            try {
                while (!channel.isClosedForRead) {
                    ensureActive() // 检查协程取消
                    val bytes = channel.readAvailable(buffer, 0, buffer.size)
                    if (bytes <= 0) break
                    raf.write(buffer, 0, bytes)
                    currentDownloaded += bytes
                    val now = System.currentTimeMillis()
                    if (now - lastEmit > 100) {
                        if (total > 0) onUpdateProgress(currentDownloaded.toFloat() / total)
                        lastEmit = now
                    }
                    yield() // 响应暂停/取消
                }
                if (total > 0) onUpdateProgress(1f)
            } finally {
                raf.close()
            }
        }

        return@withContext true
    }

    /**
     * 替换CDN
     */
    private suspend fun buildLineDownloadUrl(downloadUrl: String): String {
        val lineHost = appSettingsRepository.appSettingsFlow.first().biliLineHost ?: ""
        val uposRegex = Regex("""upos-sz-estg[0-9a-z]*\.bilivideo\.com""", RegexOption.IGNORE_CASE)
        return if (uposRegex.containsMatchIn(downloadUrl)) {
            if (lineHost.isNotEmpty()) {
                downloadUrl.replace(uposRegex, lineHost)
            } else downloadUrl
        } else {
            downloadUrl
        }
    }


    /**
     * 通用命名规则方法，支持所有类型变量替换并自动加后缀
     */
    private fun buildFileNameWithConvention(
        namingRule: String,
        conventionInfo: NamingConventionInfo?,
        fileSuffix: String
    ): String {
        var filePath = namingRule
        when (conventionInfo) {
            is NamingConventionInfo.Video -> {
                videoNamingRules.forEach { rule ->
                    val value = when (rule) {
                        FileNamePlaceholder.Video.Aid -> conventionInfo.aid ?: ""
                        FileNamePlaceholder.Video.Author -> conventionInfo.author ?: ""
                        FileNamePlaceholder.Video.BvId -> conventionInfo.bvId ?: ""
                        FileNamePlaceholder.Video.Cid -> conventionInfo.cid ?: ""
                        FileNamePlaceholder.Video.P -> conventionInfo.p ?: ""
                        FileNamePlaceholder.Video.PTitle -> conventionInfo.pTitle ?: ""
                        FileNamePlaceholder.Video.Title -> conventionInfo.title ?: ""
                        FileNamePlaceholder.Video.CollectionSeasonTitle -> conventionInfo.collectionSeasonTitle
                            ?: ""

                        FileNamePlaceholder.Video.CollectionTitle -> conventionInfo.collectionTitle
                            ?: ""
                    }.replace("/", "_")
                    filePath = filePath
                        .replace(rule.placeholder, value)
                        .replace(Regex("_+"), "_")
                        .replace(Regex("_+$"), "")
                }
            }

            is NamingConventionInfo.Donghua -> {
                donghuaNamingRules.forEach { rule ->
                    val value = when (rule) {
                        FileNamePlaceholder.Donghua.Cid -> conventionInfo.cid ?: ""
                        FileNamePlaceholder.Donghua.EpisodeNumber -> conventionInfo.episodeNumber
                            ?: ""

                        FileNamePlaceholder.Donghua.EpisodeTitle -> conventionInfo.episodeTitle
                            ?: ""

                        FileNamePlaceholder.Donghua.Title -> conventionInfo.title ?: ""
                        FileNamePlaceholder.Donghua.SeasonTitle -> conventionInfo.seasonTitle ?: ""
                    }.replace("/", "_")
                    filePath = filePath.replace(rule.placeholder, value)
                }
            }

            else -> {
                // 不支持的命名规则类型，返回原始命名规则
            }
        }
        // 自动加后缀
        if (!filePath.endsWith(".$fileSuffix")) filePath += ".${fileSuffix}"
        return filePath
    }

    /**
     * 处理合并成功
     */
    private suspend fun handleMergeSuccess(task: AppDownloadTask) {
        File(task.downloadSubTasks[0].savePath).delete()
        File(task.downloadSubTasks[1].savePath).delete()
        val newTask = task.copy(
            downloadSegment = task.downloadSegment.copy(
                savePath = "${task.downloadSubTasks[0].savePath}_merged.mp4",
                downloadState = DownloadState.COMPLETED
            )
        )
        updateTaskState(newTask, DownloadState.COMPLETED)
        downloadTaskRepository.updateSegment(newTask.downloadSegment)
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
     * 获取MIME类型
     */
    private fun getMimeType(mode: DownloadMode): String {
        return when (mode) {
            DownloadMode.AUDIO_VIDEO,
            DownloadMode.VIDEO_ONLY -> "video/mp4"

            DownloadMode.AUDIO_ONLY -> "video/mp3"
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
     * 将文件移动到下载目录并注册到媒体库，支持多级文件夹
     */
    private suspend fun moveToDownloadAndRegister(
        file: File,
        videoNamingRule: String,
        mimeType: String
    ): String? = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val parts = videoNamingRule.split("/")
        val fileName = parts.last()
        val folderPath = if (parts.size > 1) parts.dropLast(1).joinToString("/") else ""
        val relativePath = if (folderPath.isNotEmpty())
            Environment.DIRECTORY_DOWNLOADS + "/BILIBILIAS/" + folderPath
        else
            Environment.DIRECTORY_DOWNLOADS + "/BILIBILIAS"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
            }
            // 检查是否已存在同名文件，存在则先删除
            val query = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val selection =
                "${MediaStore.Downloads.DISPLAY_NAME}=? AND ${MediaStore.Downloads.RELATIVE_PATH}=?"
            val selectionArgs = arrayOf(fileName, relativePath)
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
            // Android Q以下，逐级创建文件夹
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            var targetDir = File(downloadsDir, "BILIBILIAS")
            if (!targetDir.exists()) targetDir.mkdirs()
            if (folderPath.isNotEmpty()) {
                folderPath.split("/").forEach { part ->
                    targetDir = File(targetDir, part)
                    if (!targetDir.exists()) targetDir.mkdirs()
                }
            }
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

        // 仅在下载媒体时更新任务列表
        if (!downloadViewInfo.downloadMedia) {
            return
        }

        _downloadTasks.value = currentTasks
    }


    /**
     * 下载并保存弹幕 XML（已精简）
     */
    private suspend fun downloadDanmakuForTask(task: AppDownloadTask) {
        val oid = task.downloadSegment.platformUniqueId.toLong()
        val title = getLastFileName(task, "xml")
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
        resolveDanmakuOutput(title).use { os ->
            os.write(xml.toByteArray(Charsets.UTF_8))
        }
    }

    /**
     * 创建弹幕输出流
     */
    private suspend fun resolveDanmakuOutput(title: String): OutputStream =
        withContext(Dispatchers.IO) {
            val fileName = "$title.xml"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val uri = context.contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    ContentValues().apply {
                        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                        put(MediaStore.Downloads.MIME_TYPE, "application/xml")
                        put(MediaStore.Downloads.RELATIVE_PATH, "Download/BILIBILIAS/Danmaku")
                    }
                ) ?: throw Exception("MediaStore insert failed")
                context.contentResolver.openOutputStream(uri)
            } else {
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "BILIBILIAS/Danmaku"
                ).apply { mkdirs() }
                FileOutputStream(File(dir, fileName))
            }.let { checkNotNull(it) { "OutputStream == null" } }
        }

    /**
     * 下载任务的封面图片
     */
    private suspend fun downloadCoverImageForTask(
        task: AppDownloadTask,
    ) {
        val type = task.cover?.substringAfterLast(".")
        val fileName = when (task.downloadTask.type) {
            DownloadTaskType.BILI_DONGHUA,
            DownloadTaskType.BILI_VIDEO -> "${task.downloadSegment.platformId}_pic.${type}"

            DownloadTaskType.BILI_VIDEO_SECTION if task.downloadSegment.taskId != null -> {
                val realTask =
                    downloadTaskRepository.getTaskById(task.downloadSegment.taskId ?: 0L)
                "${realTask?.platformId}_pic.${type}"
            }

            DownloadTaskType.BILI_VIDEO_SECTION -> error("封面所属任务类型异常")
        }
        downloadImageToAlbum(task.cover?.toHttps() ?: "", fileName, "BILIBILIAS")

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
            // 废除
            is BILIDonghuaPlayerInfo -> result.dash ?: result.durls?.firstOrNull {
                it.quality == downloadViewInfo.selectVideoQualityId
            }?.durl?.first() ?: result.durls?.first()?.durl?.first()
            // 废除
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

            else -> {
                null
            }
        } ?: return emptyList()

        // 根据segment的下载模式选择流
        val subTasks = when (videoData) {
            is BILIVideoDash -> {
                when (segment.downloadMode) {
                    DownloadMode.AUDIO_VIDEO -> {
                        listOf(
                            createSubTask(
                                segment,
                                DownloadSubTaskType.VIDEO
                            ),
                            createSubTask(
                                segment,
                                DownloadSubTaskType.AUDIO
                            )
                        )
                    }

                    DownloadMode.VIDEO_ONLY -> {
                        listOf(
                            createSubTask(
                                segment,
                                DownloadSubTaskType.VIDEO
                            )
                        )
                    }

                    DownloadMode.AUDIO_ONLY -> {
                        listOf(
                            createSubTask(
                                segment,
                                DownloadSubTaskType.AUDIO
                            )
                        )
                    }
                }
            }

            is BILIVideoDurl -> {
                downloadTaskRepository.updateSegment(
                    segment.copy(downloadMode = DownloadMode.VIDEO_ONLY)
                )
                listOf(
                    createSubTask(segment, DownloadSubTaskType.VIDEO)
                )
            }

            else -> throw IllegalStateException("不支持的下载数据类型")
        }

        delay(500L) // 避免请求过快
        return subTasks
    }


    /**
     * 获取任务视频下载地址
     */
    private suspend fun getTaskDownloadUrl(
        segment: DownloadSegment,
        subTaskType: DownloadSubTaskType,
        downloadViewInfo: DownloadViewInfo
    ): String? {

        val nodeType = downloadTaskRepository.getTaskNodeByNodeId(segment.nodeId)?.nodeType
            ?: throw IllegalStateException("无法获取任务节点类型")

        // 获取视频播放信息
        val videoInfo = when (nodeType) {
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

            // 废除
            is BILIDonghuaPlayerInfo -> result.dash ?: result.durls?.firstOrNull {
                it.quality == downloadViewInfo.selectVideoQualityId
            }?.durl?.first() ?: result.durls?.first()?.durl?.first()

            // 废除
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

            else -> {
                null
            }
        } ?: return null

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

            else -> throw IllegalStateException("不支持的下载数据类型")
        }

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
        type: DownloadSubTaskType
    ): DownloadSubTask {
        val savePath = getSaveTempSubTaskPath(
            subTaskType = type
        ) + "/${segment.platformId}_${System.currentTimeMillis()}.${
            when (type) {
                DownloadSubTaskType.VIDEO -> "mp4"
                DownloadSubTaskType.AUDIO -> "m4a"
            }
        }"
        return DownloadSubTask(
            segmentId = segment.segmentId,
            savePath = savePath,
            subTaskType = type,
            downloadState = DownloadState.WAITING
        )
    }
}
