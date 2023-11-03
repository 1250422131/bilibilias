package com.imcys.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.datastore.fastkv.CookiesData
import com.imcys.network.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val cookiesData: CookiesData
) : ViewModel() {

    /**
     * TODO 未通过测试
     */
    fun refreshCookie() {
        viewModelScope.launch {
            try {
                val (refresh, timestamp) = loginRepository.checkCookieNeedRefresh()
                Timber.tag("refreshCookie").d("timestamp=$timestamp")
                val time = System.currentTimeMillis()
                val correspondPath = loginRepository.getCorrespondPath(time)
                Timber.tag("refreshCookie").d("correspondPath=$correspondPath")

                val refreshCsrf = loginRepository.getRefreshCsrf(correspondPath)
                Timber.tag("refreshCookie").d("refreshCsrf=$refreshCsrf")

                val refreshCookie = loginRepository.refreshCookie(refreshCsrf)
                Timber.tag("refreshCookie").d("refreshCookie=$refreshCookie")

                Timber.tag("refreshCookie").d("confirmRefresh")
                loginRepository.confirmRefresh(refreshCookie)
            } catch (e: Exception) {
                Timber.tag("refreshCookieEx").d(e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
            cookiesData.clearCookieData()
        }
    }
}
