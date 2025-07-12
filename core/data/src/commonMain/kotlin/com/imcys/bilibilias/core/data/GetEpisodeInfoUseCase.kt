package com.imcys.bilibilias.core.data

import com.imcys.bilibilias.core.data.TextExtraction.textExtract
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.model.EpisodeInfo
import com.imcys.bilibilias.core.model.EpisodePartInfo
import com.imcys.bilibilias.core.model.Owner
import com.imcys.bilibilias.core.model.StreamData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class GetEpisodeInfoUseCase {
    operator fun invoke(query: String): Flow<EpisodeInfo?> {
        return when (val result = textExtract(query)) {
            is TextExtraction.MatchResult.BV -> {
                flow {
                    val detail = BilibiliApi.getVideoInfoDetail(result.id)
                    val playUrl = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)
                    // 后端发送过来的所有画质选项
                    val backendControlQuality =
                        playUrl.acceptQuality.zip(playUrl.acceptDescription).toMap()
                    // 可以播放的画质选项
                    val playerControlQuality = playUrl.dash.video.map { it.id }.toSet()
                    val episodeInfo = EpisodeInfo(
                        bvid = detail.bvid,
                        desc = detail.desc,
                        cover = detail.pic,
                        title = detail.title,
                        owner = Owner(detail.owner.mid, detail.owner.face, detail.owner.name),
                        parts = detail.pages.map { EpisodePartInfo(it.cid, it.part, it.page) },
                        video = playUrl.dash.video.map {
                            val q = backendControlQuality[it.id] ?: error("map error")
                            StreamData(
                                id = it.id,
                                baseUrl = it.baseUrl,
                                backupUrl = it.backupUrl,
                                codecId = 0,
                                description = q
                            )
                        },
                        audio = playUrl.dash.audio.map {
                            StreamData(
                                id = it.id,
                                baseUrl = it.baseUrl,
                                backupUrl = it.backupUrl,
                            )
                        },
                    )

                    emit(episodeInfo)
                }
            }

            TextExtraction.MatchResult.Error -> flowOf(null)
        }
    }
}