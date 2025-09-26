package com.imcys.bilibilias.ui.home.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data class HomeRoute(
    var isFormLogin: Boolean = false
): NavKey