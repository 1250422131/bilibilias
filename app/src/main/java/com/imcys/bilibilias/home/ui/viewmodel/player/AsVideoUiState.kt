package com.imcys.bilibilias.home.ui.viewmodel.player

import com.imcys.bilibilias.core.model.user.Card
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewTriple

data class AsVideoUiState(
    val viewDetail: ViewDetail = ViewDetail(),
    val userCard: Card = Card()
)

sealed interface ViewUiState {
    data object Loading : ViewUiState
    data class Success(val viewDetail: ViewDetail, val userCard: Card, val viewTriple: ViewTriple) :
        ViewUiState
}
