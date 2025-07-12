package com.imcys.bilibilias.logic.search

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.imcys.bilibilias.core.data.GetEpisodeInfoUseCase
import com.imcys.bilibilias.core.data.MediaSourceSelectedUseCase
import com.imcys.bilibilias.core.result.Result.Error
import com.imcys.bilibilias.core.result.Result.Loading
import com.imcys.bilibilias.core.result.Result.Success
import com.imcys.bilibilias.core.result.asResult
import com.imcys.bilibilias.logic.utils.createKtorPersistentHttpDownloader
import com.imcys.bilibilias.logic.utils.scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class DefaultSearchComponent(
    componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {
    private val httpDownloader = createKtorPersistentHttpDownloader()
    private val episodeInfoUseCase = GetEpisodeInfoUseCase()
    private val mediaSourceSelectedUseCase = MediaSourceSelectedUseCase()

    @OptIn(ExperimentalStateKeeperApi::class)
    private var state: State by saveable(serializer = State.serializer(), init = ::State)

    override val searchQuery = MutableStateFlow(state.searchQuery)

    // use case
    override val searchResultUiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                episodeInfoUseCase(query).asResult().map { result ->
                    when (result) {
                        is Success -> {
                            val data = result.data
                            if (data != null) {
                                SearchResultUiState.Success(data)
                            } else {
                                SearchResultUiState.Error("No data")
                            }
                        }

                        is Error -> SearchResultUiState.Error(
                            result.exception.message ?: "Unknown error"
                        )

                        is Loading -> SearchResultUiState.Loading
                    }
                }
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

    override fun downloadItem(qn: Int, bvid: String, cid: Long) {
        Logger.i { "downloadItem: $qn, $bvid, $cid" }
        scope.launch {
            val data = mediaSourceSelectedUseCase(qn, bvid, cid)
        }
    }


    @Serializable
    private data class State(val searchQuery: String = "")

    @Serializable
    private data object DialogConfig
}
