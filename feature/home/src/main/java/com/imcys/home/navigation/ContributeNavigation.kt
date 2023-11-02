package com.imcys.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ROUTE_CONTRIBUTE = "contribute"
fun NavController.navigateToContribute() {
    navigate(ROUTE_CONTRIBUTE)
}

fun NavGraphBuilder.contributeScreen() = composable(ROUTE_CONTRIBUTE) {
    ContributeRoute()
}

@Composable
internal fun ContributeRoute() {
    ContributeScreen()
}

@Composable
internal fun ContributeScreen() {

}
