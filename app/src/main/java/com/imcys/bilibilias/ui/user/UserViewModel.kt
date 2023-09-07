package com.imcys.bilibilias.ui.user

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val http: HttpClient) : BaseViewModel() {
    private val _userDataState = MutableStateFlow("")
    val userDataState = _userDataState.asStateFlow()

    init {
        getUserInformation()
    }

    fun getUserInformation() {
        launchIO {
            val data = http.get(BilibiliApi.getMyUserData).body<MyUserData>()
            _userDataState.update {
                data.toString()
            }
        }
    }
}