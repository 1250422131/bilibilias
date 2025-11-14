package com.imcys.bilibilias.ui.tools.parser

import androidx.lifecycle.ViewModel
import com.google.protobuf.copy
import com.imcys.bilibilias.network.AsCookiesStorage
import io.ktor.http.Cookie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class WebParserUIState(
    val currentUrl: String = "https://m.bilibili.com/",
)

class WebParserViewModel(
    val asCookiesStorage: AsCookiesStorage
) : ViewModel() {


    private val _uiState = MutableStateFlow(WebParserUIState())
    val uiState = _uiState.asStateFlow()

    suspend fun getAllCookies(): MutableList<Cookie> {
        return asCookiesStorage.getAllCookies()
    }

    // 更新当前URL
    fun updateCurrentUrl(newUrl: String) {
        _uiState.value = _uiState.value.copy(currentUrl = newUrl)
    }

}