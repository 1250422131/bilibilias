package com.imcys.bilibilias.ui.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.user.UserScreen
import kotlinx.serialization.Serializable

@Serializable
data class UserRoute(
    val mid: Long = 0,
    val isAnalysisUser: Boolean = false
): NavKey

fun NavController.navigateToUser(
    userRoute: UserRoute = UserRoute(),
    builder: (NavOptionsBuilder.() -> Unit)? = null
) = navigate(userRoute, builder ?: {})


fun NavGraphBuilder.userScreen(
    onToBack: () -> Unit,
    onToSettings: () -> Unit
) {
    composable<UserRoute> { navBackStackEntry ->
        val userRoute = navBackStackEntry.toRoute<UserRoute>()
        UserScreen(userRoute, onToBack, onToSettings)
    }
}