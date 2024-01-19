package com.imcys.player

import androidx.annotation.OptIn
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.bilias.core.domain.GetToolbarReportUseCase
import com.bilias.core.domain.GetVideoInSeries
import com.imcys.common.utils.Result
import com.imcys.common.utils.asResult
import com.imcys.model.PlayerInfo
import com.imcys.model.video.Stat
import com.imcys.model.video.ToolBarReport
import com.imcys.network.download.DownloadManage
import com.imcys.network.repository.video.IVideoDataSources
import com.imcys.player.navigation.BV_ID
import com.imcys.player.navigation.C_ID
import com.imcys.player.state.PlayInfoUiState
import com.imcys.player.state.PlayerUiState
import com.imcys.player.state.SeriesArchive
import com.imcys.player.state.mapToSeriesArchive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val videoRepository: IVideoDataSources,
    savedStateHandle: SavedStateHandle,
    getToolbarReportUseCase: GetToolbarReportUseCase,
    getVideoInSeries: GetVideoInSeries,
    private val downloadManage: DownloadManage
) : ViewModel() {

    private val bvid = savedStateHandle.getStateFlow(BV_ID, "")
    private val cid = savedStateHandle.getStateFlow(C_ID, "")

    val videoInfoUiState =
        bvid.flatMapLatest {
            videoInfoUiState(
                it,
                videoRepository,
                getToolbarReportUseCase,
                getVideoInSeries
            )
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PlayInfoUiState.Loading)

    /**
     * 播放器
     */
    val playerUiState = flow {
        emit(videoRepository.getPlayerPlayUrl(bvid.value, cid.value.toLong()))
    }.asResult()
        .map { result ->
            when (result) {
                is Result.Error -> PlayerUiState.LoadFailed
                Result.Loading -> PlayerUiState.Loading
                is Result.Success -> result.data.mapToPlayerUiState()
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, PlayerUiState.Loading)

    private fun PlayerInfo.mapToPlayerUiState(): PlayerUiState.Success {
        val pairs = acceptDescription.zip(acceptQuality).toImmutableList()
        return PlayerUiState.Success(
            qualityDescription = pairs,
            video = dash.video.toImmutableList(),
            audio = dash.audio.toImmutableList(),
            dolby = dash.dolby,
            duration = dash.duration,
            pageData = emptyList()
        )
    }

    /**
     * 下载视频
     */
    fun addToDownloadQueue(bvList: List<String>, quality: Int) {
        downloadManage.addTask(bvList, quality)
    }
}

private fun videoInfoUiState(
    bvId: String,
    videoRepository: IVideoDataSources,
    getToolbarReportUseCase: GetToolbarReportUseCase,
    getVideoInSeries: GetVideoInSeries,
): Flow<PlayInfoUiState> {
    val details = flow { emit(videoRepository.getDetail(bvId)) }
    val series = details.flatMapConcat { getVideoInSeries(it.owner.mid, it.aid) }

    val reportUseCase = getToolbarReportUseCase(bvId)
    return combine(
        details,
        reportUseCase,
        series,
        ::Triple,
    ).asResult()
        .map { result ->
            when (result) {
                is Result.Error -> PlayInfoUiState.LoadFailed
                Result.Loading -> PlayInfoUiState.Loading
                is Result.Success -> {
                    val (detail, report, series) = result.data
                    PlayInfoUiState.Success(
                        aid = detail.aid,
                        bvid = detail.bvid,
                        cid = detail.cid,
                        title = detail.title,
                        pic = detail.pic,
                        desc = detail.descV2?.firstOrNull()?.rawText ?: detail.desc,
                        owner = detail.owner,
                        toolBarReport = mapToToolBarReport(detail.stat, report),
                        archives = series?.mapToSeriesArchive() ?: persistentListOf(
                            SeriesArchive(
                                detail.aid,
                                detail.bvid,
                                detail.cid,
                                detail.title,
                            )
                        )
                    )
                }
            }
        }
}

private fun mapToToolBarReport(stat: Stat, report: ToolBarReport) = report.copy(
    like = stat.like,
    coin = stat.coin,
    favorite = stat.favorite,
    danmaku = stat.danmaku,
    evaluation = stat.evaluation,
    reply = stat.reply,
    share = stat.share,
    view = stat.view,
)

fun List<com.imcys.model.Dash.Video>.画质分组(): Map<Int, List<com.imcys.model.Dash.Video>> =
    groupBy { it.id }

fun List<com.imcys.model.Dash.Video>.画质最高队列(): List<com.imcys.model.Dash.Video> =
    groupBy { it.id }
        .maxBy { it.key }
        .value
        .sortedBy { it.codecid }

@OptIn(UnstableApi::class)
fun getDecoderInfo(mimeType: String) = MediaCodecUtil.getDecoderInfo(mimeType, false, false)

@OptIn(UnstableApi::class)
fun createMediaSource(
    dataSourceFactory: DataSource.Factory,
    url: String,
) = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
    MediaItem.Builder()
        .setUri(url)
        .build()
)

const val QUALITY_8K = 127
const val QUALITY_DOLBY = 126
const val QUALITY_HDR = 125
const val QUALITY_4K = 120
const val QUALITY_1080P_60 = 116
const val QUALITY_1080P_PLUS = 112
const val QUALITY_1080P = 80
const val QUALITY_720P_60 = 74
const val QUALITY_720P = 64
const val QUALITY_480P = 32
const val QUALITY_360P = 16
const val QUALITY_240P = 6
