package com.imcys.bilibilias.feature.splash

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.datastore.UsersDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val usersDataSource: UsersDataSource,
) : ComponentContext by componentContext {
    val isLogin: Boolean = runBlocking { usersDataSource.users.first().isLogined }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SplashComponent
    }
}
