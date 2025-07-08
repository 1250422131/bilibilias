package com.imcys.bilibilias.logic.search

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
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
    @OptIn(ExperimentalStateKeeperApi::class)
    private var state: State by saveable(serializer = State.serializer(), init = ::State)

    override val searchQuery = MutableStateFlow(state.searchQuery)
    override val searchResultUiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                val index = query.indexOf("BV1", ignoreCase = true)
                Logger.d { "query: $query" }
                if (index != -1 && index + 12 <= query.length) {
                    val bvid = query.substring(index, index + 12)
                    Logger.d { "bvid: $bvid" }
                    val detail = BilibiliApi.getVideoDetail(bvid)
                    flowOf(
                        SearchResultUiState.Success(
                            detail.aid,
                            bvid = detail.bvid,
                            desc = detail.desc,
                            cover = detail.pic,
                            title = detail.title,
                            ownerId = detail.owner.mid,
                            ownerFace = detail.owner.face,
                            ownerName = detail.owner.name
                        )
                    )
                } else {
                    flowOf(SearchResultUiState.LoadFailed)
                }
            }
        }.stateIn(
            scope,
            SharingStarted.WhileSubscribed(5_000),
            SearchResultUiState.Loading
        )

    override fun onSearchTriggered(query: String) {

    }

    override fun onSearchQueryChanged(query: String) {
        state = State(query)
        searchQuery.update { query }
    }

    @Serializable
    private data class State(val searchQuery: String = "")

    @Serializable
    sealed class BottomSheetChild {
        @Serializable
        data object Sheet : BottomSheetChild()

    }
}