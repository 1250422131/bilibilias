package com.imcys.bilibilias.logic.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class DefaultSearchComponent(
    componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {
    private val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    @OptIn(ExperimentalStateKeeperApi::class)
    private var state: State by saveable(serializer = State.serializer(), init = ::State)

    override val searchQuery = MutableStateFlow(state.searchQuery)
    override val searchResultUiState: StateFlow<SearchResultUiState>
        get() = TODO("Not yet implemented")

    override fun onSearchTriggered(query: String) {

    }

    override fun onSearchQueryChanged(query: String) {

    }

    @Serializable
    private data class State(val searchQuery: String = "")
}