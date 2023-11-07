package com.imcys.player.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.offline.Download
import com.imcys.model.video.Page
import com.imcys.network.download.DownloadListHolders
import com.imcys.network.download.DownloadManage
import com.imcys.network.download.DownloadToolType
import com.imcys.network.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val downloadManage: DownloadManage
) : ViewModel() {

    private val _state = MutableStateFlow(DownloadListState())
    val state = _state.asStateFlow()

    init {
        findAllTask()
        getPages()
        setInitAcceptDescription()
    }

    private fun getPages() {
        when (val res = videoRepository.videoDetails2.replayCache.lastOrNull()) {
            is com.imcys.common.utils.Result.Error -> TODO()
            com.imcys.common.utils.Result.Loading -> TODO()
            is com.imcys.common.utils.Result.Success -> _state.update {
                it.copy(availablePages = res.data.pages, selectedPages = listOf(res.data.pages.first()))
            }

            null -> Timber.d("replayCache is null")
        }
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
        details: com.imcys.model.VideoDetails,
        downloadListHolders: DownloadListHolders
    ) {
        Timber.tag("downloadInfo").d(downloadListHolders.toString())
        // launchIO {
        //     downloadOptionsStateHolders.subset.asFlow().map {
        //         delay(1.seconds)
        //         videoRepository.getDashVideoStream(details.bvid, it.cid) to it
        //     }
        //         .catch {
        //             Timber.tag("下载视频异常").d(it)
        //         }.collect { (dash, page) ->
        //             downloadManage.addTask(
        //                 details,
        //                 dash = dash,
        //                 page = page,
        //                 downloadOptionsStateHolders = downloadOptionsStateHolders
        //             )
        //         }
        // }
    }

    fun downloadDanmaku(cid: Long, aid:Long) {
        downloadManage.downloadDanmaku(cid, aid)
    }

    private fun findAllTask() {
      viewModelScope.launch {
            downloadManage.findAllTask { tasks ->
                // _state.update { downloadListState ->
                //     // val bvGroup = tasks.groupBy { it.bvid }
                //     // downloadListState.copy(bvGroup = bvGroup)
                // }
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

    private fun setInitAcceptDescription() {
        videoRepository.dashVideo.replayCache.forEach { res ->
            when (res) {
                is com.imcys.common.utils.Result.Error -> TODO()
                com.imcys.common.utils.Result.Loading -> TODO()
                is com.imcys.common.utils.Result.Success -> _state.update {
                    it.copy(
                        selectedDescription = res.data.acceptDescription.first(),
                        availableAcceptDescription = res.data.acceptDescription,
                        availableQuality = res.data.acceptQuality,
                    )
                }
            }
        }
    }

    fun setAcceptDescription(index: Int) {
        _state.update {
            it.copy(
                selectedDescription = it.availableAcceptDescription[index],
                selectedQuality = it.availableQuality[index]
            )
        }
    }

    fun setPages(page: Page) {
        _state.update {
            val pageList = it.selectedPages
            if (page in pageList && pageList.size > 1) {
                it.copy(selectedPages = pageList - page)
            } else {
                it.copy(selectedPages = pageList + page)
            }
        }
    }

    fun setDownloadTool(downloadTool: DownloadToolType) {
        _state.update {
            it.copy(downloadTool = downloadTool)
        }
    }

    fun setRequireDownloadFileType(type: DownloadFileType) {
        _state.update {
            it.copy(requireDownloadFileType = type)
        }
    }
}

data class DownloadListState(
    val bvGroup: Map<String, List<Download>> = emptyMap(),
    val downloadTool: DownloadToolType = DownloadToolType.BUILTIN,
    val requireDownloadFileType: DownloadFileType = DownloadFileType.VideoAndAudio,

    // 可选剧集
    val availablePages: List<Page> = emptyList(),
    // 已选剧集
    val selectedPages: List<Page> = emptyList(),

    val availableQuality: List<Int> = emptyList(),
    val selectedQuality: Int = 0,

    // "真彩 HDR"
    val selectedDescription: String = "",
    // "真彩 HDR", "超清 4K", "高清 1080P60", "高清 1080P","高清 720P60", "清晰 480P", "流畅 360P"
    val availableAcceptDescription: List<String> = emptyList(),
)
