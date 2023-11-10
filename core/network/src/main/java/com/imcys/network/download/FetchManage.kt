package com.imcys.network.download

import android.content.Context
import com.imcys.common.di.AppCoroutineScope
import com.imcys.datastore.mmkv.SettingsRepository
import com.imcys.network.constants.BILIBILI_WEB_URL
import com.imcys.network.constants.BROWSER_USER_AGENT
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.FetchGroup
import com.tonyodev.fetch2.FetchGroupListener
import com.tonyodev.fetch2.NetworkType
import com.tonyodev.fetch2.Request
import com.tonyodev.fetch2.Status
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Extras
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineScope
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
    @AppCoroutineScope private val scope: CoroutineScope,
) : FetchGroupListener {

    private val fetch: Fetch

    init {
        val config = initConfig(context)
        fetch = initFetch(config)
    }

    private fun initConfig(context: Context) =
        FetchConfiguration.Builder(context)
            .setDownloadConcurrentLimit(4)
            .setAutoRetryMaxAttempts(10)
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
        headers[HttpHeaders.UserAgent] = BROWSER_USER_AGENT
        headers[HttpHeaders.Referrer] = BILIBILI_WEB_URL
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

    }

    override fun onCancelled(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onCancelled(download: Download) {
    }

    // private fun updateDownloadTaskInfo(download: Download) {
    //     val cid = download.cid
    //     val tag = download.tag
    //     scope.launch {
    //         val task = downloadTaskRepository.findByCidAndTag(cid, tag!!)?.apply {
    //             error = download.error.value
    //             state = download.status.value
    //         }
    //         if (task != null) {
    //             downloadTaskRepository.update(task)
    //         }
    //     }
    // }

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

    }

    override fun onDeleted(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onDeleted(download: Download) {

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

    }


    override fun onError(groupId: Int, download: Download, error: com.tonyodev.fetch2.Error, throwable: Throwable?, fetchGroup: FetchGroup) {
    }

    override fun onError(download: Download, error: com.tonyodev.fetch2.Error, throwable: Throwable?) {
    }

    override fun onPaused(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onPaused(download: Download) {

    }

    override fun onProgress(
        groupId: Int,
        download: Download,
        etaInMilliSeconds: Long,
        downloadedBytesPerSecond: Long,
        fetchGroup: FetchGroup
    ) {

    }

    override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
        Timber.tag("onProgress").d("$etaInMilliSeconds; $downloadedBytesPerSecond")
        download.progress
    }

    override fun onQueued(groupId: Int, download: Download, waitingNetwork: Boolean, fetchGroup: FetchGroup) {
    }

    override fun onQueued(download: Download, waitingOnNetwork: Boolean) {

    }

    override fun onRemoved(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onRemoved(download: Download) {

    }

    override fun onResumed(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onResumed(download: Download) {

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

    }

    override fun onWaitingNetwork(groupId: Int, download: Download, fetchGroup: FetchGroup) {
    }

    override fun onWaitingNetwork(download: Download) {

    }

    fun findAllTask(result: (List<Download>) -> Unit) {
        fetch.getDownloadsWithStatus(
            listOf(
                Status.NONE,
                Status.QUEUED,
                Status.DOWNLOADING,
                Status.PAUSED,
                Status.COMPLETED,
                Status.CANCELLED,
                Status.FAILED,
                Status.REMOVED,
                Status.DELETED,
                Status.ADDED,
            ),
            result
        )
    }

    fun deleteAll() {
        fetch.deleteAll()
    }

    fun deleteFiles(ids: List<Int>, onSuccess: (List<Download>) -> Unit, onError: (Error) -> Unit) {
        fetch.delete(ids, onSuccess, onError)
    }

    fun deleteFile(id: Int, onSuccess: (Download) -> Unit, onError: (Error) -> Unit) {
        fetch.delete(id, onSuccess, onError)
    }

    fun deleteGroup(id: Int, onSuccess: (List<Download>) -> Unit, onError: (Error) -> Unit) {
        fetch.deleteGroup(id, onSuccess, onError)
    }
}
