package com.imcys.bilibilias.ui.download

import com.imcys.bilibilias.base.utils.DownloadManage
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import com.tonyodev.fetch2.Download
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.io.path.Path
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val downloadManage: DownloadManage
) : BaseViewModel() {

    private val _state = MutableStateFlow(DownloadListState())
    val state = _state.asStateFlow()

    init {
        findAllTask()
    }

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
            }
                .catch {
                    Timber.tag("下载视频异常").d(it)
                }.collect { (dash, page) ->
                    Timber.tag("audio").d("audio=${dash.dash.audio},quality=${downloadOptionsStateHolders.audioQuality}")
                    downloadManage.addTask(
                        details,
                        dash = dash,
                        page = page,
                        downloadOptionsStateHolders = downloadOptionsStateHolders
                    )
                }
        }
    }

    private fun findAllTask() {
        launchOnIO {
            downloadManage.findAllTask { tasks ->
                _state.update { downloadListState ->
                    val bvGroup = tasks.groupBy { it.bvid }
                    downloadListState.copy(bvGroup = bvGroup)
                }
            }
        }
    }

    fun deleteFileById(id: Int) {
        downloadManage.deleteFile(id, { download ->
            Timber.tag("deleteFile").d(download.toString())
            // /storage/emulated/0/Android/data/com.imcys.bilibilias.debug/files/download/BV1ky4y1N7K2/c_1300196787/112/video.m4s

        }, {

        })
        findAllTask()
    }
    fun deleteGroupFile(groupId: Long) {
        // downloadManage.deleteFile(groupId, {}, {})
    }
}

data class DownloadListState(
    val bvGroup: Map<String, List<Download>> = emptyMap()
)
