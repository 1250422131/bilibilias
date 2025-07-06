package com.imcys.bilibilias.ui.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.imcys.bilibilias.ui.user.UserScreen
import kotlinx.serialization.Serializable

@Serializable
data class UserRoute(val mid: Long = 0)

fun NavController.navigateToUser(
    userRoute: UserRoute = UserRoute(),
    builder: (NavOptionsBuilder.() -> Unit)? = null
) = navigate(userRoute, builder ?: {})


fun NavGraphBuilder.userScreen(
    onToBack: () -> Unit,
) {
    composable<UserRoute> { navBackStackEntry ->
        val userRoute = navBackStackEntry.toRoute<UserRoute>()
        UserScreen(userRoute,onToBack)
    }
}