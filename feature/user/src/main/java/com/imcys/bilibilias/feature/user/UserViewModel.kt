package com.imcys.bilibilias.feature.user

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.common.molecule.MoleculeViewModel
import com.imcys.bilibilias.core.datastore.LoginInfoDataSource
import com.imcys.bilibilias.core.network.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginInfoDataSource: LoginInfoDataSource,
) : MoleculeViewModel<Event, Model>() {
    @Composable
    override fun models(events: Flow<Event>): Model {
        return UserPresenter(events = events, userRepository, loginInfoDataSource)
    }
}
