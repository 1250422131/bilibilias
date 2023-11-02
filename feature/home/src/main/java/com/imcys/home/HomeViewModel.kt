package com.imcys.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.datastore.fastkv.CookiesData
import com.imcys.network.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val cookiesData: CookiesData
) : ViewModel() {

    fun logout() {
        val cookie = cookiesData.sessionData
        val jct = cookiesData.jct
        val userID = cookiesData.userID

        viewModelScope.launch {
            loginRepository.logout(cookie, jct, userID)
        }
        cookiesData.clearCookieData()
    }
}
