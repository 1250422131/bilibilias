package com.imcys.bilibilias.ui.download

import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.data.entity.DownloadTaskInfo
import com.imcys.bilibilias.common.data.repository.DownloadTaskRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val downloadQueue: DownloadQueue,
    private val downloadTaskRepository: DownloadTaskRepository,
    val groupDownloadProgress: GroupDownloadProgress
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
        details: VideoDetails,
        downloadOptionsStateHolders: DownloadOptionsStateHolders
    ) {
        Timber.tag("downloadInfo").d(downloadOptionsStateHolders.toString())
        launchIO {
            downloadOptionsStateHolders.subset.asFlow().map {
                delay(1.seconds)
                videoRepository.getDashVideoStream(details.bvid, it.cid) to it
            }.catch {
                Timber.tag("下载视频异常").d(it)
            }.collect { (dash, page) ->
                Timber.tag("audio").d("audio=${dash.dash.audio},quality=${downloadOptionsStateHolders.audioQuality}")
                downloadQueue.addTask(
                    details,
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

    private val _state = MutableStateFlow(DownloadState())
    val state = _state.asStateFlow()
    fun allTask() {
        launchIO {
            _state.update {
                it.copy(tasks = downloadTaskRepository.allDownloadTask())
            }
        }
    }
}

data class DownloadState(val tasks: List<DownloadTaskInfo> = emptyList())
