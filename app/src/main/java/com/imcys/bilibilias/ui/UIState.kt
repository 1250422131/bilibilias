package com.imcys.bilibilias.ui

import com.imcys.bilibilias.database.entity.BILIUsersEntity

sealed class UIState {
    data object Default : UIState()
    data class AccountCheck(
        val isCheckLoading: Boolean = false,
        val newCurrentUser: BILIUsersEntity? = null
    ) : UIState() // 正在检测

    data object KnowAboutApp : UIState()
}