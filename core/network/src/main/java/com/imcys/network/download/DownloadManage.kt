package com.imcys.network.download

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.hjq.toast.Toaster
import com.imcys.common.di.AppCoroutineScope
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.AppFilePathUtils
import com.imcys.model.PlayerInfo
import com.imcys.model.VideoDetails
import com.imcys.model.download.Entry
import com.imcys.model.video.PageData
import com.imcys.network.constant.BILIBILI_WEB_URL
import com.imcys.network.constant.BROWSER_USER_AGENT
import com.imcys.network.constant.REFERER
import com.imcys.network.constant.USER_AGENT
import com.imcys.network.repository.danmaku.IDanmakuDataSources
import com.imcys.network.repository.video.IVideoDataSources
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

const val DANMAKU_XML = "danmaku.xml"
const val ENTRY_JSON = "entry.json"
const val VIDEO_M4S = "video.m4s"
const val AUDIO_M4S = "audio.m4s"
const val DEFAULT_DANMAKU_SIZE = 72

const val ADM_PACK_NAME = "com.dv.adm"
const val ADM_EDITOR = "$ADM_PACK_NAME.AEditor"
const val ADM_PRO_PACK_NAME = "$ADM_PACK_NAME.pay"
const val ADM_PRO_EDITOR = "$ADM_PRO_PACK_NAME.AEditor"

const val IDM_PACK_NAME = "idm.internet.download.manager"
const val IDM_DOWNLOADER = "$IDM_PACK_NAME.Downloader"
const val IDM_PLUS_PACK_NAME = "$IDM_PACK_NAME.plus"

// todo 移动到 datastore
@Singleton
class DownloadManage @Inject constructor(
    private val videoRepository: IVideoDataSources,
    private val danmakuRepository: IDanmakuDataSources,
    private val json: Json,
    private val okDownload: OkDownload,
    @ApplicationContext private val context: Context,
    @AppCoroutineScope private val scope: CoroutineScope,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : IDownloadManage {
    // todo 等待实现
    fun launchThirdPartyDownload(downloader: ThirdPartyDownloader) {
        when (downloader) {
            ThirdPartyDownloader.ADM -> TODO()
            ThirdPartyDownloader.ADMPro -> TODO()
            ThirdPartyDownloader.IDM -> TODO()
            ThirdPartyDownloader.IDMPlus -> TODO()
            ThirdPartyDownloader.NONE -> TODO()
            else -> {}
        }
    }

    fun checkThirdPartyDownloader(): ThirdPartyDownloader {
        return if (checkInstall(ADM_PRO_PACK_NAME)) {
            ThirdPartyDownloader.ADMPro
        } else if (checkInstall(ADM_PACK_NAME)) {
            ThirdPartyDownloader.ADM
        } else if (checkInstall(IDM_PLUS_PACK_NAME)) {
            ThirdPartyDownloader.IDMPlus
        } else if (checkInstall(IDM_PACK_NAME)) {
            ThirdPartyDownloader.IDM
        } else {
            ThirdPartyDownloader.NONE
        }
    }

    fun checkInstall(pkgName: String): Boolean {
        return AppFilePathUtils.isInstallApp(context, pkgName)
    }

    fun admDownload(url: String) {
        val result = checkThirdPartyDownloader()
        if (result is ThirdPartyDownloader.NONE) {
            Toaster.show("看起来你还没有安装下载器")
            return
        }
        val intent = getIntent(url)
        when (result) {
            is ThirdPartyDownloader.ADM -> {
                Toaster.show("正在拉起ADM")
                intent.setClassName(result.pkgName, result.clsName)
                context.startActivity(intent)
            }

            is ThirdPartyDownloader.ADMPro -> {
                Toaster.show("正在拉起ADM")
                intent.setClassName(result.pkgName, result.clsName)
                context.startActivity(intent)
            }

            else -> Unit
        }
    }

    // todo fix toast 移动到界面上去
    fun idmDownload(url: String) {
        val result = checkThirdPartyDownloader()
        if (result is ThirdPartyDownloader.NONE) {
            Toaster.show("看起来你还没有安装下载器")
            return
        }
        val intent = getIntent(url)
        when (result) {
            is ThirdPartyDownloader.IDM -> {
                Toaster.show("正在拉起IDM")
                intent.setClassName(result.pkgName, result.clsName)
                context.startActivity(intent)
            }

            is ThirdPartyDownloader.IDMPlus -> {
                Toaster.show("正在拉起IDM")
                intent.setClassName(result.pkgName, result.clsName)
                context.startActivity(intent)
            }

            else -> Unit
        }
    }

    private fun getIntent(url: String): Intent {
        return Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            putExtra(REFERER, BILIBILI_WEB_URL)
            putExtra(USER_AGENT, BROWSER_USER_AGENT)

            data = Uri.parse(url)
        }
    }

    fun downloadDanmaku(cid: String, aid: Long) {
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

    private fun buildEntryJson(qn: Int, bvid: String, pageData: PageData) {
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
                            "page_data": ${json.encodeToString(pageData)}
                        }
        """.trimIndent()
    }

    fun findAllTask() {
    }

    fun deleteFile() {
    }

    private fun addToQueue(info: PlayerInfo, detail: VideoDetails, quality: Int) {
        val videoList =
            info.dash.video.groupBy { it.id }[quality]
                ?: info.dash.video.groupBy { it.id }.maxBy { it.key }.value
        val video = videoList.maxBy { it.codecid }
        val audio = info.dash.audio.maxBy { it.id }

        val cid = detail.cid
        val path = videoSavePath(
            detail.aid,
            cid,
            quality
        )

        okDownload.enqueueTask(
            path,
            cid,
            video.baseUrl,
            video.backupUrl,
            DownloadTag.VIDEO,
        )
        okDownload.enqueueTask(
            path,
            cid,
            audio.baseUrl,
            audio.backupUrl,
            DownloadTag.AUDIO,
        )
    }

    private suspend fun downloadDanmuku(aid: Long, cid: Long, duration: Int): Unit =
        withContext(ioDispatcher) {
            val reply = danmakuRepository.protoWbi(cid, 1)
            val sb = StringBuilder(reply.elems.size * DEFAULT_DANMAKU_SIZE)
            sb.append("<i>")
                .append("<chatserver>chat.bilibili.com</chatserver>")
                .append("<chatid>$cid</chatid>")
                .append("<mission>0</mission>")
                .append("<maxlimit>6000</maxlimit>")
                .append("<state>0</state>")
                .append("<real_name>0</real_name>")
                .append("<source>k-v</source>")
            for (elem in reply.elems) {
                sb.append(
                    "<d p=\"${elem.progress},${elem.mode},${elem.fontsize},${elem.color},${elem.ctime},${elem.pool},${elem.midHash},${elem.id},10\">${elem.content}</d>"
                )
            }
            val content = sb.append("</i>").toString()
            val path = dmSavePath(aid, cid)
            File(path, DANMAKU_XML).writeText(content)
        }

    private fun videoSavePath(aid: Long, cid: Long, quality: Int): String {
        return "${dmSavePath(aid, cid)}${File.separator}$quality"
    }

    private fun dmSavePath(aid: Long, cid: Long): String {
        return "${context.downloadDir}${File.separator}$aid${File.separator}c_$cid"
    }

    override fun getAllTask(path: String): List<Entry> {
        val scan = scan(path)
        return decodeEntry(scan)
    }
    fun getAllTaskFlow(path: String): Flow<List<Entry>> {
        val scan = scan(path)
        return flowOf(decodeEntry(scan))
    }
    override fun downloadDanmaku(cid: Long, second: Int) {
        val i = (second / 6 * 60) + 1

        scope.launch {
            for (index in 1..i) {
                danmakuRepository.protoWbi(cid, i)
                delay(1.seconds)
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun decodeEntry(scanResult: ArrayDeque<MutableList<File>>): List<Entry> {
        val decodeResult = scanResult
            .filter { it.size <= 4 }
//            .filterNot { it.size > 4 }
            .map { files ->
                val entryFile = files.removeFirst()
                val entry = json.decodeFromStream(Entry.serializer(), entryFile.inputStream())
                entry.copy(
                    vFile = files.find { it.name == VIDEO_M4S },
                    aFile = files.find { it.name == AUDIO_M4S },
                    dFile = files.find { it.name == DANMAKU_XML },
                )
            }
        return decodeResult
    }

    private fun scan(path: String): ArrayDeque<MutableList<File>> {
        val scannedFiles = ArrayDeque<MutableList<File>>(32)
        File(path)
            .walkTopDown()
            .forEach { file ->
                if (file.name == ENTRY_JSON) {
                    scannedFiles.addLast(mutableListOf(file))
                }
                if (file.name == VIDEO_M4S || file.name == AUDIO_M4S || file.name == DANMAKU_XML) {
                    scannedFiles.last {
                        it.add(file)
                    }
                }
            }
        return scannedFiles
    }

    fun addTask(bvid: String, quality: Int) {

    }
}