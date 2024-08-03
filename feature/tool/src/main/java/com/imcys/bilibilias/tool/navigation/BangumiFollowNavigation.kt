package com.imcys.bilibilias.tool.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.tool.bangumiFollow.BangumiFollowHeaders
import com.imcys.bilibilias.tool.bangumiFollow.BangumiFollowScreen
import com.imcys.bilibilias.tool.bangumiFollow.BangumiFollowViewModel

const val ROUTE_BANGUMI_FOLLOW = "bangumi_follow"
fun NavController.navigateToBangumiFollow() {
    navigate(ROUTE_BANGUMI_FOLLOW)
}

fun NavGraphBuilder.bangumiFollowRoute() = composable(ROUTE_BANGUMI_FOLLOW) {
    val viewModel: BangumiFollowViewModel = hiltViewModel()
    BangumiFollowRoute(
        viewModel.selectableHeaders,
        viewModel.selectedHeaders,
        viewModel::removeSelected,
        viewModel::addToSelected,
        viewModel::submit
    )
}

@Composable
fun BangumiFollowRoute(
    selectableHeaders: List<Pair<String, BangumiFollowHeaders>>,
    selectedHeaders: List<Pair<String, BangumiFollowHeaders>>,
    removeSelected: (Pair<String, BangumiFollowHeaders>) -> Unit,
    addToSelected: (Pair<String, BangumiFollowHeaders>) -> Unit,
    submit: (Context) -> Unit
) {
    BangumiFollowScreen(selectableHeaders, selectedHeaders, removeSelected, addToSelected, submit)
}
