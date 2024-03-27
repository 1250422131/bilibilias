package com.imcys.bilibilias.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.datastore.LoginInfoDataSource
import com.imcys.bilibilias.core.network.repository.BiliBiliAsRepository
import com.imcys.bilibilias.core.network.repository.LoginRepository
import com.imcys.bilibilias.home.ui.viewmodel.home.HomeIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val asRepository: BiliBiliAsRepository,
    private val loginRepository: LoginRepository,
    private val loginInfoDataSource: LoginInfoDataSource
) : ViewModel() {
    private val _homeUiState = MutableStateFlow<HomeIntent>(HomeIntent.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            loginRepository.getBilibiliHome()
        }
    }

    fun getUpdateNotice() {
        viewModelScope.launch {
            _homeUiState.update {
                HomeIntent.UpdateNoticeIntent(asRepository.getUpdateNotice())
            }
        }
    }

    fun postSignatureMessage(sha: String, md5: Pair<String, Long>, crc: String) {
        viewModelScope.launch {
            asRepository.postSignatureMessage(sha, md5, crc)
        }
    }

    fun getBanner() {
        viewModelScope.launch {
            HomeIntent.BannerIntent(asRepository.getHomeBanner())
        }
    }

    fun exitAccountLogin() {
        viewModelScope.launch {
            loginInfoDataSource.setLoginState(false)
            loginRepository.exitLogin()
        }
    }
}
