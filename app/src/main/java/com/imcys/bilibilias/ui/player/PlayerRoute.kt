package com.imcys.bilibilias.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ROUTE_PLAYER = "player"
fun NavController.navigateToPlayer() {
    navigate(ROUTE_PLAYER)
}

fun NavGraphBuilder.playerRoute(onBack: () -> Unit) = composable(ROUTE_PLAYER) {
    PlayerRoute(onBack)
}

@Composable
fun PlayerRoute(onBack: () -> Unit) {
    val viewModel: PlayerViewModel = hiltViewModel()
    val state by viewModel.videoAllDetailsState.collectAsStateWithLifecycle()
    PlayerScreen(state)
}
