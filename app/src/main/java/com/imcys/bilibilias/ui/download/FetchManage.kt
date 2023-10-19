package com.imcys.bilibilias.ui.download

import android.content.Context
import com.imcys.bilibilias.common.base.config.SettingsRepository
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.BROWSER_USER_AGENT
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.constant.USER_AGENT
import com.imcys.bilibilias.common.data.entity.DownloadTaskInfo
import com.imcys.bilibilias.common.data.repository.DownloadTaskRepository
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.FetchGroup
import com.tonyodev.fetch2.FetchGroupListener
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Request
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Extras
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

const val TAG_VIDEO = "video"
const val TAG_AUDIO = "audio"

const val EXTRAS_CID = "cid"
const val EXTRAS_PAGE_TITLE = "pageTitle"
const val EXTRAS_BV_ID = "bvid"
const val EXTRAS_AV_ID = "avid"
const val EXTRAS_TITLE = "title"

@Singleton
@Suppress("TooManyFunctions")
class FetchManage @Inject constructor(
    @ApplicationContext context: Context,
    private val downloadTaskRepository: DownloadTaskRepository,
    private val scope: CoroutineScope,
    private val groupDownloadProgress: GroupDownloadProgress
) : FetchGroupListener {

    private val fetch: Fetch

    init {
        val config = initConfig(context)
        fetch = initFetch(config)
    }

    private fun initConfig(context: Context) =
        FetchConfiguration.Builder(context)
            .setDownloadConcurrentLimit(4)
            .setGlobalNetworkType(NetworkType.ALL)
            .build()

    private fun initFetch(config: FetchConfiguration): Fetch =
        Fetch.getInstance(config)
            .addListener(this)

    fun add(request: Request) {
        fetch.enqueue(request)
    }

    fun add(requests: List<Request>) {
        fetch.enqueue(requests)
    }

    fun createRequest(
        url: String,
        file: String,
        groupId: Long,
        tag: String,
        title: String,
        pageTitle: String,
        bvid: String,
        avid: Long,
    ) = Request(url, file).apply {
        headers[USER_AGENT] = BROWSER_USER_AGENT
        headers[REFERER] = BILIBILI_URL
        this.groupId = groupId.toInt()
        this.tag = tag
        extras = Extras(
            mapOf(
                EXTRAS_CID to groupId.toString(),
                EXTRAS_PAGE_TITLE to pageTitle,
                EXTRAS_BV_ID to bvid,
                EXTRAS_TITLE to title,
                EXTRAS_AV_ID to avid.toString()
            )
        )
    }

    override fun onAdded(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onAdded(download: Download) {
        downloadTaskRepository.insert(download.toEntity())
    }

    override fun onCancelled(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onCancelled(download: Download) {
        updateDownloadTaskInfo(download)
    }

    private fun updateDownloadTaskInfo(download: Download) {
        val cid = download.extras.getLong(EXTRAS_CID, 0)
        val tag = download.tag
        scope.launch {
            val task = downloadTaskRepository.findByCidAndTag(cid, tag!!)?.apply {
                error = download.error.value
                state = download.status.value
            }
            if (task != null) {
                downloadTaskRepository.update(task)
            }
        }
    }

    override fun onCompleted(groupId: Int, download: Download, fetchGroup: FetchGroup) {
        Timber.d(download.extras.toString())
        // 导入到B站不用合并视频
        if (SettingsRepository.autoImportToBilibili) {
            return
        }
        // 自动合并音视频关闭
        if (!SettingsRepository.autoMergeVideoAndAudio) {
            return
        }
        // val v = fetchGroup.completedDownloads.find { it.group == groupId && it.tag == TAG_VIDEO }
        // val a = fetchGroup.completedDownloads.find { it.group == groupId && it.tag == TAG_AUDIO }
        // if (v != null && a != null) {
        //     val 文件名 = v.file.replace(".m4s", ".mp4")
        //     Timber.tag("onCompleted").d(v.file)
        //     Timber.tag("onCompleted").d(a.file)
        //     RxFFmpegManage(v, a, 文件名).runBuiltCommand()
        // }
    }

    override fun onCompleted(download: Download) {
        updateDownloadTaskInfo(download)
    }

    override fun onDeleted(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onDeleted(download: Download) {
        updateDownloadTaskInfo(download)
    }

    override fun onDownloadBlockUpdated(
        groupId: Int,
        download: Download,
        downloadBlock: DownloadBlock,
        totalBlocks: Int,
        fetchGroup: FetchGroup
    ) {
    }

    override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
        updateDownloadTaskInfo(download)
    }

    override fun onError(
        groupId: Int,
        download: Download,
        error: Error,
        throwable: Throwable?,
        fetchGroup: FetchGroup
    ) {
    }

    override fun onError(download: Download, error: Error, throwable: Throwable?) {
        updateDownloadTaskInfo(download)
    }

    override fun onPaused(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onPaused(download: Download) {
        updateDownloadTaskInfo(download)
    }

    override fun onProgress(
        groupId: Int,
        download: Download,
        etaInMilliSeconds: Long,
        downloadedBytesPerSecond: Long,
        fetchGroup: FetchGroup
    ) {
        groupDownloadProgress.groupTask[download.extras.getLong(EXTRAS_CID, 0)] = fetchGroup.groupDownloadProgress
    }

    override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
        Timber.tag("onProgress").d("$etaInMilliSeconds; $downloadedBytesPerSecond")
        download.progress
    }

    override fun onQueued(groupId: Int, download: Download, waitingNetwork: Boolean, fetchGroup: FetchGroup) {
    }

    override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
        updateDownloadTaskInfo(download)
    }

    override fun onRemoved(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onRemoved(download: Download) {
        updateDownloadTaskInfo(download)
    }

    override fun onResumed(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onResumed(download: Download) {
        updateDownloadTaskInfo(download)
    }

    override fun onStarted(
        groupId: Int,
        download: Download,
        downloadBlocks: List<DownloadBlock>,
        totalBlocks: Int,
        fetchGroup: FetchGroup
    ) {
    }

    override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
        updateDownloadTaskInfo(download)
    }

    override fun onWaitingNetwork(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onWaitingNetwork(download: Download) {
        updateDownloadTaskInfo(download)
    }

    private fun Download.toEntity(): DownloadTaskInfo {
        return DownloadTaskInfo(
            title = extras.getString(EXTRAS_TITLE, ""),
            pageTitle = extras.getString(EXTRAS_PAGE_TITLE, ""),
            bvid = extras.getString(EXTRAS_BV_ID, ""),
            avid = extras.getLong(EXTRAS_AV_ID, 0),
            cid = extras.getLong(EXTRAS_CID, 0),
            file = file,
            fileUri = fileUri.toString(),
            fileType = tag,
            created = created,
            error = error.value,
            state = status.value
        )
    }
}
