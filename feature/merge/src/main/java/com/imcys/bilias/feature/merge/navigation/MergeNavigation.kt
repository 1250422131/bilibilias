package com.imcys.bilias.feature.merge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilias.feature.merge.MergeRoute

const val ROUTE_MERGE = "merge"
fun NavController.navigateToMerge() {
    navigate(ROUTE_MERGE)
}

fun NavGraphBuilder.mergeRoute() = composable(ROUTE_MERGE) {
    MergeRoute()
}
