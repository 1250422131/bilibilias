package com.imcys.network.download

import android.content.Context
import com.imcys.common.di.AppCoroutineScope
import com.imcys.model.video.Page
import com.imcys.network.repository.VideoRepository
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

const val STATE_DOWNLOAD_WAIT = 0

// 非正常结束
const val STATE_DOWNLOAD_ERROR = -1
private const val BILIBILI_DOWNLOAD_PATH = "/storage/emulated/0/Android/data/tv.danmaku.bili/download"
private const val INDEX_JSON = "index.json"
private const val ENTRY_JSON = "entry.json"
private const val VIDEO_M4S = "video.m4s"
private const val AUDIO_M4S = "audio.m4s"

@Singleton
class DownloadManage @Inject constructor(
    private val fetchManage: FetchManage,
    private val videoRepository: VideoRepository,
    @ApplicationContext private val context: Context,
    @AppCoroutineScope private val scope: CoroutineScope,
    private val json: Json
) {

    fun addTask(
        details: com.imcys.model.VideoDetails,
        dash: com.imcys.model.PlayerInfo,
        page: Page,
        downloadListHolders: DownloadListHolders
    ) {
        // when (downloadListHolders.requireDownloadFileType) {
        //     DownloadFileType.VideoAndAudio -> videoAndAudio(
        //         dash,
        //         details.bvid,
        //         page,
        //         downloadListHolders,
        //         details.aid,
        //         details.title
        //     )
        //
        //     DownloadFileType.OnlyAudio -> TODO()
        // }
    }

    private fun videoAndAudio(
        dash: com.imcys.model.PlayerInfo,
        bvid: String,
        page: Page,
        downloadListHolders: DownloadListHolders,
        aid: Long,
        title: String
    ) {
        when (downloadListHolders.toolType) {
            DownloadToolType.BUILTIN -> builtin(bvid, dash, page, downloadListHolders, aid, title)
            DownloadToolType.IDM -> startIDMDownload()
            DownloadToolType.ADM -> startADMDownload()
        }
    }

    /**
     * download/bv123/c_111/danmaku.xml
     */
    private fun downloadDanmaku(cid: Long, danmakuPath: String) {
        scope.launch {
            videoRepository.getDanmakuXml(cid).collect {
                when (it) {
                    is com.imcys.common.utils.Result.Error -> Timber.tag("下载弹幕异常").d(it.exception)
                    com.imcys.common.utils.Result.Loading -> {}
                    is com.imcys.common.utils.Result.Success -> {
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

    fun downloadDanmaku(cid: Long, aid: Long) {
        // scope.launchIO {
        //     videoRepository.getRealTimeDanmaku(cid = cid, aid = aid, useWbi = true).collect { res ->
        //         when (res) {
        //             is com.imcys.common.utils.Result.Error -> TODO()
        //             com.imcys.common.utils.Result.Loading -> {}
        //             is com.imcys.common.utils.Result.Success -> buildAss(res.data.elemsList)
        //         }
        //     }
        // }
    }

    // fun buildAss(elemsList: List<Dm.DanmakuElem>) {
        // Fix
        // Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding
        // Dialogue: 0,0:14:28.92,0:14:36.92,R2L,,20,20,2,,{\move(610,25,-50,25)}广告满分
        // Dialogue: 0,0:00:27.52,0:00:31.52,Fix,,20,20,2,,{\pos(280,25)}听成了建材之父
        // Dialogue: 0,$startTime,$endTime,Large,,0000,0000,0000,,{\\pos(960,1050)\\c&H000000&}${it.content}\n"
        // progress=19516,mode=1,fontsize=25,color=16777215,content=哟呵，没探店了？,pool=0,colorful=NoneType,
        // progress=33599,mode=1,fontsize=25,color=16777215,content=中门对狙,pool=0,colorful=NoneType,
        // progress=28052,mode=1,fontsize=25,color=16777215,content=摄像师——伯娘,pool=0,colorful=NoneType,
        // progress=45419,mode=1,fontsize=25,color=16777215,content=手推木啊，好家伙，你搞个驴拉的都比你手推的好呀,pool=0,colorful=NoneType,
        // progress=267710,mode=1,fontsize=25,color=16777215,content=400爷笑嘻了,pool=0,colorful=NoneType,
        // Format:  Layer ,Start      ,End        ,Style ,Name ,MarginL ,MarginR ,MarginV ,Effect, Text
        // Dialogue: 0     ,0:00:00.38 ,0:00:08.38 ,R2L   ,     ,20      ,20      ,2       ,,{\move(610,25,-50,25)}火钳刘明
        // elemsList.take(5).forEach {
        //     Timber.d(
        //         "progress=${it.progress}," +
        //                 "mode=${it.mode}," +
        //                 "fontsize=${it.fontsize}," +
        //                 "color=${it.color}," +
        //                 "content=${it.content}," +
        //                 "pool=${it.pool}," +
        //                 "colorful=${it.colorful},"
        //     )
        //     val time = formatSecond(it.progress)
        //     val len = "Dialogue: 0,00:00:00,00:00:00,R2L,,0,0,0,,{\\move(0,0,0,0)}".length * elemsList.size
        //     buildString(len) {
        //         append("Dialogue: ")
        //         append('0')
        //         appendBeforeComma(time.first)
        //         appendBeforeComma(time.second)
        //
        //         append(it.content)
        //     }

    // }


    fun StringBuilder.appendComma(): StringBuilder = append(',')
    fun StringBuilder.appendBeforeComma(str: String): StringBuilder = append(str).appendComma()

    fun formatSecond(milliseconds: Int): Pair<String, String> {
        val duration = milliseconds.milliseconds
        // val period = duration.toDateTimePeriod()

        // val hours = period.hours
        // val minutes = period.minutes
        // val seconds = period.seconds
        //
        // val startTime = String.format(null, "%02d:%02d:%02d", hours, minutes, seconds)
        // val endTime = String.format(null, "%02d:%02d:%02d", hours, minutes, (seconds + 8) % 60)
        return "startTime" to "endTime"
    }

    fun buildHeadersInfo(
        title: String,
        playResX: String,
        playResY: String,
    ): String =
        """
        [Script Info]
        ; Script generated by BILIBILIAS Danmaku Converter
        Title: $title
        ScriptType: v4.00+
        PlayResX:$playResX
        PlayResY:$playResY
        ScaledBorderAndShadow: no
        """.trimIndent()

    fun getFontStyleInfo(): String {
        return """
        [V4+ Styles]
        Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding
        Style: Small,Microsoft YaHei,36,&H00FFFFFF, &H00FFFFFF, &H00000000, &H00000000, 0, 0, 0, 0, 100, 100, 0.00, 0.00, 1, 1, 0, 2, 20, 20, 20, 0
        Style: Medium,Microsoft YaHei,52,&H00FFFFFF, &H00FFFFFF, &H00000000, &H00000000, 0, 0, 0, 0, 100, 100, 0.00, 0.00, 1, 1, 0, 2, 20, 20, 20, 0
        Style: Large,Microsoft YaHei,64,&H00FFFFFF, &H00FFFFFF, &H00000000, &H00000000, 0, 0, 0, 0, 100, 100, 0.00, 0.00, 1, 1, 0, 2, 20, 20, 20, 0
        Style: Larger,Microsoft YaHei,72,&H00FFFFFF, &H00FFFFFF, &H00000000, &H00000000, 0, 0, 0, 0, 100, 100, 0.00, 0.00, 1, 1, 0, 2, 20, 20, 20, 0
        Style: ExtraLarge,Microsoft YaHei,90,&H00FFFFFF, &H00FFFFFF, &H00000000, &H00000000, 0, 0, 0, 0, 100, 100, 0.00, 0.00, 1, 1, 0, 2, 20, 20, 20, 0
        """.trimIndent()
    }

    fun getFormat(): String {
        return """
        [Events]
        Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
        """.trimIndent()
    }

    fun generateASS() {
        """
        [Script Info]
        Title: {{title}}
        Original Script: 根据 {{ori}} 的弹幕信息，由 https://github.com/tiansh/us-danmaku 生成
        ScriptType: v4.00+
        Collisions: Normal
        PlayResX: {{playResX}}
        PlayResY: {{playResY}}
        Timer: 10.0000

        [V4+ Styles]
        Format: Name, Fontname, Fontsize, PrimaryColour, SecondaryColour, OutlineColour, BackColour, Bold, Italic, Underline, StrikeOut, ScaleX, ScaleY, Spacing, Angle, BorderStyle, Outline, Shadow, Alignment, MarginL, MarginR, MarginV, Encoding
        Style: Fix,{{font}},25,&H{{alpha}}FFFFFF,&H{{alpha}}FFFFFF,&H{{alpha}}000000,&H{{alpha}}000000,1,0,0,0,100,100,0,0,1,2,0,2,20,20,2,0
        Style: R2L,{{font}},25,&H{{alpha}}FFFFFF,&H{{alpha}}FFFFFF,&H{{alpha}}000000,&H{{alpha}}000000,1,0,0,0,100,100,0,0,1,2,0,2,20,20,2,0

        [Events]
        Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text
        """.trimIndent()
    }

    private fun startADMDownload() {}

    private fun startIDMDownload() {}
    private fun builtin(
        bvid: String,
        dash: com.imcys.model.PlayerInfo,
        page: Page,
        downloadListHolders: DownloadListHolders,
        aid: Long,
        title: String
    ) {
        val qn = downloadListHolders.videoQuality
        val d = dash.dash
        buildIndexJson(d)
        buildEntryJson(qn, bvid, page)
        val cid = page.cid

        val videoUrl: String =
            d.video.groupBy { it.id }[downloadListHolders.videoQuality]?.last()?.baseUrl
                ?: d.video.groupBy { it.id }.firstNotNullOf { (_, v) -> v.last().backupUrl.last() }
        val audioUrl = d.audio.maxBy { it.id }.baseUrl
        Timber.tag("downloadUrl").d("video=$videoUrl,audio=$audioUrl")
        val video = File(context.getExternalFilesDir("download/$bvid/c_$cid/$qn"), VIDEO_M4S)
        val audio = File(context.getExternalFilesDir("download/$bvid/c_$cid/$qn"), AUDIO_M4S)
        val requestVideo = createVideoRequest(videoUrl, video.absolutePath, cid, title, page.part, bvid, aid)
        val requestAudio = createAudioRequest(audioUrl, audio.path, cid, title, page.part, bvid, aid)
        fetchManage.add(listOf(requestVideo, requestAudio))
        downloadDanmaku(cid, "$bvid/c_$cid")
    }

    private fun buildEntryJson(qn: Int, bvid: String, page: Page) {
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
                            "page_data": ${json.encodeToString(page)}
                        }
        """.trimIndent()
    }

    private fun buildIndexJson(d: com.imcys.model.Dash) {
        val indexJson = """
                {
                "video":[${json.encodeToString(d.video.maxBy { it.codecid })}],
                "audio":[${json.encodeToString(d.audio.maxBy { it.id })}]
                }
        """.trimIndent()
    }

    /**
     * {BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}
     * download/bvid/cid/画质/video.m4s
     * download/bv123/c_111/120/video.m4s
     */
    private fun buildSavePath(bvid: String, cid: Long, qn: Int, name: String): String =
        "savePath/$bvid/c_$cid/$qn/$name"

    private fun createVideoRequest(url: String, file: String, cid: Long, title: String, pageTitle: String, bvid: String, aid: Long) =
        fetchManage.createRequest(url, file, cid, TAG_VIDEO, title, pageTitle, bvid, aid)

    private fun createAudioRequest(url: String, file: String, cid: Long, title: String, pageTitle: String, bvid: String, aid: Long) =
        fetchManage.createRequest(url, file, cid, TAG_AUDIO, title, pageTitle, bvid, aid)

    fun findAllTask(result: (List<Download>) -> Unit) {
        fetchManage.findAllTask(result)
    }

    fun deleteAll() {
        fetchManage.deleteAll()
    }

    fun deleteFiles(ids: List<Int>, onSuccess: (List<Download>) -> Unit, onError: (Error) -> Unit) {
        fetchManage.deleteFiles(ids, onSuccess, onError)
    }

    fun deleteFile(id: Int, onSuccess: (Download) -> Unit, onError: (Error) -> Unit) {
        fetchManage.deleteFile(id, onSuccess, onError)
    }

    fun deleteGroup(groupId: Int, onSuccess: (List<Download>) -> Unit, onError: (Error) -> Unit) {
        fetchManage.deleteGroup(groupId, onSuccess, onError)
    }

    fun addTask(bvid: String, cid: Long, quality: Int?) {

    }
}

