package com.imcys.bilibilias.logic.search

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.logic.scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

class DefaultSearchComponent(
    componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {
    private val dialogNavigation = SlotNavigation<DialogConfig>()

    @OptIn(ExperimentalStateKeeperApi::class)
    private var state: State by saveable(serializer = State.serializer(), init = ::State)

    override val searchQuery = MutableStateFlow(state.searchQuery)
    override val searchResultUiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                extractBvid(query)?.let { bvid ->
                    val detail = BilibiliApi.getVideoDetail(bvid)
                    val playInfo = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)

                    val realQualities = playInfo.dash.video.map { it.id }.toSet()

                    val episodeQualities =
                        playInfo.acceptDescription.zip(playInfo.acceptQuality) { description, quality ->
                            EpisodeQuality(
                                description = description,
                                quality = quality,
                            )
                        }

                    val availableQualities =
                        episodeQualities.filterNot { it.quality !in realQualities }
                    flowOf(
                        SearchResultUiState.Success(
                            detail.aid,
                            bvid = detail.bvid,
                            desc = detail.desc,
                            cover = detail.pic,
                            title = detail.title,
                            ownerId = detail.owner.mid,
                            ownerFace = detail.owner.face,
                            ownerName = detail.owner.name,
                            episodes = detail.pages.map { Episode(it.cid, it.page, it.part) },
                            availableQualities = availableQualities,
                        )
                    )
                } ?: flowOf(SearchResultUiState.LoadFailed)
            }
        }.stateIn(
            scope,
            SharingStarted.WhileSubscribed(5_000),
            SearchResultUiState.Loading
        )

    private fun extractBvid(query: String): String? {
        val index = query.indexOf("BV1", ignoreCase = true)
        Logger.d { "query: $query" }
        return if (index != -1 && index + 12 <= query.length) {
            query.substring(index, index + 12)
        } else null
    }

    override fun onSearchTriggered(query: String) {

    }

    override fun onSearchQueryChanged(query: String) {
        state = State(query)
        searchQuery.update { query }
    }

    override fun downloadItem(quality: EpisodeQuality, bvid: String, cid: Long) {
        Logger.i { "downloadItem: $quality, $bvid, $cid" }
    }

    @Serializable
    private data class State(val searchQuery: String = "")

    @Serializable
    private data object DialogConfig
}

data class EpisodeQuality(
    val description: String,
    val quality: Int,
)

data class Episode(
    val cid: Long,
    val index: Int,
    val part: String,
)
