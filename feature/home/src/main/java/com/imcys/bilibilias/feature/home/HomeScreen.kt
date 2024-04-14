package com.imcys.bilibilias.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

@Composable
fun HomeRoute(onSalute: () -> Unit, onDonation: () -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.homeUiState.collectAsState()
    HomeScreen(
        onSalute,
        onDonation,
        viewModel::postSignatureMessage,
        viewModel::exitAccountLogin,
        uiState
    )
}

@Composable
internal fun HomeScreen(
    onSalute: () -> Unit,
    onDonation: () -> Unit,
    postSignatureMessage: (String, Pair<String, Long>, String) -> Unit,
    exitLogin: () -> Unit,
    uiState: HomeUiState,
) {
    when (uiState) {
        HomeUiState.Empty,
        HomeUiState.Loading -> Unit

        is HomeUiState.Success -> HomeContent(
            onSalute,
            onDonation,
            postSignatureMessage,
            exitLogin,
            uiState.banner,
            uiState.updateNotice,
        )
    }
}
