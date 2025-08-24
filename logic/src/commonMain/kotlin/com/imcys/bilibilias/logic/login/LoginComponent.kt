package com.imcys.bilibilias.logic.login

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.model.SelfInfo
import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.logic.root.AppComponentContext
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

interface LoginComponent {
    val cookieStateMachine: CookieStateMachine
    val qrCodeStateMachine: QrCodeLoginStateMachine
}

class DefaultLoginComponent(
    componentContext: AppComponentContext,
    override val cookieStateMachine: CookieStateMachine,
    override val qrCodeStateMachine: QrCodeLoginStateMachine,
    private val preferences: AsPreferencesDataSource,
) : LoginComponent, AppComponentContext by componentContext {
    init {
        lifecycle.doOnDestroy {
            applicationScope.launch {
                val data = BilibiliApi.getNavigationData()
                preferences.setSelfInfo(
                    SelfInfo(
                        Uuid.random(),
                        data.mid,
                        data.uname,
                        data.face
                    )
                )
            }
        }
    }

    companion object {
        private val logger = logger<LoginComponent>()
    }
}