package com.imcys.network.download

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.hjq.toast.Toaster
import com.imcys.bilibilias.dm.DmSegMobileReply
import com.imcys.common.di.AppCoroutineScope
import com.imcys.common.utils.AppFilePathUtils
import com.imcys.model.Dash
import com.imcys.model.NetworkPlayerPlayUrl
import com.imcys.model.ViewDetail
import com.imcys.model.download.CacheFile
import com.imcys.model.download.Entry
import com.imcys.network.constant.BILIBILI_WEB_URL
import com.imcys.network.constant.BROWSER_USER_AGENT
import com.imcys.network.constant.REFERER
import com.imcys.network.constant.USER_AGENT
import com.imcys.network.repository.danmaku.IDanmakuDataSources
import com.imcys.network.repository.video.IVideoDataSources
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

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
) : IDownloadManage {
    // todo 等待实现
    // region 移动到更上层
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

    // endregion
    fun downloadDanmaku() {}

    private fun buildEntryJsonFileContent(
        qn: Int,
        detail: ViewDetail,
        seasionId: Long? = null
    ): String {
        return """
                      {
                            "media_type": 2,
                            "has_dash_audio": true,
                            "is_completed": true,
                            "total_bytes": 下载大小,
                            "downloaded_bytes": 下载大小,
                            "title": "${detail.title}",
                            "type_tag": "$qn",
                            "cover": "${detail.pic}",
                            "video_quality": $qn,
                            "prefered_video_quality": $qn,
                            "guessed_total_bytes": 0,
                            "total_time_milli": ${detail.duration},
                            "danmaku_count": ${detail.stat.danmaku},
                            "time_update_stamp": ${System.currentTimeMillis()},
                            "time_create_stamp": ${System.currentTimeMillis()},
                            "can_play_in_advance": true,
                            "interrupt_transform_temp_file": false,
                            "quality_pithy_description": $qn,
                            "quality_superscript": "码率",
                            "cache_version_code": 7630200,
                            "preferred_audio_quality": 0,
                            "audio_quality": 0,
                            
                            "avid": ${detail.aid},
                            "spid": 0,
                            "seasion_id": ${seasionId ?: 0},
                            "bvid": "${detail.bvid}",
                            "owner_id": ${detail.owner.mid},
                            "owner_name": "${detail.owner.name}",
                            "owner_avatar": "${detail.owner.face}",
                            "page_data": ${json.encodeToString(detail.pages)}
                        }
        """.trimIndent()
    }

    fun findAllTask() {
    }

    fun deleteFile() {
    }

    private fun addToQueue(info: NetworkPlayerPlayUrl, detail: ViewDetail, quality: Int) {
        val video = getVideoStrategy(info, quality)
        val audio = getAudioStrategy(info)

        val cid = detail.cid
        val path = buildCacheFullPath(detail.aid, cid, quality)
        enqueueTasksTogether(path, cid, video, audio)
        scope.launch {
            recordDownloadInfo(quality, detail)
            recordDMInfo(detail.aid, cid, detail.duration)
        }
    }

    /**
     * 记录下载信息
     */
    private suspend fun recordDownloadInfo(quality: Int, detail: ViewDetail) {
        File(buildCacheBasePath(detail.aid, detail.cid), ENTRY_JSON)
            .writeText(buildEntryJsonFileContent(quality, detail))
    }

    /**
     * 获取视频策略
     */
    private fun getVideoStrategy(info: NetworkPlayerPlayUrl, quality: Int): Dash.Video {
        val videos = info.dash.video
        val videosGroup = videos.groupBy { it.id }
        val videoList =
            videosGroup[quality] ?: videosGroup.maxBy { it.key }.value
        return videoList.maxBy { it.codecid }
    }

    /**
     * 获取音频策略
     */
    private fun getAudioStrategy(info: NetworkPlayerPlayUrl): Dash.Audio {
        return info.dash.audio.maxBy { it.id }
    }

    private fun enqueueTasksTogether(
        path: String,
        cid: Long,
        video: Dash.Video,
        audio: Dash.Audio
    ) {
        okDownload.enqueueTask(
            path,
            cid,
            audio.baseUrl,
            audio.backupUrl,
            DownloadTag.AUDIO,
        )
        okDownload.enqueueTask(
            path,
            cid,
            video.baseUrl,
            video.backupUrl,
            DownloadTag.VIDEO,
        )
    }

    private suspend fun recordDMInfo(aid: Long, cid: Long, duration: Int) {
        suspend fun getAllDMSegment(): List<DmSegMobileReply> {
            val segment = mutableListOf<DmSegMobileReply>()
            val index = duration / 360 + 1
            for (i in 1..index) {
                val reply = danmakuRepository.protoWbi(aid, cid, i, 1)
                segment.add(reply)
            }
            return segment
        }

        val allDMSegment = getAllDMSegment()

        File(buildCacheBasePath(aid, cid), DANMAKU_XML).writeText(
            buildDMXmlFileContent(
                allDMSegment,
                cid
            )
        )
    }

    private fun buildDMXmlFileContent(dmSegment: List<DmSegMobileReply>, cid: Long): String {
        val sb = StringBuilder(dmSegment.sumOf { it.elems.size } * DEFAULT_DANMAKU_SIZE)
        sb.appendLine("<i>")
            .appendLine("<chatserver>chat.bilibili.com</chatserver>")
            .appendLine("<chatid>$cid</chatid>")
            .appendLine("<mission>0</mission>")
            .appendLine("<maxlimit>6000</maxlimit>")
            .appendLine("<state>0</state>")
            .appendLine("<real_name>0</real_name>")
            .appendLine("<source>k-v</source>")
        for (segment in dmSegment) {
            for (elem in segment.elems) {
                sb.appendLine(
                    "<d p=\"${elem.progress},${elem.mode},${elem.fontsize},${elem.color},${elem.ctime},${elem.pool},${elem.midHash},${elem.id},10\">${elem.content}</d>"
                )
            }
        }
        return sb.appendLine("</i>").toString()
    }

    private fun buildCacheFullPath(aid: Long, cid: Long, quality: Int): String {
        return "${buildCacheBasePath(aid, cid)}${File.separator}$quality"
    }

    private fun buildCacheBasePath(aid: Long, cid: Long): String {
        return "${context.downloadDir}${File.separator}$aid${File.separator}c_$cid"
    }

    override fun getAllTask(path: String): List<CacheFile> {
        val scan = TopDownAccess(path)
        return decodeEntryFile(scan)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun decodeEntryFile(result: ArrayDeque<MutableList<File>>): List<CacheFile> {
        return result
            .filter { it.size <= 4 }
            .map { files ->
                val entryFile = files.removeFirst()
                val entry = json.decodeFromStream(Entry.serializer(), entryFile.inputStream())
                CacheFile(
                    0, collectionName = entry.title, chapterName = entry.pageData.part,
                    audio = files.find { it.name == AUDIO_M4S }!!,
                    video = files.find { it.name == VIDEO_M4S }!!,
                    danmaku = files.find { it.name == DANMAKU_XML }!!,
                    bvId = entry.bvid,
                    avId = entry.avid,
                    chapters = emptyList(),
                    cover = entry.cover
                )
            }
    }

    private fun TopDownAccess(path: String): ArrayDeque<MutableList<File>> {
        val result = ArrayDeque<MutableList<File>>(32)
        File(path)
            .walkTopDown()
            .forEach { file ->
                if (file.name == ENTRY_JSON) {
                    result.addLast(mutableListOf(file))
                }
                if (file.name == VIDEO_M4S || file.name == AUDIO_M4S || file.name == DANMAKU_XML) {
                    result.last {
                        it.add(file)
                    }
                }
            }
        return result
    }

    fun addTask(bvid: String, quality: Int) {
        scope.launch {
            val detail = videoRepository.getView(bvid, true)
            val playUrl = videoRepository.获取视频播放地址(detail.aid, detail.bvid, detail.cid)
            addToQueue(playUrl, detail, quality)
        }
    }

    fun addTask(bvList: List<String>, quality: Int) {
        bvList.forEach {
            addTask(it, quality)
        }
    }
}
