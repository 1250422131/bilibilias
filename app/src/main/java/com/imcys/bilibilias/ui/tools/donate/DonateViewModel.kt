package com.imcys.bilibilias.ui.tools.donate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.network.model.app.AppOldDonateBean
import com.imcys.bilibilias.network.service.AppAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DonateViewModel(
    appAPIService: AppAPIService
) : ViewModel() {

    data class UIState(
        val oldDonateInfo: AppOldDonateBean? = null
    )

    val uiState: StateFlow<UIState>
        field = MutableStateFlow(UIState())


    init {
        viewModelScope.launch(Dispatchers.IO) {
            appAPIService.getAppOldDonate().onSuccess {
                uiState.value = uiState.value.copy(
                    oldDonateInfo = it
                )
            }
        }
    }


}