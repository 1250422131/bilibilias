package com.imcys.bilibilias.base.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.model.task.DownloadTaskInfo
import com.imcys.bilibilias.base.model.task.deepCopy
import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.BROWSER_USER_AGENT
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.constant.USER_AGENT
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.extend.toAsFFmpeg
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.base.utils.file.hasSubDirectory
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.Protocol
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import java.util.zip.Inflater
import javax.inject.Inject
import javax.inject.Singleton

const val FLV_FILE = 1
const val DASH_FILE = 0

const val STATE_DOWNLOAD_WAIT = 0
const val STATE_DOWNLOADING = 1
const val STATE_DOWNLOAD_END = 2
const val STATE_DOWNLOAD_PAUSE = 3
const val STATE_MERGE = 4
const val STATE_MERGE_END = 5

// 非正常结束
const val STATE_DOWNLOAD_ERROR = -1
const val STATE_MERGE_ERROR = -1

// 定义一个下载队列类
@Singleton
class DownloadQueue @Inject constructor(
    @ApplicationContext val context: Context,
    private val networkService: NetworkService,
) {

    private val groupTasksMap: MutableMap<Long, MutableList<DownloadTaskInfo>> = mutableMapOf()

    // 存储待下载的任务
    private val queue = mutableListOf<DownloadTaskInfo>()

    // 当前正在下载的任务
    private val currentTasks = mutableListOf<DownloadTaskInfo>()

    // 创建 ktor client
    private val client = HttpClient(OkHttp) {
        engine {
            config {
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
                protocols(listOf(Protocol.HTTP_1_1))

                // 设置 OkHttp 的内存管理
                dispatcher(Dispatcher().apply {
                    maxRequestsPerHost = 1
                    maxRequests = 2
                })
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

        // 配置客户端内存使用
        engine {
            pipelining = false
        }
    }

    // 下载进度回调
    data class Progress(
        val downloadedBytes: Long,
        val totalBytes: Long,
        val progress: Double
    )

    // 创建进度更新通道,用于控制进度更新频率
    private val progressChannel = Channel<Pair<DownloadTaskInfo, Double>>(Channel.CONFLATED)

    // 使用 StateFlow 管理下载任务状态
    private val _downloadTasks = MutableStateFlow<List<DownloadTaskInfo>>(emptyList())
    val downloadTasks = _downloadTasks.asStateFlow()

    // 使用 StateFlow 管理已完成任务状态
    private val _finishedTasks = MutableStateFlow<List<DownloadFinishTaskInfo>>(emptyList())
    val finishedTasks = _finishedTasks.asStateFlow()

    // 添加取消标志
    private val cancelFlags = mutableMapOf<String, Boolean>()

    init {
        // 启动进度更新协程
        launchIO {
            for ((task, progress) in progressChannel) {
                updateProgress(task, progress)
                // 添加小延迟避免更新太频繁
                delay(100)
            }
        }
    }

    // 获取任务的唯一标识
    private fun getTaskKey(task: DownloadTaskInfo): String {
        return "${task.url}_${task.savePath}"
    }

    // 取消下载任务
    fun cancelTask(task: DownloadTaskInfo) {
        cancelFlags[getTaskKey(task)] = true


        if (currentTasks.find { it.savePath == task.savePath } != null) {
            if (task.isGroupTask) {
                val groupTasks = groupTasksMap[task.downloadTaskDataBean.cid]
                groupTasks?.forEach {
                    cancelFlags[getTaskKey(it)] = true
                    currentTasks.remove(it)
                    it.state = STATE_DOWNLOAD_ERROR
                    it.progress = 0.0
                }
                groupTasksMap.remove(task.downloadTaskDataBean.cid)
            } else {
                currentTasks.remove(task)
                task.state = STATE_DOWNLOAD_ERROR
                task.progress = 0.0
            }
        } else {
            if (task.isGroupTask) {
                val groupTasks = groupTasksMap[task.downloadTaskDataBean.cid]
                groupTasks?.forEach {
                    cancelFlags[getTaskKey(it)] = true
                    queue.remove(it)
                    it.state = STATE_DOWNLOAD_ERROR
                    it.progress = 0.0
                }
                groupTasksMap.remove(task.downloadTaskDataBean.cid)
            } else {
                queue.remove(task)
            }
        }

        updateTasks()
        executeTask()
    }

    // 修改下载文件函数，添加取消检查
    private fun downloadFile(
        url: String,
        file: File,
        headers: Map<String, String>,
        task: DownloadTaskInfo
    ): Flow<Progress> = flow {
        val taskKey = getTaskKey(task)
        cancelFlags[taskKey] = false

        client.prepareGet(url) {
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }.execute { response ->
            val channel = response.bodyAsChannel()
            val totalBytes = response.contentLength() ?: 0L

            file.parentFile?.mkdirs()

            val bufferSize = 8192 // 8KB buffer
            var lastEmitTime = 0L

            file.outputStream().buffered(bufferSize).use { output ->
                var downloadedBytes = 0L
                val buffer = ByteArray(bufferSize)

                while (!channel.isClosedForRead) {
                    if (cancelFlags[taskKey] == true) {
                        throw CancellationException("Download cancelled")
                    }

                    val bytes = channel.readAvailable(buffer, 0, bufferSize)
                    if (bytes < 0) break

                    output.write(buffer, 0, bytes)
                    output.flush()

                    downloadedBytes += bytes

                    // 每100ms更新一次进度
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastEmitTime >= 100) {
                        val progress = (downloadedBytes.toDouble() / totalBytes) * 100
                        emit(Progress(downloadedBytes, totalBytes, progress))
                        lastEmitTime = currentTime
                    }

                    kotlinx.coroutines.delay(1)
                }

                // 确保发送最终进度
                emit(Progress(downloadedBytes, totalBytes, 100.0))
            }
        }
    }.flowOn(Dispatchers.IO)

    // 添加下载任务到队列中
    fun addTask(
        url: String,
        savePath: String,
        fileType: Int,
        downloadTaskDataBean: DownloadTaskDataBean,
        isGroupTask: Boolean = true,
        onComplete: (Boolean) -> Unit,
    ) {
        // 检查是否存在相同的任务
        val isDuplicate = checkDuplicateTask(url, savePath, downloadTaskDataBean.cid)
        if (isDuplicate) {
            launchUI {
                asToast(context, "该任务已在下载队列中")
            }
            return
        }

        // 创建一个下载任务
        val task = DownloadTaskInfo(
            url,
            savePath,
            fileType,
            downloadTaskDataBean,
            isGroupTask = isGroupTask,
            onComplete,
        )

        if (task.isGroupTask) {
            // 在map中找到这个任务所属的一组任务
            val groupTasks = groupTasksMap[task.downloadTaskDataBean.cid]
            if (groupTasks == null) {
                // 创建一个新的任务列表
                val newGroupTasks = mutableListOf<DownloadTaskInfo>()
                newGroupTasks.add(task)
                groupTasksMap[task.downloadTaskDataBean.cid] = newGroupTasks
            } else {
                groupTasks.add(task)
            }
        }

        queue.add(task)
        updateTasks() // 更新任务列表状态

        if (queue.isNotEmpty()) {
            executeTask()
        }
    }

    // 检查是否存在重复任务
    private fun checkDuplicateTask(url: String, savePath: String, cid: Long): Boolean {
        // 检查当前下载中的任务
        val inCurrentTasks = currentTasks.any { task ->
            task.url == url ||
                    task.savePath == savePath
        }
        if (inCurrentTasks) return true

        // 检查队列中的任务
        val inQueue = queue.any { task ->
            task.url == url ||
                    task.savePath == savePath
        }
        if (inQueue) return true

        // 检查分组任务 - 只检查具体的URL和路径，不检查cid
        val groupTasks = groupTasksMap[cid]
        if (groupTasks != null) {
            val inGroup = groupTasks.any { task ->
                task.url == url || task.savePath == savePath
            }
            if (inGroup) return true
        }

        return false
    }

    // 修改执行任务函数
    private fun executeTask() {
        // 如果当前任务已满或队列为空，直接返回
        if (currentTasks.size >= 2 || queue.isEmpty()) return

        // 获取但不立即移除任务
        val mTask = queue.first()

        if (mTask.isGroupTask) {
            // 如果是组任务，检查是否有配对的任务
            val groupTasks = groupTasksMap[mTask.downloadTaskDataBean.cid]
            if (groupTasks != null && groupTasks.size == 2 && queue.containsAll(groupTasks)) {
                // 一次性移除两个任务
                queue.removeAll(groupTasks)
                // 启动组任务下载
                startGroupDownload(groupTasks)
            }
        } else {
            // 单个任务直接下载
            queue.removeAt(0)
            startSingleDownload(mTask)
        }
    }

    private fun startGroupDownload(groupTasks: List<DownloadTaskInfo>) {
        groupTasks.forEach { task ->
            task.state = STATE_DOWNLOADING
            currentTasks.add(task)
            startDownload(task)
        }
        updateTasks()
    }

    private fun startSingleDownload(task: DownloadTaskInfo) {
        task.state = STATE_DOWNLOADING
        currentTasks.add(task)
        startDownload(task)
        updateTasks()
    }

    private fun startDownload(mTask: DownloadTaskInfo) {
        val fileRegex = ".+/(.+)\$"
        val rFile = Pattern.compile(fileRegex)
        val m = rFile.matcher(mTask.savePath)
        val fileName = if (m.find()) m.group(1) ?: "" else ""
        val filePath = mTask.savePath.replace("/$fileName", "")
        val file = File(filePath, fileName)

        val headers = mapOf(
            USER_AGENT to BROWSER_USER_AGENT,
            REFERER to "https://www.bilibili.com/",
            COOKIE to (BaseApplication.dataKv.decodeString(COOKIES, "") ?: "")
        )

        launchIO {
            try {
                downloadFile(mTask.url, file, headers, mTask)
                    .collect { progress ->
                        withContext(Dispatchers.Main) {
                            mTask.fileSize = (progress.downloadedBytes / 1048576).toDouble()
                            mTask.fileDlSize = (progress.totalBytes / 1048576).toDouble()
                            mTask.progress = progress.progress
                            updateTasks()
                        }
                    }

                withContext(Dispatchers.Main) {
                    mTask.state = STATE_DOWNLOAD_END
                    mTask.progress = 100.0
                    updateTasks()

                    if (mTask.isGroupTask) {
                        val groupTasks = groupTasksMap[mTask.downloadTaskDataBean.cid]
                        val isGroupTasksCompleted = groupTasks?.all {
                            it.state == STATE_DOWNLOAD_END
                        } ?: false

                        if (isGroupTasksCompleted) {
                            launchIO {
                                videoDataSubmit(mTask)
                                videoMerge(mTask)

                                withContext(Dispatchers.Main) {
                                    groupTasks?.forEach { task ->
                                        currentTasks.remove(task)
                                    }
                                    groupTasksMap.remove(mTask.downloadTaskDataBean.cid)
                                    updateTasks()
                                    executeTask()
                                }
                            }
                        }
                    } else {
                        moveFileToDlUriPath(mTask.savePath)
                        currentTasks.remove(mTask)
                        mTask.onComplete(true)

                        launchIO {
                            saveFinishTask(mTask)
                            videoDataSubmit(mTask)
                            updatePhotoMedias(context, file)
                        }
                        updateTasks()
                        executeTask()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    when (e) {
                        is CancellationException -> {
                            currentTasks.remove(mTask)
                        }

                        else -> {
                            if (mTask.isGroupTask) {
                                val groupTasks = groupTasksMap[mTask.downloadTaskDataBean.cid]
                                groupTasks?.forEach {
                                    currentTasks.remove(it)
                                    it.state = STATE_DOWNLOAD_ERROR
                                    it.onComplete(false)
                                }
                            } else {
                                currentTasks.remove(mTask)
                                mTask.state = STATE_DOWNLOAD_ERROR
                                mTask.onComplete(false)
                            }
                            Log.e("DownloadQueue", "Download error: ${e.message}", e)
                        }
                    }
                    updateTasks()
                    executeTask()
                }
            }
        }
    }

    /**
     * 储存完成的下载任务
     * @param task Task
     */
    private fun saveFinishTask(task: DownloadTaskInfo) {
        launchIO {
            var videoTitle = ""
            var videoPageTitle = ""
            var avid = 0L
            var cid = 0L
            var videoBvid = ""
            task.downloadTaskDataBean.bangumiSeasonBean?.apply {
                videoTitle = share_copy
                videoPageTitle = long_title
                avid = aid
                cid = this.cid
                videoBvid = bvid
            }

            task.downloadTaskDataBean.videoPageDataData?.apply {
                videoTitle = part
                videoPageTitle = part
                avid = this.cid
                cid = this.cid
                videoBvid = NewVideoNumConversionUtils.av2bv(avid)
            }

            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val value = sharedPreferences.getString(
                "user_download_save_uri_path",
                null,
            )

            var safPath = ""
            val path = if (value == null) {
                task.savePath
            } else {
                val documentFile =
                    DocumentFile.fromSingleUri(context, Uri.parse(value))!!
                safPath = (documentFile.uri.toString() + Uri.encode(
                    task.savePath.replace(
                        "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download",
                        ""
                    )
                ))

                "/storage/emulated/0/" + documentFile.uri.path?.replace(
                    Regex("/tree/.*:"),
                    ""
                )!! + task.savePath.replace(
                    "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download",
                    ""
                )
            }

            val downloadFinishTaskInfo = DownloadFinishTaskInfo(
                videoTitle = videoTitle,
                videoPageTitle = videoPageTitle,
                videoAvid = avid,
                videoBvid = videoBvid,
                videoCid = cid,
                savePath = path,
                safPath = safPath,
                fileType = task.fileType,
            )

            val downloadFinishTaskDao =
                AppDatabase.getDatabase(context).downloadFinishTaskDao()

            // 协程提交
            DownloadFinishTaskRepository(downloadFinishTaskDao).apply {
                insert(downloadFinishTaskInfo)
                // 更新已完成任务状态
                updateFinishedTasks(allDownloadFinishTask())
            }
        }
    }

    /**
     * 储存完成的下载任务集合
     * @param tasks Array<out Task>
     */
    private fun saveFinishTask(vararg tasks: DownloadTaskInfo) {
        tasks.forEach {
            saveFinishTask(it)
        }
    }

    /**
     * 数据解析
     * @param task Task
     */
    private fun videoDataSubmit(task: DownloadTaskInfo) {
        var aid: Long? = task.downloadTaskDataBean.bangumiSeasonBean?.cid

        launchUI {
            val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")

            val videoBaseBean = networkService.getVideoBaseBean(task.downloadTaskDataBean.bvid)
            val mid = videoBaseBean.data.owner.mid
            val name = videoBaseBean.data.owner.name
            val copyright = videoBaseBean.data.copyright
            val tName = videoBaseBean.data.tname

            if (aid == null) {
                aid = videoBaseBean.data.aid
            }

            addAsVideoData(
                task.downloadTaskDataBean.bvid,
                aid!!,
                mid,
                name,
                copyright,
                tName,
            )
        }
    }

    /**
     * 提交解析视频数据
     * @param bvid String?
     * @param aid Int
     * @param name String?
     * @param copyright Int
     * @param tName String?
     */
    private fun addAsVideoData(
        bvid: String?,
        aid: Long,
        mid: Long,
        name: String?,
        copyright: Int,
        tName: String?,
    ) {
        // 通知缓存成功
        Analytics.trackEvent(context.getString(R.string.app_download_queue_cachesuccessful))
        // Need to translate ?
        StatService.onEvent(
            context,
            "CacheSuccessful",
            context.getString(R.string.app_download_queue_cachesuccessful)
        )

        launchIO {
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            val microsoftAppCenterType =
                sharedPreferences.getBoolean("microsoft_app_center_type", true)
            val baiduStatisticsType =
                sharedPreferences.getBoolean("baidu_statistics_type", true)

            val myUserData = networkService.getMyUserData()

            // 提交数据
            if (!microsoftAppCenterType && !baiduStatisticsType) {
                networkService.postAsData(aid, bvid, mid, name, tName, copyright)
                "${BiliBiliAsApi.appAddAsVideoData}?Aid=$aid&Bvid=$bvid&Mid=$mid&Upname=$name&Tname=$tName&Copyright=$copyright"
            } else {
                networkService.postAsData(
                    aid,
                    bvid,
                    mid,
                    name,
                    tName,
                    copyright,
                    myUserData.data.uname,
                    myUserData.data.mid
                )
            }
        }
    }

    /**
     * 参数合并
     * @param cid Int
     */
    private fun videoMerge(mTask: DownloadTaskInfo) {

        val cid = mTask.downloadTaskDataBean.cid

        val mergeState =
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                "user_dl_finish_automatic_merge_switch",
                true,
            )
        val importState =
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                "user_dl_finish_automatic_import_switch",
                false,
            )


        val taskMutableList = groupTasksMap[cid]
        val videoTask =
            taskMutableList?.lastOrNull { it.fileType == 0 }
        val audioTask =
            taskMutableList?.lastOrNull { it.fileType == 1 }

        if (mergeState) {
            // 耗时操作，这里直接开个新线程
            val videoPath =
                videoTask!!.savePath
            val audioPath =
                audioTask!!.savePath
            // 这里的延迟是为了有足够时间让下载检查下载完整
            runFFmpegRxJavaVideoMerge(
                videoTask,
                videoPath,
                audioPath,
            )

            // 旧的合并方案： MediaExtractorUtils.combineTwoVideos(audioPath, 0,videoPath,mergeFile)
        } else if (importState) {
            videoTask!!.downloadTaskDataBean.bangumiSeasonBean?.apply {
                // 分别添加下载完成了
                saveFinishTask(videoTask, audioTask!!)
                importVideo(cid)
            }
            updateVideoMergeOrImportTask(videoTask, STATE_DOWNLOAD_END, true)
            executeTask()
        } else {
            // 这类代表虽然是dash下载，但是并不需要其他操作
            moveFileToDlUriPath(videoTask!!.savePath)
            moveFileToDlUriPath(audioTask!!.savePath)
            saveFinishTask(videoTask, audioTask)
            // 移除任务
            updateVideoMergeOrImportTask(mTask, STATE_DOWNLOAD_END, true)
            executeTask()
        }
    }

    private fun updateVideoMergeOrImportTask(
        task: DownloadTaskInfo,
        state: Int,
        downloadState: Boolean
    ) {
        // 更新任务状态
        task.state = state
        // 下载成功，调用任务的完成回调
        task.onComplete(downloadState)
        // 移除任务
        groupTasksMap[task.downloadTaskDataBean.cid]?.forEach { item ->
            currentTasks.remove(item)
        }
        // 更新
        updateTasks()
    }

    private fun runFFmpegRxJavaVideoMerge(
        task: DownloadTaskInfo,
        videoPath: String,
        audioPath: String,

        ) {
        val userDLMergeCmd =
            PreferenceManager.getDefaultSharedPreferences(context).getString(
                "user_dl_merge_cmd_editText",
                "ffmpeg -i {VIDEO_PATH} -i {AUDIO_PATH} -c copy {VIDEO_MERGE_PATH}",
            )

        File(videoPath + "_merge.mp4")

        val commands = userDLMergeCmd?.toAsFFmpeg(
            videoPath,
            audioPath,
            videoPath + "_merge.mp4",
        )
        // context.getFileDir().getPath()
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(object : RxFFmpegSubscriber() {
                override fun onError(message: String?) {
                    updateVideoMergeOrImportTask(task, STATE_MERGE_ERROR, false)
                    asToast(
                        context,
                        context.getString(R.string.app_download_queue_merge_error)
                    )
                    FileUtils.deleteFile(videoPath)
                    FileUtils.deleteFile(audioPath)
                    FileUtils.deleteFile(videoPath + "_merge.mp4")

                    runOnUiThread {
                        asToast(context, "合并错误,请重新下载，已经删除下载文件。")
                    }
                    executeTask()
                }

                override fun onFinish() {
                    asToast(
                        context,
                        context.getString(R.string.app_download_queue_merge_finish)
                    )
                    runOnUiThread {
                        asToast(context, "合并完成")
                    }
                    // 删除合并文件
                    val deleteMergeSatae =
                        PreferenceManager.getDefaultSharedPreferences(context)
                            .getBoolean(
                                "user_dl_finish_delete_merge_switch",
                                true,
                            )
                    if (deleteMergeSatae) {
                        FileUtils.deleteFile(videoPath)
                        FileUtils.deleteFile(audioPath)
                        // 仅仅储存视频地址
                        task.savePath = videoPath + "_merge.mp4"
                        task.fileType = 0
                        moveFileToDlUriPath(task.savePath)
                        saveFinishTask(task)
                    } else {
                        // 分别储存两次下载结果
                        val videoTask = task
                        val audioTask = task
                        videoTask.fileType = 0
                        audioTask.savePath = audioPath
                        audioTask.fileType = 1
                        // 分别移动视频和音频
                        moveFileToDlUriPath(videoPath)
                        moveFileToDlUriPath(audioPath)
                        moveFileToDlUriPath(videoPath + "_merge.mp4")

                        saveFinishTask(videoTask, audioTask)

                    }
                    updateVideoMergeOrImportTask(task, STATE_DOWNLOAD_END, true)

                    // 继续下一个
                    executeTask()
                }

                override fun onProgress(progress: Int, progressTime: Long) {
                    // 更新任务状态
                    task.state = STATE_MERGE
                    updateProgress(task, progress.toDouble())

                }

                override fun onCancel() {
                }
            })
    }

    /**
     * 缓存导回B站观看
     * @param cid Int
     */
    private fun importVideo(cid: Long) {
        val VIDEO_TYPE = 1
        val BANGUMI_TYPE = 2
        val taskMutableList = groupTasksMap[cid]

        val videoTask =
            taskMutableList?.filter { it.fileType == 0 }
        val audioTask =
            taskMutableList?.filter { it.fileType == 1 }

        var bvid = ""
        var type = VIDEO_TYPE
        val downloadTaskDataBean = videoTask!![0].downloadTaskDataBean
        var displayDesc: String? = "1080P"

        var pageThisNum: Int? = 0

        var shareUrl: String? = ""

        downloadTaskDataBean.bangumiSeasonBean?.apply {
            bvid = this.bvid
            type = BANGUMI_TYPE
            // av过滤
            // No need to translate ?
            val pageRegex = Regex("""(?<=(第))([0-9]+)""")
            pageThisNum = if (pageRegex.containsMatchIn(share_copy)) {
                pageRegex.find(
                    share_copy,
                )?.value!!.toInt()
            } else {
                TODO()
            }

            shareUrl = share_url

            downloadTaskDataBean.dashBangumiPlayBean?.result?.support_formats?.forEach {
                if (it.quality.toString() == downloadTaskDataBean.qn) displayDesc = it.display_desc
            }
        } ?: downloadTaskDataBean.videoPageDataData?.apply {
            bvid = NewVideoNumConversionUtils.av2bv(this.cid)
            type = VIDEO_TYPE
            downloadTaskDataBean.dashVideoPlayBean?.data?.support_formats?.forEach {
                if (it.quality.toString() == downloadTaskDataBean.qn) displayDesc = it.display_desc
            }
        }

        // 临时bangumiEntry -> 只对番剧使用
        var videoEntry = context.getString(R.string.BangumiEntry)
        var videoIndex = context.getString(R.string.VideoIndex)
        val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")

        launchUI {
            val videoBaseBean = networkService.getVideoBaseBean(bvid)
            if (videoBaseBean.code == 0) {
                videoEntry = videoEntry.replace("UP主UID", videoBaseBean.data.owner.mid.toString())
                videoEntry = videoEntry.replace("UP名称", videoBaseBean.data.owner.name)
                videoEntry = videoEntry.replace("UP头像", videoBaseBean.data.owner.face)
                videoEntry = videoEntry.replace("AID编号", videoBaseBean.data.aid.toString())
                videoEntry = videoEntry.replace("BVID编号", bvid)
                videoEntry = videoEntry.replace("CID编号", videoBaseBean.data.cid.toString())
                videoEntry = videoEntry.replace("下载标题", videoBaseBean.data.title + ".mp4")

                videoEntry = videoEntry.replace("文件名称", videoBaseBean.data.title + ".mp4")
                videoEntry = videoEntry.replace("标题", videoBaseBean.data.title)
                videoEntry = videoEntry.replace("子集号", pageThisNum.toString())
                videoEntry = videoEntry.replace("子集索引", (pageThisNum!! - 1).toString())
                videoEntry = videoEntry.replace("排序号", (2000000 + pageThisNum!!).toString())
                videoEntry = videoEntry.replace("下载子TITLE", downloadTaskDataBean.pageTitle)

                videoEntry =
                    videoEntry.replace(
                        "LINK地址",
                        videoBaseBean.data.redirect_url.replace("/", "\\/")
                    )

                val width: Int?
                val timeLength: Int?
                val height = when (type) {
                    VIDEO_TYPE -> {
                        timeLength = downloadTaskDataBean.dashVideoPlayBean?.data?.timelength
                        width = downloadTaskDataBean.videoPageDataData?.dimension?.width
                        downloadTaskDataBean.videoPageDataData?.dimension?.height
                    }

                    BANGUMI_TYPE -> {
                        timeLength =
                            downloadTaskDataBean.dashBangumiPlayBean?.result?.timelength
                        width = downloadTaskDataBean.bangumiSeasonBean?.dimension?.width
                        downloadTaskDataBean.bangumiSeasonBean?.dimension?.height
                    }

                    else -> {
                        TODO("判断错误")
                    }
                }
                videoEntry = videoEntry.replace("高度", height.toString())
                videoEntry = videoEntry.replace("宽度", width.toString())
                videoEntry = videoEntry.replace("QN编码", downloadTaskDataBean.qn)
                videoEntry = if (downloadTaskDataBean.qn == "112") {
                    videoEntry.replace(
                        "码率",
                        "高码率",
                    )
                } else {
                    videoEntry.replace("码率", "")
                }

                videoEntry = videoEntry.replace("总时间", timeLength.toString())

                videoEntry =
                    videoEntry.replace("弹幕数量", videoBaseBean.data.stat.danmaku.toString())
                videoEntry = videoEntry.replace("下载子标题", downloadTaskDataBean.pageTitle)

                val dashAudioSize = AppFilePathUtils.getFileSize(audioTask!![0].savePath)
                val dashVideoSize = AppFilePathUtils.getFileSize(videoTask[0].savePath)

                videoEntry =
                    videoEntry.replace("封面地址", videoBaseBean.data.pic.replace("/", "\\/"))
                videoEntry = videoEntry.replace("下载大小", dashVideoSize.toString())
                videoIndex = videoIndex.replace("视频大小", dashVideoSize.toString())
                videoIndex = videoIndex.replace("高度", height.toString())
                videoIndex = videoIndex.replace("宽度", width.toString())
                videoEntry = videoEntry.replace("清晰度", displayDesc!!)
                videoIndex = videoIndex.replace("QN编码", downloadTaskDataBean.qn)
                videoIndex = videoIndex.replace("音频大小", dashAudioSize.toString())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (type == BANGUMI_TYPE) {
                        safImpVideo(
                            videoTask[0],
                            videoTask[0].savePath,
                            audioTask[0].savePath,
                            videoEntry,
                            videoIndex,
                            downloadTaskDataBean,
                            videoBaseBean,
                        )
                    }
                } else {
                    if (type == BANGUMI_TYPE) {
                        fileImpVideo(
                            videoTask[0],
                            videoTask[0].savePath,
                            audioTask[0].savePath,
                            videoEntry,
                            videoIndex,
                            downloadTaskDataBean,
                            videoBaseBean,
                        )
                    }
                }
            }
        }
    }

    private fun fileImpVideo(
        task: DownloadTaskInfo,
        videoPath: String,
        audioPath: String,
        videoEntry: String,
        videoIndex: String,
        downloadTaskDataBean: DownloadTaskDataBean,
        videoBaseBean: VideoBaseBean,
    ) {
        var videoEntry = videoEntry
        val epidUrl = videoBaseBean.data.redirect_url
        // av过滤
        val epRegex = Regex("""(?<=(ep))([0-9]+)""")
        val epid = if (epRegex.containsMatchIn(epidUrl)) {
            epRegex.find(
                epidUrl,
            )?.value!!.toLong()
        } else {
            TODO()
        }
        launchIO {

            val bangumiSeasonBean = networkService.getBangumiSeasonBeanByEpid(epid)

            val ssid = bangumiSeasonBean.result.season_id
            videoEntry =
                videoEntry.replace("SSID编号", (bangumiSeasonBean.result.season_id).toString())
            videoEntry = videoEntry.replace("EPID编号", epid.toString())


            val danmakuByte = networkService.getDanmuBytes(downloadTaskDataBean.cid)


            val bufferedSink: BufferedSink?
            val dest =
                File("/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/danmaku.json")
            if (!dest.exists()) dest.createNewFile()
            val sink = dest.sink() // 打开目标文件路径的sink
            val decompressBytes =
                decompress(danmakuByte) // 调用解压函数进行解压，返回包含解压后数据的byte数组
            bufferedSink = sink.buffer()
            decompressBytes.let { it -> bufferedSink.write(it) } // 将解压后数据写入文件（sink）中
            bufferedSink.close()

            FileUtils.fileWrite(
                "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/entry.json",
                videoEntry,
            )
            FileUtils.fileWrite(
                "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/${downloadTaskDataBean.qn}/index.json",
                videoIndex,
            )

            AppFilePathUtils.copyFile(
                videoPath,
                "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/${downloadTaskDataBean.qn}/video.m4s",
            )
            AppFilePathUtils.copyFile(
                audioPath,
                "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/${downloadTaskDataBean.qn}/audio.m4s",
            )

            val impFileDeleteState =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(
                        "user_dl_delete_import_file_switch",
                        true,
                    )

            if (impFileDeleteState) {
                FileUtils.deleteFile(videoPath)
                FileUtils.deleteFile(audioPath)
                task.savePath =
                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/${downloadTaskDataBean.qn}/video.m4s"
                // 分别储存两次下载结果
                saveFinishTask(task)
                task.savePath =
                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/${downloadTaskDataBean.qn}/audio.m4s"
                task.fileType = 1
                saveFinishTask(task)
            } else {
                // 分别储存两次下载结果
                saveFinishTask(task)
                task.savePath = audioPath
                task.fileType = 1
                saveFinishTask(task)
            }
        }
    }

    private fun safImpVideo(
        task: DownloadTaskInfo,
        videoPath: String,
        audioPath: String,
        videoEntry: String,
        videoIndex: String,
        downloadTaskDataBean: DownloadTaskDataBean,
        videoBaseBean: VideoBaseBean,
    ) {
        launchIO {
            var videoEntry = videoEntry
            val appDataUri =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getString("AppDataUri", "")

            val saf = DocumentFile.fromTreeUri(context, Uri.parse(appDataUri))

            val biliBiliDocument = saf?.findFile("download") ?: run {
                saf?.createDirectory("download")
            }

            val epidUrl = videoBaseBean.data.redirect_url
            // av过滤
            val epRegex = Regex("""(?<=(ep))([0-9]+)""")
            val epid = if (epRegex.containsMatchIn(epidUrl)) {
                epRegex.find(
                    epidUrl,
                )?.value!!.toLong()
            } else {
                TODO()
            }

            val bangumiSeasonBean = networkService.getBangumiSeasonBeanByEpid(epid)

            val ssid = bangumiSeasonBean.result.season_id
            videoEntry =
                videoEntry.replace("SSID编号", (bangumiSeasonBean.result.season_id).toString())
            videoEntry = videoEntry.replace("EPID编号", epid.toString())

            val ssidDocument = biliBiliDocument?.findFile("s_$ssid") ?: run {
                biliBiliDocument?.createDirectory("s_$ssid")
            }

            val epidDocument =
                ssidDocument?.createDirectory(epid.toString())

            val qnDocument =
                epidDocument?.createDirectory(downloadTaskDataBean.qn)

            // 创建文件
            val danmakuDocument = epidDocument?.createFile("application/xml", "danmaku")
            val entryDocument = epidDocument?.createFile("application/json", "entry")
            FileUtils.fileWrite(
                context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/entry.json",
                videoEntry,
            )

            FileUtils.fileWrite(
                context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/index.json",
                videoIndex,
            )
            val danmakuByte = networkService.getDanmuBytes(downloadTaskDataBean.cid)


            val bufferedSink: BufferedSink?
            val dest = File(
                context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/danmaku.json",
            )
            if (!dest.exists()) dest.createNewFile()
            val sink = dest.sink() // 打开目标文件路径的sink
            val decompressBytes =
                decompress(danmakuByte) // 调用解压函数进行解压，返回包含解压后数据的byte数组
            bufferedSink = sink.buffer()
            decompressBytes.let { it -> bufferedSink.write(it) } // 将解压后数据写入文件（sink）中
            withContext(Dispatchers.IO) {
                bufferedSink.close()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppFilePathUtils.copySafFile(
                    context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/danmaku.json",
                    danmakuDocument?.uri,
                    context,
                )
                AppFilePathUtils.copySafFile(
                    context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/entry.json",
                    entryDocument?.uri,
                    context,
                )
            }

            val indexDocument =
                qnDocument?.createFile("application/json", "index.json")
            val videoDocument =
                qnDocument?.createFile("application/m4s", "video.m4s")
            val audioDocument =
                qnDocument?.createFile("application/m4s", "audio.m4s")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppFilePathUtils.copySafFile(
                    videoPath,
                    videoDocument?.uri,
                    context,
                )
                AppFilePathUtils.copySafFile(
                    audioPath,
                    audioDocument?.uri,
                    context,
                )
                AppFilePathUtils.copySafFile(
                    context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/index.json",
                    indexDocument?.uri,
                    context,
                )
            }

            val impFileDeleteState =
                PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(
                        "user_dl_delete_import_file_switch",
                        true,
                    )

            if (impFileDeleteState) {
                FileUtils.deleteFile(videoPath)
                FileUtils.deleteFile(audioPath)
                task.savePath =
                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/${downloadTaskDataBean.qn}/video.m4s"
                // 分别储存两次下载结果
                task.fileType = 0
                saveFinishTask(task)
                task.savePath =
                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_$ssid/$epid/${downloadTaskDataBean.qn}/audio.m4s"
                task.fileType = 1
                saveFinishTask(task)
            } else {

                // 分别储存两次下载结果
                // 不删除则移动到指定目录
                moveFileToDlUriPath(videoPath)
                moveFileToDlUriPath(audioPath)

                saveFinishTask(task)
                task.savePath = audioPath
                task.fileType = 1
                saveFinishTask(task)
            }

            FileUtils.delete(
                context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid,
            )
        }
    }

    // 在 DownloadQueue 类中
    fun updateProgress(task: DownloadTaskInfo, progress: Double) {
        task.progress = progress
        updateTasks()
    }

    // 解压deflate数据的函数
    fun decompress(data: ByteArray): ByteArray {
        var output: ByteArray
        val decompresser = Inflater(true) // 这个true是关键
        decompresser.reset()
        decompresser.setInput(data)
        val o = ByteArrayOutputStream(data.size)
        try {
            val buf = ByteArray(1024)
            while (!decompresser.finished()) {
                val i: Int = decompresser.inflate(buf)
                o.write(buf, 0, i)
            }
            output = o.toByteArray()
        } catch (e: Exception) {
            output = data
            e.printStackTrace()
        } finally {
            try {
                o.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        decompresser.end()
        return output
    }

    // 更新图库
    private fun updatePhotoMedias(context: Context, vararg files: File) {
        files.forEach {
            val intent = Intent()
            intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
            intent.data = Uri.fromFile(it)
            context.sendBroadcast(intent)
        }
    }


    private fun moveFileToDlUriPath(oldPath: String) {
        val result = runCatching {
            launchIO {
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context)
                val saveUriPath = sharedPreferences.getString(
                    "user_download_save_uri_path",
                    null,
                )

                if (saveUriPath != null) {
                    var dlFileDocument = DocumentFile.fromTreeUri(
                        context,
                        Uri.parse(saveUriPath)
                    )!!

                    val docList = oldPath.replace(
                        "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download/",
                        ""
                    ).split("/")

                    docList.forEachIndexed { index, name ->
                        // 是不是最后尾部
                        if (index != docList.size - 1) {
                            dlFileDocument = if (!dlFileDocument.hasSubDirectory(name)) {
                                dlFileDocument.createDirectory(name)!!
                            } else {
                                dlFileDocument.findFile(name)!!
                            }
                        } else {
                            dlFileDocument =
                                dlFileDocument.createFile(
                                    "application/${name.split(".").last()}",
                                    name
                                )!!
                            val copyResult = AppFilePathUtils.copySafFile(
                                oldPath,
                                dlFileDocument.uri,
                                context
                            )

                            if (copyResult) {
                                FileUtils.deleteFile(oldPath)
                            } else {
                                launchUI {
                                    asToast(
                                        context,
                                        "移动失败，文件会被保留在原路径"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result.isFailure) {
            launchUI {
                asToast(
                    context,
                    "移动失败，文件会被保留在初始路径，请在删除后重新下载"
                )
            }
        }
    }

    // 更新任务列表
    private fun updateTasks() {
        _downloadTasks.value = (currentTasks + queue).map { it.deepCopy() }
    }

    // 更新已完成任务
    private fun updateFinishedTasks(tasks: List<DownloadFinishTaskInfo>) {
        _finishedTasks.value = tasks
    }

}

