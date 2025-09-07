package com.imcys.bilibilias.logic.login

import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.core.logging.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid


class LoginViewModel(
    val cookieStateMachine: CookieStateMachine,
    val qrCodeStateMachine: QrCodeLoginStateMachine,
    private val preferences: AsPreferencesDataSource,
    private val api: BilibiliApi,
    private val applicationScope: CoroutineScope,
) : ViewModel() {

    override fun onCleared() {
        applicationScope.launch {
            val data = api.getNavigationData()
            preferences.setSelfInfo(
                SelfInfo(
                    Uuid.random(),
                    data.mid!!,
                    data.uname!!,
                    data.face!!
                )
            )
        }
    }
    companion object {
        private val logger = logger<LoginViewModel>()
    }
}