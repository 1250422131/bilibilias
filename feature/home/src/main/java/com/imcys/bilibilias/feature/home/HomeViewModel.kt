package com.imcys.bilibilias.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.network.repository.BiliBiliAsRepository
import com.imcys.bilibilias.core.network.repository.LoginRepository
import com.imcys.bilibilias.core.network.utils.WBIUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val asRepository: BiliBiliAsRepository,
    private val loginRepository: LoginRepository,
    private val loginInfoDataSource: LoginInfoDataSource
) : ViewModel() {
    // TODO: 缺少异常处理，需要处理超时异常
    val homeUiState: StateFlow<HomeUiState> = flowOf(HomeUiState.Empty).flatMapLatest {
        val updateNoticeFlow = flow { emit(asRepository.getUpdateNotice()) }
        val bannerFlow = flow { emit(asRepository.getHomeBanner()) }
        combine(updateNoticeFlow, bannerFlow) { notice, banner ->
            HomeUiState.Success(notice, banner)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        HomeUiState.Loading
    )

    init {
        viewModelScope.launch {
            saveDailyToken()
        }
    }

    fun postSignatureMessage(sha: String, md5: Pair<String, Long>, crc: String) {
        viewModelScope.launch {
            asRepository.postSignatureMessage(sha, md5, crc)
        }
    }

    fun exitAccountLogin() {
        viewModelScope.launch {
            loginInfoDataSource.setLoginState(false)
            loginRepository.exitLogin()
        }
    }

    private suspend fun saveDailyToken() {
        loginRepository.getBilibiliHome()
        val bar = loginRepository.导航栏用户信息()
        loginInfoDataSource.setMid(bar.mid)
        loginInfoDataSource.setMixKey(WBIUtils.getMixinKey(bar.imgKey, bar.subKey))
    }
}
