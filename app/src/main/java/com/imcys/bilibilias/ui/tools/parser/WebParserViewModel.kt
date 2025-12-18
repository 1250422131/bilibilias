package com.imcys.bilibilias.ui.tools.parser

import androidx.lifecycle.ViewModel
import com.google.protobuf.copy
import com.imcys.bilibilias.network.AsCookiesStorage
import io.ktor.http.Cookie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class WebParserUIState(
    val currentUrl: String = "https://m.bilibili.com/",
)

class WebParserViewModel(
    val asCookiesStorage: AsCookiesStorage
) : ViewModel() {


    val uiState: StateFlow<WebParserUIState>
        field = MutableStateFlow(WebParserUIState())

    suspend fun getAllCookies(): MutableList<Cookie> {
        return asCookiesStorage.getAllCookies()
    }

    // 更新当前URL
    fun updateCurrentUrl(newUrl: String) {
        uiState.value = uiState.value.copy(currentUrl = newUrl)
    }

}