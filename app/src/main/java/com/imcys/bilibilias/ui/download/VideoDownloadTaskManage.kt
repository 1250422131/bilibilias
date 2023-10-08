package com.imcys.bilibilias.ui.download

import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class VideoDownloadTaskManage

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val httpClient: HttpClient,
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
        downloadOptionsStateHolders: DownloadOptionsStateHolders,
        foldername: String
    ) {
        launchIO {
            downloadOptionsStateHolders.subset.asFlow().map {
                delay(1.seconds)
                videoRepository.getDashVideoStream(bvid, it.cid, downloadOptionsStateHolders.videoQuality) to it
            }.collect { (dash, page) ->
                App.downloadQueue.addTask(
                    bvid = bvid,
                    cid = page.cid,
                    filename = page.part,
                    foldername = foldername,
                    videoUrl = dash.dash.video.first().baseUrl,
                    audioUrl = dash.dash.audio.find { it.id == downloadOptionsStateHolders.audioQuality }!!.baseUrl,
                    downloadMethod=  downloadOptionsStateHolders.toolType
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
}
