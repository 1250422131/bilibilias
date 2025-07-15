package com.imcys.bilibilias.logic.search

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.imcys.bilibilias.logic.login.LoginComponent
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, SearchChild>>
    val searchQuery: StateFlow<String>
    val searchResultUiState: StateFlow<SearchResultUiState>
    fun onSearchTriggered(query: String)
    fun onSearchQueryChanged(query: String)
    fun downloadItem(qn: Int, bvid: String, cid: Long)
    fun onBackClicked()
    fun onLoginClicked()
    sealed class SearchChild {
        data object Main : SearchChild()
        data class Login(val component: LoginComponent) : SearchChild()
    }
}