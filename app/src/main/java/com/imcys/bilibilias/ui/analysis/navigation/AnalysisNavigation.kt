package com.imcys.bilibilias.ui.analysis.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.imcys.bilibilias.ui.analysis.AnalysisScreen
import kotlinx.serialization.Serializable

@Serializable
data class AnalysisRoute(
    var asInputText: String = ""
)


fun NavController.navigateToAnalysis(
    analysisRoute: AnalysisRoute = AnalysisRoute(),
    builder: (NavOptionsBuilder.() -> Unit)? = null
) = navigate(analysisRoute, builder ?: {})


@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.analysisScreen(
    sharedTransitionScope: SharedTransitionScope,
    onToBack: () -> Unit,
    goToUser: (mid: Long) -> Unit, ) {
    composable<AnalysisRoute> { navBackStackEntry ->
        // 从 navBackStackEntry 获取参数
        val route = navBackStackEntry.toRoute<AnalysisRoute>()
        AnalysisScreen(route, sharedTransitionScope, this@composable, onToBack, goToUser)
    }
}