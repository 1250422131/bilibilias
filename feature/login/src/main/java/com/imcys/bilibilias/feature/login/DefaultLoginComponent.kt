package com.imcys.bilibilias.feature.login

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.network.repository.LoginRepository
import com.imcys.bilibilias.feature.common.AsComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultLoginComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val loginRepository: LoginRepository,
    private val loginInfoDataSource: LoginInfoDataSource,
) : LoginComponent, AsComponentContext<LoginEvent, LoginModel>(componentContext) {

    @Composable
    override fun models(events: Flow<LoginEvent>): LoginModel {
        return LoginPresenter(events, loginRepository, loginInfoDataSource)
    }

    @AssistedFactory
    interface Factory : LoginComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultLoginComponent
    }
}
