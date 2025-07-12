package com.imcys.bilibilias.core.data

import com.imcys.bilibilias.core.data.TextExtraction.textExtract
import com.imcys.bilibilias.core.data.model.Episode
import com.imcys.bilibilias.core.data.model.EpisodeInfo
import com.imcys.bilibilias.core.data.model.EpisodeItem
import com.imcys.bilibilias.core.data.model.EpisodePartInfo
import com.imcys.bilibilias.core.data.model.EpisodeSource
import com.imcys.bilibilias.core.data.model.Owner
import com.imcys.bilibilias.core.data.model.Quality
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class GetEpisodeInfoUseCase {
    operator fun invoke(query: String): Flow<Episode?> {
        return when (val result = textExtract(query)) {
            is TextExtraction.MatchResult.BV -> {
                flow {
                    val detail = BilibiliApi.getVideoInfoDetail(result.id)
                    val playUrl = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)

                    val realQualities = playUrl.dash.video.map { it.id }.toSet()
                    val showQualities =
                        playUrl.acceptDescription.zip(playUrl.acceptQuality) { description, quality ->
                            Quality(description, quality)
                        }
                    emit(
                        Episode(
                            aid = detail.aid,
                            bvid = detail.bvid,
                            desc = detail.desc,
                            cover = detail.pic,
                            title = detail.title,
                            owner = Owner(
                                id = detail.owner.mid,
                                avatarUrl = detail.owner.face,
                                name = detail.owner.name
                            ),
                            series = detail.pages.map { EpisodeItem(it.cid, it.part, it.page) },
                            qualities = showQualities.filterNot { it.numeric !in realQualities }
                        )
                    )
                }
            }

            TextExtraction.MatchResult.Error -> flowOf(null)
        }
    }

    fun invoke2(query: String): Flow<EpisodeInfo?> {
        return when (val result = textExtract(query)) {
            is TextExtraction.MatchResult.BV -> {
                flow {
                    val detail = BilibiliApi.getVideoInfoDetail(result.id)
                    val playUrl = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)
                    EpisodeInfo(
                        bvid = detail.bvid,
                        desc = detail.desc,
                        cover = detail.pic,
                        title = detail.title,
                        owner = Owner(detail.owner.mid, detail.owner.face, detail.owner.name),
                        parts = detail.pages.map { EpisodePartInfo(it.cid, it.part, it.page) },
                        video = playUrl.dash.video.map {
                            EpisodeSource(
                                it.id.toString(),
                                it.codecid,
                                it.baseUrl,
                                it.backupUrl
                            )
                        },
                        audio = playUrl.dash.audio.map {
                            EpisodeSource(
                                it.id.toString(),
                                it.codecid,
                                it.baseUrl,
                                it.backupUrl
                            )
                        },
                    )


                    emit(TODO())
                }
            }

            TextExtraction.MatchResult.Error -> flowOf(null)
        }
    }
}