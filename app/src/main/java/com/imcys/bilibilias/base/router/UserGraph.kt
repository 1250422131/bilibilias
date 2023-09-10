package com.imcys.bilibilias.base.router

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.imcys.bilibilias.ui.user.Collection
import com.imcys.bilibilias.ui.user.User
import com.imcys.bilibilias.ui.user.UserViewModel
import timber.log.Timber

fun NavGraphBuilder.userNavHostGraph(userViewModel: UserViewModel) {
    navigation(Screen.User.route, route = "userGraph") {
        composable(Screen.User.route) { User(userViewModel) }
        composable(RouterConstants.Collection) { Collection(userViewModel) }
        composable("playHistory") { }
        composable("bangumiFollowr") { }
    }
}
