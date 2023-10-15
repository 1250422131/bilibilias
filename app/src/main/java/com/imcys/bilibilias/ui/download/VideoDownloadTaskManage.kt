package com.imcys.bilibilias.ui.download

import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.model.video.Dash
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class VideoDownloadTaskManage

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val downloadQueue: DownloadQueue
) : BaseViewModel() {
    /**
     * qn 该值在 DASH 格式下无效，因为 DASH 格式会取到所有分辨率的流地址
     *
     * flv
     * "${BilibiliApi.videoPlayPath}?bvid=${videoDetails.bvid}&cid=${it.cid}&qn=$qn&fnval=0&fourk=1"
     *
     * dash
     * "${BilibiliApi.videoPlayPath}?bvid=${videoDetails.bvid}&cid=${it.cid}&qn=$qn&fnval=4048&fourk=1"
     */
    fun downloadVideo(
        bvid: String,
        downloadOptionsStateHolders: DownloadOptionsStateHolders
    ) {
        Timber.tag("downloadInfo").d(downloadOptionsStateHolders.toString())
        launchIO {
            downloadOptionsStateHolders.subset.asFlow().map {
                delay(1.seconds)
                videoRepository.getDashVideoStream(bvid, it.cid) to it
            }.catch {
                Timber.tag("下载视频异常").d(it)
            }.collect { (dash, page) ->
                val videos = dash.dash.video
                val videoUrl = getCurrentQualityEfficientEncode(videos, downloadOptionsStateHolders)
                Timber.tag("audio").d("audio=${dash.dash.audio},quality=${downloadOptionsStateHolders.audioQuality}")
                downloadQueue.addTask(
                    bvid = bvid,
                    cid = page.cid,
                    videoUrl = videoUrl,
                    audioUrl = dash.dash.audio.maxBy { it.id }.baseUrl,
                    toolType = downloadOptionsStateHolders.toolType,
                    qn = downloadOptionsStateHolders.videoQuality,
                    dash = dash,
                    page = page,
                    downloadOptionsStateHolders = downloadOptionsStateHolders
                )
            }
        }
        // val file = File(photoDir, "test.flv")
        // launchIO {
        //     httpClient.prepareGet("") {
        //         header(HttpHeaders.Referrer, BILIBILI_URL)
        //     }.execute { httpResponse ->
        //         httpResponse.bodyAsChannel().copyAndClose(file.writeChannel())
        //     }
        // }
    }

    /**
     * key
     * 7	AVC 编码	8K 视频不支持该格式
     * 12	HEVC 编码
     * 13	AV1 编码
     * 选择当前清晰度的高效编码 13-》12-》7
     */
    private fun getCurrentQualityEfficientEncode(
        videos: List<Dash.Video>,
        downloadOptionsStateHolders: DownloadOptionsStateHolders
    ): String = videos
        .groupBy { it.id }[downloadOptionsStateHolders.videoQuality]
        ?.maxBy { it.codecid }!!
        .baseUrl
}
