package com.imcys.bilibilias.logic.search

import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.imcys.bilibilias.core.domain.model.EpisodeCacheRequest
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent : BackHandlerOwner {
    val searchQuery: StateFlow<String>
    val searchResultUiState: StateFlow<SearchResultUiState>
    val selfInfoUiState: StateFlow<SelfInfoUiState>
    fun onSearchTriggered(query: String)
    fun onSearchQueryChanged(query: String)
    fun requestCache(request: EpisodeCacheRequest)
    fun onLogout()
}