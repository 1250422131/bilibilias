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
        viewModelScope.launch {
            loginRepository.logout()
            cookiesData.clearCookieData()
        }
    }
}
