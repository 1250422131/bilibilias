package com.imcys.bilibilias.feature.user

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.network.repository.UserRepository
import com.imcys.bilibilias.feature.common.AsComponentContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginInfoDataSource: LoginInfoDataSource,
    componentContext: ComponentContext,
) : AsComponentContext<Event, Model>(componentContext) {
    @Composable
    override fun models(events: Flow<Event>): Model {
        return UserPresenter(events = events, userRepository, loginInfoDataSource)
    }
}
