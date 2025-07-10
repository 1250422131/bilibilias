package com.imcys.bilibilias.core.data

import com.imcys.bilibilias.core.data.TextExtraction.textExtract
import com.imcys.bilibilias.core.data.model.CacheData
import com.imcys.bilibilias.core.data.model.Episode
import com.imcys.bilibilias.core.data.model.EpisodeItem
import com.imcys.bilibilias.core.data.model.MediaSources
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

    suspend fun getCacheData(id: String): CacheData {
        val detail = BilibiliApi.getVideoInfoDetail(id)
        val playUrl = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)
        return CacheData(
            title = detail.title,
            cover = detail.pic,
            pub = detail.pubDate,
            bvid = detail.bvid,
            cid = detail.cid,
            videos = playUrl.dash.video.map {
                MediaSources(
                    it.baseUrl,
                    it.id,
                    it.codecid,
                    it.height,
                    it.width
                )
            },
            audios = playUrl.dash.audio.map { MediaSources(it.baseUrl, it.id, it.codecid) },
        )
    }
}