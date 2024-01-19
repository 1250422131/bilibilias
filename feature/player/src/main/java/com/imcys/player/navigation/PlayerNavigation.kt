package com.imcys.player.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.imcys.common.utils.ParseInputType
import com.imcys.player.PlayerRoute

const val PLAYER_ID = "playerId"
const val ROUTE_PLAYER = "player"
const val BV_ID = "bvid"
const val A_ID = "aid"
const val C_ID = "cid"
const val PARSE_TYPE = "parse_type"
fun NavController.navigateToPlayer(aid: String, bvid: String, cid: String) {
    navigate("$ROUTE_PLAYER/$aid/$bvid/$cid") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.playerScreen(
    navigateToDownloadAanmaku: () -> Unit,
    navigateToUserSpace: (Long) -> Unit,
) = composable(
    "$ROUTE_PLAYER/{$A_ID}/{$BV_ID}/{$C_ID}/{$PARSE_TYPE}",
    arguments = listOf(
        navArgument(A_ID) {
            type = NavType.StringType
        },
        navArgument(BV_ID) {
            type = NavType.StringType
        },
        navArgument(C_ID) {
            type = NavType.StringType
        },
        navArgument(PARSE_TYPE) {
            type = NavType.EnumType(ParseInputType::class.java)
        }
    )
) {
    PlayerRoute(
        navigateToDownloadAanmaku,
        navigateToUserSpace
    )
}
