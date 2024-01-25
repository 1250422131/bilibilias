package com.imcys.player.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.imcys.player.PlayerRoute

const val ROUTE_PLAYER = "player"
const val BV_ID = "bvId"
const val A_ID = "aId"
const val C_ID = "cId"
const val EP_ID = "epId"
fun NavController.navigateToPlayer(aid: String, bvid: String, cid: String, epId: String) {
    navigate("$ROUTE_PLAYER/$aid/$bvid/$cid/$epId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.playerScreen(
    navigateToUserSpace: (Long) -> Unit,
) = composable(
    "$ROUTE_PLAYER/{$A_ID}/{$BV_ID}/{$C_ID}/?$EP_ID={$EP_ID}",
    arguments = listOf(
        navArgument(A_ID) {
            type = NavType.StringType
            defaultValue = ""
        },
        navArgument(BV_ID) {
            type = NavType.StringType
            defaultValue = ""
        },
        navArgument(C_ID) {
            type = NavType.StringType
            defaultValue = ""
        },
        navArgument(EP_ID) {
            type = NavType.StringType
            defaultValue = "没有值"
        }
    )
) {
    PlayerRoute(navigateToUserSpace)
}
