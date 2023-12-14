package com.imcys.space.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.imcys.space.UserSpaceRoute

const val ROUTE_USER_SPACE = "user_spaces"
const val MID = "mid"

fun NavController.navigateToUserSpace(mid: Long) {
    navigate("$ROUTE_USER_SPACE/$mid")
}

/**
 * ```md
 * https://github.com/users/SOCK-MAGIC/projects/1/views/3?pane=issue&itemId=46772592
 * 1.编码格式选择
 * 2.清晰度选择
 * 3.暂不考虑音频下载
 * ```
 */
fun NavGraphBuilder.userSpaceRoute(navigateToCollectionDownload: (String) -> Unit) = composable(
    "$ROUTE_USER_SPACE/{$MID}",
    listOf(
        navArgument(MID) {
            type = NavType.LongType
        }
    )
) {
    UserSpaceRoute(navigateToCollectionDownload)
}
