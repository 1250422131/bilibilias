package com.imcys.bilibilias.ui.user.navigation

import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.user.UserScreen
import kotlinx.serialization.Serializable

@Serializable
data class UserRoute(
    val mid: Long = 0,
    val isAnalysisUser: Boolean = false
) : NavKey
