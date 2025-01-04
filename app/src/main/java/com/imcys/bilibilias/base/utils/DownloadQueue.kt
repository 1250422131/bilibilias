package com.imcys.bilibilias.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.app.App.Companion.context
import com.imcys.bilibilias.base.model.task.DownloadTaskInfo
import com.imcys.bilibilias.base.model.task.deepCopy
import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
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
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.adapter.DownloadFinishTaskAd
import com.imcys.bilibilias.home.ui.adapter.DownloadTaskAdapter
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownloadProvider
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.utils.HandlerUtils
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Response
import okio.BufferedSink
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
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
class DownloadQueue @Inject constructor() {

    private val groupTasksMap: MutableMap<Long, MutableList<DownloadTaskInfo>> = mutableMapOf()

    var downloadTaskAdapter: DownloadTaskAdapter? = null
    var downloadFinishTaskAd: DownloadFinishTaskAd? = null

    // 存储待下载的任务
    private val queue = mutableListOf<DownloadTaskInfo>()

    // 当前正在下载的任务
    private val currentTasks = mutableListOf<DownloadTaskInfo>()

    @Inject
    lateinit var networkService: NetworkService

    // 添加下载任务到队列中
    fun addTask(
        url: String,
        savePath: String,
        fileType: Int,
        downloadTaskDataBean: DownloadTaskDataBean,
        isGroupTask: Boolean = true,
        onComplete: (Boolean) -> Unit,
    ) {
        // 创建一个下载任务
        val task =
            DownloadTaskInfo(
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
                // 将这个任务加入到这个任务列表中
                newGroupTasks.add(task)
                // 将这个任务列表加入到map中
                groupTasksMap[task.downloadTaskDataBean.cid] = newGroupTasks
            } else {
                groupTasks.add(task)
            }
        }
        // 添加下载任务到队列中
        queue.add(task)
        // 如果队列不为空，就执行队列中的所有任务
        if (queue.isNotEmpty()) {
            executeTask()
        }
    }

    // 执行下载任务
    private fun executeTask() {
        while (currentTasks.size < 2 && queue.isNotEmpty()) {
            // 删除并且返回当前的task
            val mTask = queue.removeAt(0)

            val fileRegex = ".+/(.+)\$"
            val rFile: Pattern = Pattern.compile(fileRegex)
            val m = rFile.matcher(mTask.savePath)
            var fileName = ""
            if (m.find()) {
                fileName = m.group(1) ?: ""
            }

            val filePath = mTask.savePath.replace("/$fileName", "")

            val okDownloadTask = createTasK(mTask.url, filePath, fileName)

            // 更新任务状态
            mTask.state = STATE_DOWNLOADING
            // 添加任务到当前任务列表中
            currentTasks.add(mTask)

            mTask.call = okDownloadTask

            okDownloadTask.enqueue(object : DownloadListener {
                override fun taskStart(task: DownloadTask) {
                }

                override fun connectTrialStart(
                    task: DownloadTask,
                    requestHeaderFields: MutableMap<String, MutableList<String>>,
                ) {
                }

                override fun connectTrialEnd(
                    task: DownloadTask,
                    responseCode: Int,
                    responseHeaderFields: MutableMap<String, MutableList<String>>,
                ) {
                }

                override fun downloadFromBeginning(
                    task: DownloadTask,
                    info: BreakpointInfo,
                    cause: ResumeFailedCause,
                ) {
                }

                override fun downloadFromBreakpoint(task: DownloadTask, info: BreakpointInfo) {
                }

                override fun connectStart(
                    task: DownloadTask,
                    blockIndex: Int,
                    requestHeaderFields: MutableMap<String, MutableList<String>>,
                ) {
                }

                override fun connectEnd(
                    task: DownloadTask,
                    blockIndex: Int,
                    responseCode: Int,
                    responseHeaderFields: MutableMap<String, MutableList<String>>,
                ) {
                }

                override fun fetchStart(task: DownloadTask, blockIndex: Int, contentLength: Long) {
                }

                override fun fetchProgress(
                    task: DownloadTask,
                    blockIndex: Int,
                    increaseBytes: Long,
                ) {
                    val totalOffset = task.info?.totalOffset ?: 0L
                    val totalLength = task.info?.totalLength ?: 0L
                    val progress = ((totalOffset.toFloat() / totalLength) * 100)
                    updateProgress(mTask, progress.toDouble())
                    // 下载进度更新时的回调，可以在这里处理下载百分比
                    mTask.fileSize = (totalOffset / 1048576).toDouble()
                    mTask.fileDlSize = (totalLength / 1048576).toDouble()
                }

                override fun fetchEnd(task: DownloadTask, blockIndex: Int, contentLength: Long) {
                }

                override fun taskEnd(
                    task: DownloadTask,
                    cause: EndCause,
                    realCause: Exception?,
                ) {
                    // 异常
                    if (realCause != null) {
                        if (mTask.isGroupTask) {
                            val groupTasks = groupTasksMap[mTask.downloadTaskDataBean.cid]
                            groupTasks?.forEach {
                                currentTasks.remove(it)
                                it.state = STATE_DOWNLOAD_ERROR
                                it.onComplete(false)
                                currentTasks.remove(it)
                            }
                        } else {
                            currentTasks.remove(mTask)
                            // 更新任务状态
                            mTask.state = STATE_DOWNLOAD_ERROR
                            // 下载失败，调用任务的完成回调
                            mTask.onComplete(false)
                        }
                        // 更新
                        updateAdapter()
                        // 执行下一个任务
                        executeTask()
                        Log.d(
                            "TAG",
                            context.getString(R.string.app_download_queue_error_text) + " ${realCause.message} "
                        )

                        return
                    }
                    // 下载完成
                    mTask.state = STATE_DOWNLOAD_END
                    mTask.progress = 100.0
                    updateAdapter()
                    if (mTask.isGroupTask) {
                        // 在map中找到这个任务所属的一组任务
                        val groupTasks = groupTasksMap[mTask.downloadTaskDataBean.cid]
                        // 判断这一组任务是否都已经下载完成
                        val isGroupTasksCompleted =
                            groupTasks?.all { it.state == STATE_DOWNLOAD_END } ?: false
                        if (isGroupTasksCompleted) {
                            videoDataSubmit(mTask)
                            videoMerge(mTask)
                        }
                    } else {
                        moveFileToDlUriPath(mTask.savePath)
                        currentTasks.remove(mTask)
                        // 下载成功，调用任务的完成回调
                        mTask.onComplete(true)

                        // FLV或者单独任务不需要合并操作，直接视为下载了。
                        saveFinishTask(mTask)
                        videoDataSubmit(mTask)
                        updatePhotoMedias(OkDownloadProvider.context, File(mTask.savePath))
                        updateAdapter()

                    }
                    // 执行下一个任务
                    executeTask()
                }
            })
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
                PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
            val value = sharedPreferences.getString(
                "user_download_save_uri_path",
                null,
            )

            var safPath = ""
            val path = if (value == null) {
                task.savePath
            } else {
                val documentFile =
                    DocumentFile.fromSingleUri(OkDownloadProvider.context, Uri.parse(value))!!
                safPath = (documentFile.uri.toString() + Uri.encode(task.savePath.replace(
                    "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download",
                    ""
                )))

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
                AppDatabase.getDatabase(OkDownloadProvider.context).downloadFinishTaskDao()

            // 协程提交
            DownloadFinishTaskRepository(downloadFinishTaskDao).apply {
                insert(downloadFinishTaskInfo)

                downloadFinishTaskAd?.apply {
                    val finishTasks = allDownloadFinishTask()
                    submitList(finishTasks)
                }
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
            OkDownloadProvider.context,
            "CacheSuccessful",
            context.getString(R.string.app_download_queue_cachesuccessful)
        )

        launchIO {
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
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
                networkService.postAsData(aid, bvid, mid, name, tName, copyright,myUserData.data.uname,myUserData.data.mid)
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
            PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context).getBoolean(
                "user_dl_finish_automatic_merge_switch",
                true,
            )
        val importState =
            PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context).getBoolean(
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
        updateAdapter()
    }

    private fun runFFmpegRxJavaVideoMerge(
        task: DownloadTaskInfo,
        videoPath: String,
        audioPath: String,

        ) {
        val userDLMergeCmd =
            PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context).getString(
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
                        OkDownloadProvider.context,
                        context.getString(R.string.app_download_queue_merge_error)
                    )
                    FileUtils.deleteFile(videoPath)
                    FileUtils.deleteFile(audioPath)
                    FileUtils.deleteFile(videoPath + "_merge.mp4")

                    runOnUiThread {
                        asToast(OkDownloadProvider.context, "合并错误,请重新下载，已经删除下载文件。")
                    }
                    executeTask()
                }

                override fun onFinish() {
                    asToast(
                        OkDownloadProvider.context,
                        context.getString(R.string.app_download_queue_merge_finish)
                    )
                    runOnUiThread {
                        asToast(OkDownloadProvider.context, "合并完成")
                    }
                    // 删除合并文件
                    val deleteMergeSatae =
                        PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
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
        var videoEntry = App.bangumiEntry
        var videoIndex = App.videoIndex
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
        launchUI {

            val bangumiSeasonBean = networkService.getBangumiSeasonBeanByEpid(epid)

            val ssid = bangumiSeasonBean.result.season_id
            videoEntry =
                videoEntry.replace("SSID编号", (bangumiSeasonBean.result.season_id).toString())
            videoEntry = videoEntry.replace("EPID编号", epid.toString())


            val danmakuByte = networkService.getDanmuBytes(downloadTaskDataBean.cid)

            BaseApplication.handler.post {
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
                    PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
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
                PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
                    .getString("AppDataUri", "")

            val saf = DocumentFile.fromTreeUri(OkDownloadProvider.context, Uri.parse(appDataUri))

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
                OkDownloadProvider.context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/entry.json",
                videoEntry,
            )

            FileUtils.fileWrite(
                OkDownloadProvider.context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/index.json",
                videoIndex,
            )
            val danmakuByte = networkService.getDanmuBytes(downloadTaskDataBean.cid)


            val bufferedSink: BufferedSink?
            val dest = File(
                OkDownloadProvider.context.getExternalFilesDir("temp")
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
                    OkDownloadProvider.context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/danmaku.json",
                    danmakuDocument?.uri,
                    OkDownloadProvider.context,
                )
                AppFilePathUtils.copySafFile(
                    OkDownloadProvider.context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/entry.json",
                    entryDocument?.uri,
                    OkDownloadProvider.context,
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
                    OkDownloadProvider.context,
                )
                AppFilePathUtils.copySafFile(
                    audioPath,
                    audioDocument?.uri,
                    OkDownloadProvider.context,
                )
                AppFilePathUtils.copySafFile(
                    OkDownloadProvider.context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/index.json",
                    indexDocument?.uri,
                    OkDownloadProvider.context,
                )
            }

            val impFileDeleteState =
                PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
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
                OkDownloadProvider.context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid,
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter() {
        // 通知 RecyclerView 适配器数据发生了改变
        downloadTaskAdapter?.apply {
            submitList((currentTasks + queue).map { it.deepCopy() })
        }
    }

    // 在 DownloadQueue 类中
    fun updateProgress(task: DownloadTaskInfo, progress: Double) {
        // 更新当前任务的下载进度
        task.progress = progress
        updateAdapter()
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

    /**
     * 创建下载任务实例
     *
     * @param url
     * @param parentPath
     * @param fileName
     * @return
     */
    private fun createTasK(url: String, parentPath: String, fileName: String): DownloadTask {
        val task = DownloadTask.Builder(url, parentPath, fileName)
            .setFilenameFromResponse(false) // 是否使用 response header or url path 作为文件名，此时会忽略指定的文件名，默认false
//            .setPassIfAlreadyCompleted(false) // 如果文件已经下载完成，再次下载时，是否忽略下载，默认为true(忽略)，设为false会从头下载
            .setConnectionCount(1) // 需要用几个线程来下载文件，默认根据文件大小确定；如果文件已经 split block，则设置后无效
            .setPreAllocateLength(false) // 在获取资源长度后，设置是否需要为文件预分配长度，默认false
            .setMinIntervalMillisCallbackProcess(1500) // 通知调用者的频率，避免anr，默认3000
            .setWifiRequired(false) // 是否只允许wifi下载，默认为false
            .setAutoCallbackToUIThread(true) // 是否在主线程通知调用者，默认为true
            // .setHeaderMapFields(new HashMap<String, List<String>>())//设置请求头
            // .addHeader(String key, String value)//追加请求头
            // .setPriority(0) //设置优先级，默认值是0，值越大下载优先级越高
            .setReadBufferSize(4096) // 设置读取缓存区大小，默认4096
            .setFlushBufferSize(16384) // 设置写入缓存区大小，默认16384
            .setSyncBufferSize(65536) // 写入到文件的缓冲区大小，默认65536
            .setSyncBufferIntervalMillis(2000) // 写入文件的最小时间间隔，默认2000
        task.addHeader(
            USER_AGENT,
            BROWSER_USER_AGENT,
        )
        task.addHeader(REFERER, "https://www.bilibili.com/")
        val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")
        task.addHeader(COOKIE, cookie!!)

        return task.build()
    }


    private fun moveFileToDlUriPath(oldPath: String) {
        val result = runCatching {
            launchIO {
                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(OkDownloadProvider.context)
                val saveUriPath = sharedPreferences.getString(
                    "user_download_save_uri_path",
                    null,
                )

                if (saveUriPath != null) {
                    var dlFileDocument = DocumentFile.fromTreeUri(
                        OkDownloadProvider.context,
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
                                OkDownloadProvider.context
                            )

                            if (copyResult) {
                                FileUtils.deleteFile(oldPath)
                            } else {
                                launchUI {
                                    asToast(
                                        OkDownloadProvider.context,
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
                    OkDownloadProvider.context,
                    "移动失败，文件会被保留在初始路径，请在删除后重新下载"
                )
            }
        }
    }

}
