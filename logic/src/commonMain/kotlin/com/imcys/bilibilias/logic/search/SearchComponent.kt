package com.imcys.bilibilias.logic.search

import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent : BackHandlerOwner {
    val searchQuery: StateFlow<String>
    val searchResultUiState: StateFlow<SearchResultUiState>
    fun onSearchTriggered(query: String)
    fun onSearchQueryChanged(query: String)
    fun downloadItem(qn: Int, bvid: String, cid: Long)
}