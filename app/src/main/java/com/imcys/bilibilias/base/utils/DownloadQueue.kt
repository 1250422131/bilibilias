package com.imcys.bilibilias.base.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.task.DownloadTaskInfo
import com.imcys.bilibilias.base.model.user.DownloadTaskDataBean
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.extend.toAsFFmpeg
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.base.model.video.Dash
import com.imcys.bilibilias.common.base.model.video.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.utils.VideoUtils
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.common.data.download.entity.DownloadFileType
import com.imcys.bilibilias.common.data.entity.DownloadFinishTaskInfo
import com.imcys.bilibilias.ui.download.DownloadOptionsStateHolders
import com.imcys.bilibilias.ui.download.DownloadToolType
import com.imcys.bilibilias.ui.download.FetchManage
import com.imcys.bilibilias.ui.download.TAG_AUDIO
import com.imcys.bilibilias.ui.download.TAG_VIDEO
import com.microsoft.appcenter.analytics.Analytics
import dagger.hilt.android.qualifiers.ApplicationContext
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.zip.Inflater
import javax.inject.Inject
import javax.inject.Singleton

const val STATE_DOWNLOAD_WAIT = 0

// 非正常结束
const val STATE_DOWNLOAD_ERROR = -1
private const val BILIBILI_DOWNLOAD_PATH = "/storage/emulated/0/Android/data/tv.danmaku.bili/download"
private const val INDEX_JSON = "index.json"
private const val ENTRY_JSON = "entry.json"
private const val VIDEO_M4S = "video.m4s"
private const val AUDIO_M4S = "audio.m4s"

@Singleton
class DownloadQueue @Inject constructor(
    private val fetchManage: FetchManage,
    private val videoRepository: VideoRepository,
    @ApplicationContext private val context: Context
) : CoroutineScope by MainScope() {

    fun addTask(
        bvid: String,
        cid: Long,
        videoUrl: String,
        audioUrl: String,
        toolType: DownloadToolType,
        qn: Int,
        dash: DashVideoPlayBean,
        page: VideoDetails.Page,
        downloadOptionsStateHolders: DownloadOptionsStateHolders
    ) {
        when (downloadOptionsStateHolders.requireDownloadFileType) {
            DownloadFileType.VideoAndAudio -> videoAndAudio(
                videoUrl,
                audioUrl,
                dash,
                bvid,
                qn,
                page,
                downloadOptionsStateHolders
            )

            DownloadFileType.OnlyAudio -> TODO()
        }
    }

    private fun videoAndAudio(
        videoUrl: String,
        audioUrl: String,
        dash: DashVideoPlayBean,
        bvid: String,
        qn: Int,
        page: VideoDetails.Page,
        downloadOptionsStateHolders: DownloadOptionsStateHolders
    ) {
        when (downloadOptionsStateHolders.toolType) {
            DownloadToolType.BUILTIN -> builtin(bvid, dash, page, downloadOptionsStateHolders)
            DownloadToolType.IDM -> startIDMDownload()
            DownloadToolType.ADM -> startADMDownload()
        }
    }

    /**
     * download/bv123/c_111/danmaku.xml
     */
    private fun downloadDanmaku(cid: Long, danmakuPath: String) {
        launchIO {
            videoRepository.getDanmakuXml(cid).collect {
                when (it) {
                    is Result.Error -> Timber.tag("下载弹幕异常").d(it.exception)
                    Result.Loading -> {}
                    is Result.Success -> {
                        val file = File(
                            (context.getExternalFilesDir("download/$danmakuPath")),
                            "danmaku.xml"
                        )
                        FileSystem.SYSTEM.sink(file.toOkioPath()).use { fileSink ->
                            fileSink.buffer().use { bufferedSink ->
                                bufferedSink.write(it.data)
                                // android.os.FileUtils.copy()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startADMDownload() {}

    private fun startIDMDownload() {}
    private fun builtin(
        bvid: String,
        dash: DashVideoPlayBean,
        page: VideoDetails.Page,
        downloadOptionsStateHolders: DownloadOptionsStateHolders
    ) {
        val qn = downloadOptionsStateHolders.videoQuality
        val d = dash.dash
        buildIndexJson(d)
        buildEntryJson(qn, bvid, page)
        val cid = page.cid

        val videoUrl: String =
            d.video.groupBy { it.id }[downloadOptionsStateHolders.videoQuality]?.last()?.baseUrl
                ?: d.video.groupBy { it.id }.firstNotNullOf { (_, v) -> v.last().backupUrl.last() }

        val audioUrl = d.audio.maxBy { it.id }.baseUrl
        val video = File(context.getExternalFilesDir("download/$bvid/c_$cid/$qn"), VIDEO_M4S)
        val audio = File(context.getExternalFilesDir("download/$bvid/c_$cid/$qn"), AUDIO_M4S)
        val requestVideo = createVideoRequest(videoUrl, video.absolutePath, cid)
        val requestAudio = createAudioRequest(audioUrl, audio.path, cid)
        fetchManage.add(listOf(requestVideo, requestAudio))
        downloadDanmaku(cid, "$bvid/c_$cid")
    }

    private fun buildEntryJson(qn: Int, bvid: String, page: VideoDetails.Page) {
        val entryJson = """
                      {
                            "media_type": 2,
                            "has_dash_audio": true,
                            "is_completed": true,
                            "total_bytes": 下载大小,
                            "downloaded_bytes": 下载大小,
                            "title": "标题",
                            "type_tag": "$qn",
                            "cover": "封面地址",
                            "video_quality": $qn,
                            "prefered_video_quality": $qn,
                            "guessed_total_bytes": 0,
                            "total_time_milli": 总时间,
                            "danmaku_count": 弹幕数量,
                            "time_update_stamp": 1627134435654,
                            "time_create_stamp": 1627134431287,
                            "can_play_in_advance": true,
                            "interrupt_transform_temp_file": false,
                            "quality_pithy_description": "清晰度",
                            "quality_superscript": "码率",
                            "cache_version_code": 6340400,
                            "preferred_audio_quality": 0,
                            "audio_quality": 0,
                            
                            "avid": AID编号,
                            "spid": 0,
                            "seasion_id": 0,
                            "bvid": "$bvid",
                            "owner_id": UP主UID,
                            "owner_name": "UP名称",
                            "owner_avatar": "UP头像",
                            "page_data": ${Json.encodeToString(page)}
                        }
            """.trimIndent()
    }

    private fun buildIndexJson(d: Dash) {
        val indexJson = """
                {
                "video":[${Json.encodeToString(d.video.maxBy { it.codecid })}],
                "audio":[${Json.encodeToString(d.audio.maxBy { it.id })}]
                }
            """.trimIndent()
    }

    /**
     * {BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}
     * download/bvid/cid/画质/video.m4s
     * download/bv123/c_111/120/video.m4s
     */
    private fun buildSavePath(bvid: String, cid: Long, qn: Int, name: String): String =
        "$savePath/$bvid/c_$cid/$qn/$name"

    private fun createVideoRequest(url: String, file: String, cid: Long) =
        fetchManage.createRequest(url, file, cid.toInt(), TAG_VIDEO)

    private fun createAudioRequest(url: String, file: String, cid: Long) =
        fetchManage.createRequest(url, file, cid.toInt(), TAG_AUDIO)

    /**
     * 储存完成的下载任务
     * @param task Task
     */
    private fun saveFinishTask(task: DownloadTaskInfo) {
        CoroutineScope(Dispatchers.Default).launchIO {
            var videoTitle = ""
            var videoPageTitle = ""
            var avid = 0L
            var cid = 0L
            var videoBvid = ""
            task.downloadTaskDataBean.bangumiSeasonBean?.apply {
                videoTitle = shareCopy
                videoPageTitle = longTitle
                avid = aid
                cid = this.cid
                videoBvid = bvid
            }

            task.downloadTaskDataBean.videoPageDataData?.apply {
                videoTitle = part
                videoPageTitle = part
                avid = this.cid
                cid = this.cid
                videoBvid = VideoUtils.toBvidOffline(avid)
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

            // val downloadFinishTaskDao = AppDatabase.getDatabase(App.context).downloadFinishTaskDao()

            // 协程提交
            // DownloadFinishTaskRepository(downloadFinishTaskDao).apply {
            //     insert(downloadFinishTaskInfo)
            //
            //     downloadFinishTaskAd?.apply {
            //         val finishTasks = allDownloadFinishTask()
            //         submitList(finishTasks)
            //     }
            // }
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

        launch {
            val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")

            val videoDetails = KtHttpUtils.addHeader(COOKIE, cookie!!)
                .asyncGet<VideoDetails>("${BilibiliApi.getVideoDataPath}?bvid=${task.downloadTaskDataBean.bvid}")
            val mid = videoDetails.owner.mid
            val name = videoDetails.owner.name
            val copyright = videoDetails.copyright
            val tName = videoDetails.tname

            if (aid == null) {
                aid = videoDetails.aid
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
        Analytics.trackEvent("缓存成功")
        StatService.onEvent(App.context, "CacheSuccessful", "缓存成功")

        launchIO {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.context)
            val microsoftAppCenterType =
                sharedPreferences.getBoolean("microsoft_app_center_type", true)
            val baiduStatisticsType =
                sharedPreferences.getBoolean("baidu_statistics_type", true)

            val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")

            val myUserData = KtHttpUtils.addHeader(COOKIE, cookie!!)
                .asyncGet<MyUserData>(BilibiliApi.getMyUserData)

            val url = if (!microsoftAppCenterType && !baiduStatisticsType) {
                "${BiliBiliAsApi.appAddAsVideoData}?Aid=$aid&Bvid=$bvid&Mid=$mid&Upname=$name&Tname=$tName&Copyright=$copyright"
            } else {
                "${BiliBiliAsApi.appAddAsVideoData}?Aid=$aid&Bvid=$bvid&Mid=$mid&Upname=$name&Tname=$tName&Copyright=$copyright&UserName=${myUserData.uname}&UserUID=${myUserData.mid}"
            }
            // 提交数据
        }
    }

    /**
     * 参数合并
     * @param cid Int
     */
    private fun videoMerge(cid: Long) {
        val mergeState =
            PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
                "user_dl_finish_automatic_merge_switch",
                true,
            )
        val importState =
            PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
                "user_dl_finish_automatic_import_switch",
                false,
            )

        if (mergeState) {
            // 耗时操作，这里直接开个新线程
        } else if (importState) {
            importVideo(cid)
        } else {
        }
    }

    private fun runFFmpegRxJavaVideoMerge(
        task: DownloadTaskInfo,
        videoPath: String,
        audioPath: String,

    ) {
        val userDLMergeCmd =
            PreferenceManager.getDefaultSharedPreferences(App.context).getString(
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
                }

                override fun onFinish() {
                    asToast(App.context, "合并完成")
                    // 删除合并文件
                    val deleteMergeSatae =
                        PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
                            "user_dl_finish_delete_merge_switch",
                            true,
                        )
                    if (deleteMergeSatae) {
                        FileUtils.deleteFile(videoPath)
                        FileUtils.deleteFile(audioPath)
                        // 仅仅储存视频地址
                        task.savePath = videoPath + "_merge.mp4"
                        task.fileType = 0
                        saveFinishTask(task)
                        // 通知相册更新
                        updatePhotoMedias(File(videoPath + "_merge.mp4"))
                    } else {
                        // 分别储存两次下载结果
                        task.fileType = 0
                        task.savePath = audioPath
                        task.fileType = 1
                        saveFinishTask(task, task)
                        // 通知相册更新
                        updatePhotoMedias(File(videoPath), File(audioPath))
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
    private fun importVideo(cid: Long) {
        val VIDEO_TYPE = 1
        val BANGUMI_TYPE = 2

        var bvid = ""
        var type = VIDEO_TYPE

        var displayDesc: String? = "1080P"

        var pageThisNum: Int? = 0

        var shareUrl: String? = ""

        // downloadTaskDataBean.bangumiSeasonBean?.apply {
        //     bvid =""
        //     type = BANGUMI_TYPE
        //     // av过滤
        //     val pageRegex = Regex("""(?<=(第))([0-9]+)""")
        //     pageThisNum = if (pageRegex.containsMatchIn(shareCopy)) {
        //         pageRegex.find(
        //             shareCopy,
        //         )?.value!!.toInt()
        //     } else {
        //
        //     }

        //     shareUrl = shareUrl
        //
        //     downloadTaskDataBean.dashBangumiPlayBean?.result?.support_formats?.forEach {
        //         if (it.quality.toString() == downloadTaskDataBean.qn) displayDesc = it.display_desc
        //     }
        // } ?: downloadTaskDataBean.videoPageDataData?.apply {
        //     bvid = VideoUtils.toBvidOffline(this.cid)
        //     type = VIDEO_TYPE
        //     downloadTaskDataBean.dashVideoPlayBean?.supportFormats?.forEach {
        //         if (it.quality.toString() == downloadTaskDataBean.qn) displayDesc = it.displayDesc
        //     }
        // }

        // 临时bangumiEntry -> 只对番剧使用
        // var bangumiEntry = App.bangumiEntry
        // var videoIndex = App.videoIndex
        // val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")
        // HttpUtils.addHeader(COOKIE, cookie!!)
        //     .get("${BilibiliApi.getVideoDataPath}?bvid=$bvid", VideoDetails::class.java) {
        //         if (it.cid != 0L) {
        //             bangumiEntry = bangumiEntry.replace("UP主UID", it.owner.mid.toString())
        //                 .replace("UP名称", it.owner.name)
        //                 .replace("UP头像", it.owner.face)
        //                 .replace("AID编号", it.aid.toString())
        //                 .replace("BVID编号", bvid)
        //                 .replace("CID编号", it.cid.toString())
        //                 .replace("下载标题", it.title + ".mp4")
        //                 .replace("文件名称", it.title + ".mp4")
        //                 .replace("标题", it.title)
        //                 .replace("子集号", pageThisNum.toString())
        //                 .replace("子集索引", (pageThisNum!! - 1).toString())
        //                 .replace("排序号", (2000000 + pageThisNum!!).toString())
        //                 .replace("下载子TITLE", downloadTaskDataBean.pageTitle)
        //                 .replace("LINK地址", it.redirectUrl!!.replace("/", "\\/"))
        //                 .replace("高度", downloadTaskDataBean.bangumiSeasonBean!!.dimension.height.toString())
        //                 .replace("宽度", downloadTaskDataBean.bangumiSeasonBean.dimension.width.toString())
        //                 .replace("QN编码", downloadTaskDataBean.qn)
        //                 .replace("总时间", downloadTaskDataBean.dashBangumiPlayBean!!.result?.timelength.toString())
        //                 .replace("弹幕数量", it.stat.danmaku.toString())
        //                 .replace("下载子标题", downloadTaskDataBean.pageTitle)
        //                 .replace("封面地址", it.pic.replace("/", "\\/"))
        //                 .replace("下载大小", AppFilePathUtils.getFileSize(videoTask[0].savePath).toString())
        //                 .replace("清晰度", displayDesc!!)
        //                 .replace("码率", if (downloadTaskDataBean.qn == "112") "高码率" else "")
        //
        //             videoIndex = videoIndex.replace("视频大小", AppFilePathUtils.getFileSize(videoTask[0].savePath).toString())
        //                 .replace("高度", downloadTaskDataBean.videoPageDataData?.dimension?.height.toString())
        //                 .replace("宽度", downloadTaskDataBean.videoPageDataData?.dimension?.width.toString())
        //                 .replace("QN编码", downloadTaskDataBean.qn)
        //                 .replace("音频大小", AppFilePathUtils.getFileSize(audioTask!![0].savePath).toString())

        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //     if (type == BANGUMI_TYPE) {
        //         safImpVideo(
        //             videoTask[0],
        //             videoTask[0].savePath,
        //             audioTask[0].savePath,
        //             bangumiEntry,
        //             videoIndex,
        //             downloadTaskDataBean,
        //             it,
        //         )
        //     }
        // } else {
        //     if (type == BANGUMI_TYPE) {
        //         fileImpVideo(
        //             videoTask[0],
        //             videoTask[0].savePath,
        //             audioTask[0].savePath,
        //             bangumiEntry,
        //             videoIndex,
        //             downloadTaskDataBean,
        //             it,
        //         )
        //     }
        // }
    }

    private fun fileImpVideo(
        task: DownloadTaskInfo,
        videoPath: String,
        audioPath: String,
        videoEntry: String,
        videoIndex: String,
        downloadTaskDataBean: DownloadTaskDataBean,
        videoDetails: VideoDetails,
    ) {
        var videoEntry = videoEntry
        val epidUrl = videoDetails.redirectUrl
        // av过滤
        val epRegex = Regex("""(?<=(ep))([0-9]+)""")
        val epid = if (epRegex.containsMatchIn(epidUrl!!)) {
            epRegex.find(
                epidUrl,
            )?.value!!.toInt()
        } else {
            TODO()
        }
        // HttpUtils.get(
        //     "${BilibiliApi.bangumiVideoDataPath}?ep_id=$epid",
        //     Bangumi::class.java,
        /*) */
        /*{
            val ssid = it.result.seasonId
            videoEntry = videoEntry.replace("SSID编号", (it.result.seasonId).toString())
                .replace("EPID编号", epid.toString())
            val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")

            HttpUtils.addHeader(COOKIE, cookie!!)
                .get(
                    "${BilibiliApi.videoDanMuPath}?oid=${downloadTaskDataBean.cid}",
                    object : okhttp3.Callback {
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: Response) {
                            BaseApplication.handler.post {
                                val bufferedSink: BufferedSink?
                                val dest =
                                    File(
                                        "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/danmaku.json"
                                    )
                                if (!dest.exists()) dest.createNewFile()
                                val sink = dest.sink() // 打开目标文件路径的sink
                                val decompressBytes =
                                    decompress(response.body!!.bytes()) // 调用解压函数进行解压，返回包含解压后数据的byte数组
                                bufferedSink = sink.buffer()
                                decompressBytes.let { bufferedSink.write(it) } // 将解压后数据写入文件（sink）中
                                bufferedSink.close()

                                FileUtils.fileWrite(
                                    "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/$ENTRY_JSON",
                                    videoEntry,
                                )
                                FileUtils.fileWrite(
                                    "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/${downloadTaskDataBean.qn}/$INDEX_JSON",
                                    videoIndex,
                                )

                                AppFilePathUtils.copyFile(
                                    videoPath,
                                    "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/${downloadTaskDataBean.qn}/$VIDEO_M4S",
                                )
                                AppFilePathUtils.copyFile(
                                    audioPath,
                                    "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/${downloadTaskDataBean.qn}/$AUDIO_M4S",
                                )

                                val impFileDeleteState =
                                    PreferenceManager.getDefaultSharedPreferences(App.context)
                                        .getBoolean(
                                            "user_dl_delete_import_file_switch",
                                            true,
                                        )

                                if (impFileDeleteState) {
                                    FileUtils.deleteFile(videoPath)
                                    FileUtils.deleteFile(audioPath)
                                    task.savePath =
                                        "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/${downloadTaskDataBean.qn}/$VIDEO_M4S"
                                    // 分别储存两次下载结果
                                    // saveFinishTask(task)
                                    task.savePath =
                                        "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/${downloadTaskDataBean.qn}/$AUDIO_M4S"
                                    task.fileType = 1
                                    // saveFinishTask(task)
                                } else {
                                    // 分别储存两次下载结果
                                    // saveFinishTask(task)
                                    task.savePath = audioPath
                                    task.fileType = 1
                                    // saveFinishTask(task)
                                }
                            }
                        }
                    },
                )
        }*/
    }

    private fun safImpVideo(
        task: DownloadTaskInfo,
        videoPath: String,
        audioPath: String,
        videoEntry: String,
        videoIndex: String,
        downloadTaskDataBean: DownloadTaskDataBean,
        videoDetails: VideoDetails,
    ) {
        // launch(Dispatchers.Default) {
        //     var videoEntry = videoEntry
        //     val appDataUri = PreferenceManager.getDefaultSharedPreferences(App.context)
        //         .getString("AppDataUri", "")
        //
        //     val saf = DocumentFile.fromTreeUri(App.context, Uri.parse(appDataUri))
        //
        //     var biliBiliDocument = saf?.findFile("download") ?: run {
        //         saf?.createDirectory("download")
        //     }
        //
        //     val epidUrl = "videoBaseBean.redirect_url"
        //     // av过滤
        //     val epRegex = Regex("""(?<=(ep))([0-9]+)""")
        //     val epid = if (epRegex.containsMatchIn(epidUrl)) {
        //         epRegex.find(
        //             epidUrl,
        //         )?.value!!.toInt()
        //     } else {
        //         TODO()
        //     }
        //
        //     val bangumiSeasonBean =
        //         KtHttpUtils.asyncGet<Bangumi>("${BilibiliApi.bangumiVideoDataPath}?ep_id=$epid")
        //
        //     val ssid = bangumiSeasonBean.result.seasonId
        //     videoEntry =
        //         videoEntry.replace("SSID编号", (bangumiSeasonBean.result.seasonId).toString())
        //     videoEntry = videoEntry.replace("EPID编号", epid.toString())
        //
        //     val ssidDocument = biliBiliDocument?.findFile("s_$ssid") ?: run {
        //         biliBiliDocument?.createDirectory("s_$ssid")
        //     }
        //
        //     val epidDocument =
        //         ssidDocument?.createDirectory(epid.toString())
        //
        //     val qnDocument =
        //         epidDocument?.createDirectory(downloadTaskDataBean.qn)
        //
        //     // 创建文件
        //     val danmakuDocument = epidDocument?.createFile("application/xml", "danmaku")
        //     val entryDocument = epidDocument?.createFile("application/json", "entry")
        //     FileUtils.fileWrite(
        //         App.context.getExternalFilesDir("temp")
        //             .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/$ENTRY_JSON",
        //         videoEntry,
        //     )
        //
        //     val tempPath = App.context.getExternalFilesDir("temp")
        //         .toString() + "/导入模板/" +
        //         downloadTaskDataBean.bangumiSeasonBean?.aid +
        //         "/c_" + downloadTaskDataBean.cid + "/"
        //     FileUtils.fileWrite(
        //         tempPath + downloadTaskDataBean.qn + "/$INDEX_JSON",
        //         videoIndex,
        //     )
        //     val cookie = BaseApplication.dataKv.decodeString(COOKIES, "")
        //
        //     // val asyncResponse = TODO() HttpUtils.addHeader(COOKIE, cookie!!)
        //     //     .asyncGet("${BilibiliApi.videoDanMuPath}?oid=${downloadTaskDataBean.cid}")
        //
        //     val response = asyncResponse.await()
        //
        //     val bufferedSink: BufferedSink?
        //     val dest = File(
        //         tempPath + downloadTaskDataBean.qn + "/danmaku.json",
        //     )
        //     if (!dest.exists()) dest.createNewFile()
        //     val sink = dest.sink() // 打开目标文件路径的sink
        //     val decompressBytes =
        //         decompress(response.body!!.bytes()) // 调用解压函数进行解压，返回包含解压后数据的byte数组
        //     bufferedSink = sink.buffer()
        //     decompressBytes.let { bufferedSink.write(it) } // 将解压后数据写入文件（sink）中
        //     bufferedSink.close()
        //
        //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //         AppFilePathUtils.copySafFile(
        //             tempPath + downloadTaskDataBean.qn + "/danmaku.json",
        //             danmakuDocument?.uri,
        //             App.context,
        //         )
        //         AppFilePathUtils.copySafFile(
        //             App.context.getExternalFilesDir("temp")
        //                 .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid + "/c_" + downloadTaskDataBean.cid + "/$ENTRY_JSON",
        //             entryDocument?.uri,
        //             App.context,
        //         )
        //     }
        //
        //     val indexDocument =
        //         qnDocument?.createFile("application/json", "$INDEX_JSON")
        //     val videoDocument =
        //         qnDocument?.createFile("application/m4s", "$VIDEO_M4S")
        //     val audioDocument =
        //         qnDocument?.createFile("application/m4s", "$AUDIO_M4S")
        //
        //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //         AppFilePathUtils.copySafFile(
        //             videoPath,
        //             videoDocument?.uri,
        //             App.context,
        //         )
        //         AppFilePathUtils.copySafFile(
        //             audioPath,
        //             audioDocument?.uri,
        //             App.context,
        //         )
        //         AppFilePathUtils.copySafFile(
        //             tempPath + downloadTaskDataBean.qn + "/$INDEX_JSON",
        //             indexDocument?.uri,
        //             App.context,
        //         )
        //     }
        //
        //     val impFileDeleteState =
        //         PreferenceManager.getDefaultSharedPreferences(App.context).getBoolean(
        //             "user_dl_delete_import_file_switch",
        //             true,
        //         )
        //
        //     if (impFileDeleteState) {
        //         FileUtils.deleteFile(videoPath)
        //         FileUtils.deleteFile(audioPath)
        //         task.savePath =
        //             "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/${downloadTaskDataBean.qn}/$VIDEO_M4S"
        //         // 分别储存两次下载结果
        //         task.fileType = 0
        //         saveFinishTask(task)
        //         task.savePath =
        //             "$BILIBILI_DOWNLOAD_PATH/s_$ssid/$epid/${downloadTaskDataBean.qn}/$AUDIO_M4S"
        //         task.fileType = 1
        //         saveFinishTask(task)
        //     } else {
        //         // 分别储存两次下载结果
        //         saveFinishTask(task)
        //         task.savePath = audioPath
        //         task.fileType = 1
        //         saveFinishTask(task)
        //     }
        //
        //     FileUtils.delete(
        //         App.context.getExternalFilesDir("temp")
        //             .toString() + "/导入模板/" + downloadTaskDataBean.bangumiSeasonBean?.aid,
        //     )
        // }
    }

    // 解压deflate数据的函数
    fun decompress(data: ByteArray): ByteArray {
        var output: ByteArray
        val decompress = Inflater(true)
        decompress.reset()
        decompress.setInput(data)
        val o = ByteArrayOutputStream(data.size)
        try {
            val buf = ByteArray(4092)
            while (!decompress.finished()) {
                val i = decompress.inflate(buf)
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
        decompress.end()
        return output
    }
}

val e3 = """
    {
              "media_type": 2,
              "has_dash_audio": true,
              "is_completed": true,
              "total_bytes": 下载大小,
              "downloaded_bytes": 下载大小,
              "title": "标题",
              "type_tag": "QN编码",
              "cover": "封面地址",
              "video_quality": QN编码,
              "prefered_video_quality": QN编码,
              "guessed_total_bytes": 0,
              "total_time_milli": 总时间,
              "danmaku_count": 弹幕数量,
              "time_update_stamp": 1672473810242,
              "time_create_stamp": 1672473771023,
              "can_play_in_advance": true,
              "interrupt_transform_temp_file": false,
              "quality_pithy_description": "清晰度",
              "quality_superscript": "码率",
              "cache_version_code": 7010500,
              "preferred_audio_quality": 0,
              "audio_quality": 0,
              "source": { "av_id": AID编号, "cid": CID编号, "website": "bangumi" },
              "ep": {
                "av_id": AID编号,
                "page": 子集索引,
                "danmaku": CID编号,
                "cover": "封面地址",
                "episode_id": EPID编号,
                "index": "子集号",
                "index_title": "下载子TITLE",
                "from": "bangumi",
                "season_type": 4,
                "width": 宽度,
                "height": 高度,
                "rotate": 0,
                "link": "LINK地址",
                "bvid": "BVID编号",
                "sort_index": 排序号
              },
              "season_id": "SSID编号"
            }

""".trimIndent()
