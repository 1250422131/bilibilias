package com.imcys.bilibilias.ui.tools.parser

import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.network.AsCookiesStorage
import io.ktor.http.Cookie

class WebParserViewModel(
    val asCookiesStorage: AsCookiesStorage
): ViewModel() {

    suspend fun getAllCookies(): MutableList<Cookie> {
        return asCookiesStorage.getAllCookies()
    }

}