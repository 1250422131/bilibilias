package com.imcys.bilibilias.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.extend.toAsFFmpeg
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.base.utils.VideoNumConversion
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.home.ui.adapter.DownloadFinishTaskAd
import com.imcys.bilibilias.home.ui.adapter.DownloadTaskAdapter
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.microsoft.appcenter.analytics.Analytics
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import kotlinx.coroutines.*
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


const val FLV_FILE = 1
const val DASH_FILE = 0

const val STATE_DOWNLOAD_WAIT = 0
const val STATE_DOWNLOADING = 1
const val STATE_DOWNLOAD_END = 2
const val STATE_DOWNLOAD_PAUSE = 3

//非正常结束
const val STATE_DOWNLOAD_ERROR = -1


// 定义一个下载队列类

class DownloadQueue :
    CoroutineScope by MainScope() {

    private val groupTasksMap: MutableMap<Int, MutableList<Task>> = mutableMapOf()


    var downloadTaskAdapter: DownloadTaskAdapter? = null
    var downloadFinishTaskAd: DownloadFinishTaskAd? = null

    // 存储待下载的任务
    private val queue = mutableListOf<Task>()

    // 当前正在下载的任务
    private val currentTasks = mutableListOf<Task>()

    var allTask = mutableListOf<Task>()


    // 下载任务类
    data class Task(
        // 下载地址
        val url: String,
        // 下载文件保存路径
        var savePath: String,
        // 文件类型，0为视频，1为音频
        var fileType: Int,
        //下载任务的其他参撒
        val downloadTaskDataBean: DownloadTaskDataBean,
        // 标识这个任务是否是一组任务的一部分
        var isGroupTask: Boolean = true,
        // 定义下载完成回调
        val onComplete: (Boolean) -> Unit,
        var payloadsType: Int = 0,
        // 下载状态
        var state: Int = STATE_DOWNLOAD_WAIT,
        // 定义当前任务的下载进度
        var progress: Double = 0.0,
        // 定义当前文件大小
        var fileSize: Double = 0.0,
        // 定义当前已经下载的大小
        var fileDlSize: Double = 0.0,
        // 定义当前任务的下载请求
        var call: DownloadTask? = null,
    )


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
            Task(
                url,
                savePath,
                fileType,
                downloadTaskDataBean,
                isGroupTask = isGroupTask,
                onComplete
            )

        if (task.isGroupTask) {
            // 在map中找到这个任务所属的一组任务
            val groupTasks = groupTasksMap[task.downloadTaskDataBean.cid]
            if (groupTasks == null) {
                // 创建一个新的任务列表
                val newGroupTasks = mutableListOf<Task>()
                // 将这个任务加入到这个任务列表中
                newGroupTasks.add(task)
                // 将这个任务列表加入到map中
                groupTasksMap[task.downloadTaskDataBean.cid] = newGroupTasks
            } else groupTasks.add(task)
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
            //删除并且返回当前的task
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


            //更新任务状态
            mTask.state = STATE_DOWNLOADING
            // 添加任务到当前任务列表中
            currentTasks.add(mTask)

            mTask.call = okDownloadTask

            okDownloadTask.enqueue(object : DownloadListener {
                override fun taskStart(task: DownloadTask) {

                }

                override fun connectTrialStart(
                    task: DownloadTask,
                    requestHeaderFields: MutableMap<String, MutableList<String>>
                ) {
                }

                override fun connectTrialEnd(
                    task: DownloadTask,
                    responseCode: Int,
                    responseHeaderFields: MutableMap<String, MutableList<String>>
                ) {
                }

                override fun downloadFromBeginning(
                    task: DownloadTask,
                    info: BreakpointInfo,
                    cause: ResumeFailedCause
                ) {
                }

                override fun downloadFromBreakpoint(task: DownloadTask, info: BreakpointInfo) {
                }

                override fun connectStart(
                    task: DownloadTask,
                    blockIndex: Int,
                    requestHeaderFields: MutableMap<String, MutableList<String>>
                ) {
                }

                override fun connectEnd(
                    task: DownloadTask,
                    blockIndex: Int,
                    responseCode: Int,
                    responseHeaderFields: MutableMap<String, MutableList<String>>
                ) {
                }

                override fun fetchStart(task: DownloadTask, blockIndex: Int, contentLength: Long) {
                }

                override fun fetchProgress(
                    task: DownloadTask,
                    blockIndex: Int,
                    increaseBytes: Long
                ) {
                    val totalOffset = task.info?.totalOffset ?: 0L
                    val totalLength = task.info?.totalLength ?: 0L
                    val progress = ((totalOffset.toFloat() / totalLength) * 100)
                    updateProgress(mTask, progress.toDouble())


                    mTask.fileSize = (totalOffset / 1048576).toDouble()
                    mTask.fileDlSize = (totalLength / 1048576).toDouble()
                    // 下载进度更新时的回调，可以在这里处理下载百分比
                }

                override fun fetchEnd(task: DownloadTask, blockIndex: Int, contentLength: Long) {
                }

                override fun taskEnd(
                    task: DownloadTask,
                    cause: EndCause,
                    realCause: Exception?
                ) {

                    //异常
                    if (realCause != null) {
                        currentTasks.remove(mTask)
                        //更新任务状态
                        mTask.state = STATE_DOWNLOAD_ERROR
                        // 下载失败，调用任务的完成回调
                        mTask.onComplete(false)
                        //更新
                        updateAdapter()
                        // 执行下一个任务
                        executeTask()

                        return
                    }

                    currentTasks.remove(mTask)
                    //更新任务状态
                    mTask.state = STATE_DOWNLOAD_END
                    // 下载成功，调用任务的完成回调
                    mTask.onComplete(true)
                    //更新
                    updateAdapter()

                    if (mTask.isGroupTask) {
                        // 在map中找到这个任务所属的一组任务
                        val groupTasks = groupTasksMap[mTask.downloadTaskDataBean.cid]
                        // 判断这一组任务是否都已经下载完成
                        val isGroupTasksCompleted =
                            groupTasks?.all { it.state == STATE_DOWNLOAD_END } ?: false
                        if (isGroupTasksCompleted) {
                            videoDataSubmit(mTask)
                            videoMerge(mTask.downloadTaskDataBean.cid)
                        }

                    } else {
                        //FLV或者单独任务不需要合并操作，直接视为下载了。
                        saveFinishTask(mTask)
                        videoDataSubmit(mTask)
                        updatePhotoMedias(App.context, File(mTask.savePath))
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
    private fun saveFinishTask(task: Task) {
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.IO) {

            var videoTitle = ""
            var videoPageTitle = ""
            var avid = 0
            var cid = 0
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
                videoBvid = VideoNumConversion.toBvidOffline(avid)

            }

            val downloadFinishTaskInfo = DownloadFinishTaskInfo(
                videoTitle = videoTitle,
                videoPageTitle = videoPageTitle,
                videoAvid = avid,
                videoBvid = videoBvid,
                videoCid = cid,
                savePath = task.savePath,
                fileType = task.fileType,
            )

            val downloadFinishTaskDao =
                BaseApplication.appDatabase.downloadFinishTaskDao()

            //协程提交
            DownloadFinishTaskRepository(downloadFinishTaskDao).apply {

                insert(downloadFinishTaskInfo)

                downloadFinishTaskAd?.apply {
                    val finishTasks = allDownloadFinishTask
                    submitList(finishTasks)
                }

            }


        }

    }


    /**
     * 储存完成的下载任务集合
     * @param tasks Array<out Task>
     */
    private fun saveFinishTask(vararg tasks: Task) {
        tasks.forEach {
            saveFinishTask(it)
        }
    }


    /**
     * 数据解析
     * @param task Task
     */
    private fun videoDataSubmit(task: Task) {

        var aid: Int? = task.downloadTaskDataBean.bangumiSeasonBean?.cid

        launch {
            val cookie = BaseApplication.dataKv.decodeString("cookies", "")

            val videoBaseBean = KtHttpUtils.addHeader("cookie", cookie!!)
                .asyncGet<VideoBaseBean>("${BilibiliApi.getVideoDataPath}?bvid=${task.downloadTaskDataBean.bvid}")
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
                tName
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
        aid: Int,
        mid: Long,
        name: String?,
        copyright: Int,
        tName: String?,
    ) {

        //通知缓存成功
        Analytics.trackEvent("缓存成功")
        StatService.onEvent(App.context, "CacheSuccessful", "缓存成功")

        launch(Dispatchers.IO) {

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.context)
            val microsoftAppCenterType =
                sharedPreferences.getBoolean("microsoft_app_center_type", true)
            val baiduStatisticsType =
                sharedPreferences.getBoolean("baidu_statistics_type", true)

            val cookie = BaseApplication.dataKv.decodeString("cookies", "")

            val myUserData = KtHttpUtils.addHeader("cookie", cookie!!)
                .asyncGet<MyUserData>(BilibiliApi.getMyUserData)

            val url = if (!microsoftAppCenterType && !baiduStatisticsType) {
                "${BiliBiliAsApi.appAddAsVideoData}?Aid=$aid&Bvid=$bvid&Mid=$mid&Upname=$name&Tname=$tName&Copyright=$copyright"
            } else {
                "${BiliBiliAsApi.appAddAsVideoData}?Aid=$aid&Bvid=$bvid&Mid=$mid&Upname=$name&Tname=$tName&Copyright=$copyright&UserName=${myUserData.data.uname}&UserUID=${myUserData.data.mid}"
            }
            //提交数据
            HttpUtils.asyncGet(url).await()

        }


    }

    /**
     * 参数合并
     * @param cid Int
     */
    private fun videoMerge(cid: Int) {


        val mergeState =
            PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
                "user_dl_finish_automatic_merge_switch",
                true
            )
        val importState =
            PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
                "user_dl_finish_automatic_import_switch",
                false
            )


        val taskMutableList = groupTasksMap[cid]
        val videoTask =
            taskMutableList?.filter { it.fileType == 0 }
        val audioTask =
            taskMutableList?.filter { it.fileType == 1 }

        if (mergeState) {
            //耗时操作，这里直接开个新线程
            val videoPath =
                videoTask!![0].savePath
            val audioPath =
                audioTask!![0].savePath
            //这里的延迟是为了有足够时间让下载检查下载完整

            runFFmpegRxJavaVideoMerge(
                videoTask[0],
                videoPath,
                audioPath
            )

            //旧的合并方案： MediaExtractorUtils.combineTwoVideos(audioPath, 0,videoPath,mergeFile)

        } else if (importState) {
            videoTask!![0].downloadTaskDataBean.bangumiSeasonBean?.apply {
                //分别添加下载完成了
                saveFinishTask(videoTask[0], audioTask!![0])
                importVideo(cid)
            }

        } else {

            //这类代表虽然是dash下载，但是并不需要其他操作
            saveFinishTask(videoTask!![0], audioTask!![0])
            //这类通知相册更新下文件
            updatePhotoMedias(App.context, File(videoTask[0].savePath), File(audioTask[0].savePath))

        }

    }

    private fun runFFmpegRxJavaVideoMerge(
        task: Task,
        videoPath: String,
        audioPath: String,

        ) {

        val userDLMergeCmd =
            PreferenceManager.getDefaultSharedPreferences(App.context).getString(
                "user_dl_merge_cmd_editText",
                "ffmpeg -i {VIDEO_PATH} -i {AUDIO_PATH} -c copy {VIDEO_MERGE_PATH}"
            )

        File(videoPath + "_merge.mp4")

        val commands = userDLMergeCmd?.toAsFFmpeg(
            videoPath,
            audioPath,
            videoPath + "_merge.mp4"
        )
        //context.getFileDir().getPath()

        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commands)
            .subscribe(object : RxFFmpegSubscriber() {
                override fun onError(message: String?) {
                }

                override fun onFinish() {
                    asToast(App.context, "合并完成")
                    //删除合并文件
                    val deleteMergeSatae =
                        PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
                            "user_dl_finish_delete_merge_switch",
                            true
                        )
                    if (deleteMergeSatae) {
                        FileUtils.deleteFile(videoPath)
                        FileUtils.deleteFile(audioPath)
                        //仅仅储存视频地址
                        task.savePath = videoPath + "_merge.mp4"
                        task.fileType = 0
                        saveFinishTask(task)
                        //通知相册更新
                        updatePhotoMedias(App.context, File(videoPath + "_merge.mp4"))
                    } else {
                        //分别储存两次下载结果
                        val videoTask = task
                        val audioTask = task
                        videoTask.fileType = 0
                        audioTask.savePath = audioPath
                        audioTask.fileType = 1
                        saveFinishTask(videoTask, audioTask)
                        //通知相册更新
                        updatePhotoMedias(App.context, File(videoPath), File(audioPath))
                    }


                }

                override fun onProgress(progress: Int, progressTime: Long) {
                }

                override fun onCancel() {
                }

            })


    }


    /**
     * 缓存导回B站观看
     * @param cid Int
     */
    private fun importVideo(cid: Int) {
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
            //av过滤
            val pageRegex = Regex("""(?<=(第))([0-9]+)""")
            pageThisNum = if (pageRegex.containsMatchIn(share_copy)) pageRegex.find(
                share_copy
            )?.value!!.toInt() else TODO()

            shareUrl = share_url

            downloadTaskDataBean.dashBangumiPlayBean?.result?.support_formats?.forEach {
                if (it.quality.toString() == downloadTaskDataBean.qn) displayDesc = it.display_desc
            }

        } ?: downloadTaskDataBean.videoPageDataData?.apply {
            bvid = VideoNumConversion.toBvidOffline(this.cid)
            type = VIDEO_TYPE
            downloadTaskDataBean.dashVideoPlayBean?.data?.support_formats?.forEach {
                if (it.quality.toString() == downloadTaskDataBean.qn) displayDesc = it.display_desc
            }
        }

        //临时bangumiEntry -> 只对番剧使用
        var videoEntry = App.bangumiEntry
        var videoIndex = App.videoIndex
        val cookie = BaseApplication.dataKv.decodeString("cookies", "")
        HttpUtils.addHeader("cookie", cookie!!)
            .get("${BilibiliApi.getVideoDataPath}?bvid=$bvid", VideoBaseBean::class.java) {
                if (it.code == 0) {
                    videoEntry = videoEntry.replace("UP主UID", it.data.owner.mid.toString())
                    videoEntry = videoEntry.replace("UP名称", it.data.owner.name)
                    videoEntry = videoEntry.replace("UP头像", it.data.owner.face)
                    videoEntry = videoEntry.replace("AID编号", it.data.aid.toString())
                    videoEntry = videoEntry.replace("BVID编号", bvid)
                    videoEntry = videoEntry.replace("CID编号", it.data.cid.toString())
                    videoEntry = videoEntry.replace("下载标题", it.data.title + ".mp4")

                    videoEntry = videoEntry.replace("文件名称", it.data.title + ".mp4")
                    videoEntry = videoEntry.replace("标题", it.data.title)
                    videoEntry = videoEntry.replace("子集号", pageThisNum.toString())
                    videoEntry = videoEntry.replace("子集索引", (pageThisNum!! - 1).toString())
                    videoEntry = videoEntry.replace("排序号", (2000000 + pageThisNum!!).toString())
                    videoEntry = videoEntry.replace("下载子TITLE", downloadTaskDataBean.pageTitle)


                    videoEntry =
                        videoEntry.replace("LINK地址", it.data.redirect_url.replace("/", "\\/"))

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
                    videoEntry = if (downloadTaskDataBean.qn == "112") videoEntry.replace(
                        "码率",
                        "高码率"
                    ) else videoEntry.replace("码率", "")


                    videoEntry = videoEntry.replace("总时间", timeLength.toString())

                    videoEntry = videoEntry.replace("弹幕数量", it.data.stat.danmaku.toString())
                    videoEntry = videoEntry.replace("下载子标题", downloadTaskDataBean.pageTitle)

                    val dashAudioSize = AppFilePathUtils.getFileSize(audioTask!![0].savePath)
                    val dashVideoSize = AppFilePathUtils.getFileSize(videoTask[0].savePath)


                    videoEntry = videoEntry.replace("封面地址", it.data.pic.replace("/", "\\/"))
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
                                it
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
                                it
                            )
                        }
                    }


                }

            }


    }

    private fun fileImpVideo(
        task: Task,
        videoPath: String,
        audioPath: String,
        videoEntry: String,
        videoIndex: String,
        downloadTaskDataBean: DownloadTaskDataBean,
        videoBaseBean: VideoBaseBean,
    ) {
        var videoEntry = videoEntry
        val epidUrl = videoBaseBean.data.redirect_url
        //av过滤
        val epRegex = Regex("""(?<=(ep))([0-9]+)""")
        val epid = if (epRegex.containsMatchIn(epidUrl)) epRegex.find(
            epidUrl
        )?.value!!.toInt() else TODO()
        HttpUtils.get(
            "${BilibiliApi.bangumiVideoDataPath}?ep_id=$epid",
            BangumiSeasonBean::class.java
        ) {
            val ssid = it.result.season_id
            videoEntry = videoEntry.replace("SSID编号", (it.result.season_id).toString())
            videoEntry = videoEntry.replace("EPID编号", epid.toString())
            val cookie = BaseApplication.dataKv.decodeString("cookies", "")

            HttpUtils.addHeader("cookie", cookie!!)
                .get("${BilibiliApi.videoDanMuPath}?oid=${downloadTaskDataBean.cid}",
                    object : okhttp3.Callback {
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: Response) {
                            BaseApplication.handler.post {
                                val bufferedSink: BufferedSink?
                                val dest =
                                    File("/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/danmaku.json")
                                if (!dest.exists()) dest.createNewFile()
                                val sink = dest.sink() //打开目标文件路径的sink
                                val decompressBytes =
                                    decompress(response.body!!.bytes()) //调用解压函数进行解压，返回包含解压后数据的byte数组
                                bufferedSink = sink.buffer()
                                decompressBytes.let { it -> bufferedSink.write(it) } //将解压后数据写入文件（sink）中
                                bufferedSink.close()

                                FileUtils.fileWrite(
                                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/entry.json",
                                    videoEntry
                                )
                                FileUtils.fileWrite(
                                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/${downloadTaskDataBean.qn}/index.json",
                                    videoIndex
                                )

                                AppFilePathUtils.copyFile(
                                    videoPath,
                                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/${downloadTaskDataBean.qn}/video.m4s"
                                )
                                AppFilePathUtils.copyFile(
                                    audioPath,
                                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/${downloadTaskDataBean.qn}/audio.m4s"
                                )

                                val impFileDeleteState =
                                    PreferenceManager.getDefaultSharedPreferences(App.context)
                                        .getBoolean(
                                            "user_dl_delete_import_file_switch",
                                            true
                                        )

                                if (impFileDeleteState) {
                                    FileUtils.deleteFile(videoPath)
                                    FileUtils.deleteFile(audioPath)
                                    task.savePath =
                                        "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/${downloadTaskDataBean.qn}/video.m4s"
                                    //分别储存两次下载结果
                                    saveFinishTask(task)
                                    task.savePath =
                                        "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/${downloadTaskDataBean.qn}/audio.m4s"
                                    task.fileType = 1
                                    saveFinishTask(task)

                                } else {
                                    //分别储存两次下载结果
                                    saveFinishTask(task)
                                    task.savePath = audioPath
                                    task.fileType = 1
                                    saveFinishTask(task)
                                }

                            }
                        }

                    })

        }

    }

    private fun safImpVideo(
        task: Task,
        videoPath: String,
        audioPath: String,
        videoEntry: String,
        videoIndex: String,
        downloadTaskDataBean: DownloadTaskDataBean,
        videoBaseBean: VideoBaseBean,
    ) {

        launch(Dispatchers.Default) {

            var videoEntry = videoEntry
            val appDataUri = PreferenceManager.getDefaultSharedPreferences(App.context)
                .getString("AppDataUri", "")

            val saf = DocumentFile.fromTreeUri(App.context, Uri.parse(appDataUri))


            var biliBiliDocument = saf?.findFile("download") ?: run {
                saf?.createDirectory("download")
            }

            val epidUrl = videoBaseBean.data.redirect_url
            //av过滤
            val epRegex = Regex("""(?<=(ep))([0-9]+)""")
            val epid = if (epRegex.containsMatchIn(epidUrl)) epRegex.find(
                epidUrl
            )?.value!!.toInt() else TODO()

            val bangumiSeasonBean =
                KtHttpUtils.asyncGet<BangumiSeasonBean>("${BilibiliApi.bangumiVideoDataPath}?ep_id=$epid")

            val ssid = bangumiSeasonBean.result.season_id
            videoEntry =
                videoEntry.replace("SSID编号", (bangumiSeasonBean.result.season_id).toString())
            videoEntry = videoEntry.replace("EPID编号", epid.toString())


            val ssidDocument = biliBiliDocument?.findFile("s_${ssid}") ?: run {
                biliBiliDocument?.createDirectory("s_${ssid}")

            }

            val epidDocument =
                ssidDocument?.createDirectory(epid.toString())

            val qnDocument =
                epidDocument?.createDirectory(downloadTaskDataBean.qn)

            //创建文件
            val danmakuDocument = epidDocument?.createFile("application/xml", "danmaku")
            val entryDocument = epidDocument?.createFile("application/json", "entry")
            FileUtils.fileWrite(
                App.context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/entry.json",
                videoEntry
            )

            FileUtils.fileWrite(
                App.context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/index.json",
                videoIndex
            )
            val cookie = BaseApplication.dataKv.decodeString("cookies", "")

            val asyncResponse = HttpUtils.addHeader("cookie", cookie!!)
                .asyncGet("${BilibiliApi.videoDanMuPath}?oid=${downloadTaskDataBean.cid}")


            val response = asyncResponse.await()

            val bufferedSink: BufferedSink?
            val dest = File(
                App.context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/danmaku.json"
            )
            if (!dest.exists()) dest.createNewFile()
            val sink = dest.sink() //打开目标文件路径的sink
            val decompressBytes =
                decompress(response.body!!.bytes()) //调用解压函数进行解压，返回包含解压后数据的byte数组
            bufferedSink = sink.buffer()
            decompressBytes.let { it -> bufferedSink.write(it) } //将解压后数据写入文件（sink）中
            bufferedSink.close()


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppFilePathUtils.copySafFile(
                    App.context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/danmaku.json",
                    danmakuDocument?.uri,
                    App.context
                )
                AppFilePathUtils.copySafFile(
                    App.context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/entry.json",
                    entryDocument?.uri,
                    App.context
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
                    videoPath, videoDocument?.uri,
                    App.context
                )
                AppFilePathUtils.copySafFile(
                    audioPath,
                    audioDocument?.uri,
                    App.context
                )
                AppFilePathUtils.copySafFile(
                    App.context.getExternalFilesDir("temp")
                        .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/" + downloadTaskDataBean.qn + "/index.json",
                    indexDocument?.uri,
                    App.context
                )
            }


            val impFileDeleteState =
                PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
                    "user_dl_delete_import_file_switch",
                    true
                )

            if (impFileDeleteState) {
                FileUtils.deleteFile(videoPath)
                FileUtils.deleteFile(audioPath)
                task.savePath =
                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/${downloadTaskDataBean.qn}/video.m4s"
                //分别储存两次下载结果
                task.fileType = 0
                saveFinishTask(task)
                task.savePath =
                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download/s_${ssid}/${epid}/${downloadTaskDataBean.qn}/audio.m4s"
                task.fileType = 1
                saveFinishTask(task)

            } else {
                //分别储存两次下载结果
                saveFinishTask(task)
                task.savePath = audioPath
                task.fileType = 1
                saveFinishTask(task)
            }


            FileUtils.delete(
                App.context.getExternalFilesDir("temp")
                    .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid
            )


        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter() {
        // 通知 RecyclerView 适配器数据发生了改变

        downloadTaskAdapter?.apply {
            val newMutableList = mutableListOf<Task>().apply {

                //任务拷贝，防止传入RecyclerView后被动更改
                currentTasks.forEach {
                    add(it.copy())
                }

                queue.forEach {
                    add(it.copy())
                }
            }

            submitList(newMutableList.toList())
        }


    }

    // 在 DownloadQueue 类中
    fun updateProgress(task: Task, progress: Double) {
        // 更新当前任务的下载进度
        task.progress = progress
        updateAdapter()
    }


    //解压deflate数据的函数
    fun decompress(data: ByteArray): ByteArray {
        var output: ByteArray
        val decompresser = Inflater(true) //这个true是关键
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


    //更新图库
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
            .setFilenameFromResponse(false) //是否使用 response header or url path 作为文件名，此时会忽略指定的文件名，默认false
            .setPassIfAlreadyCompleted(true) //如果文件已经下载完成，再次下载时，是否忽略下载，默认为true(忽略)，设为false会从头下载
            .setConnectionCount(1) //需要用几个线程来下载文件，默认根据文件大小确定；如果文件已经 split block，则设置后无效
            .setPreAllocateLength(false) //在获取资源长度后，设置是否需要为文件预分配长度，默认false
            .setMinIntervalMillisCallbackProcess(1500) //通知调用者的频率，避免anr，默认3000
            .setWifiRequired(false) //是否只允许wifi下载，默认为false
            .setAutoCallbackToUIThread(true) //是否在主线程通知调用者，默认为true
            //.setHeaderMapFields(new HashMap<String, List<String>>())//设置请求头
            //.addHeader(String key, String value)//追加请求头
            //.setPriority(0) //设置优先级，默认值是0，值越大下载优先级越高
            .setReadBufferSize(4096) //设置读取缓存区大小，默认4096
            .setFlushBufferSize(16384) //设置写入缓存区大小，默认16384
            .setSyncBufferSize(65536) //写入到文件的缓冲区大小，默认65536
            .setSyncBufferIntervalMillis(2000) //写入文件的最小时间间隔，默认2000
        task.addHeader(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0"
        )
        task.addHeader("referer", "https://www.bilibili.com/")
        val cookie = BaseApplication.dataKv.decodeString("cookies", "")
        task.addHeader("cookie", cookie!!)



        return task.build()
    }
}


