package com.imcys.bilibilias.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(onSalute: () -> Unit, onDonation: () -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.homeUiState.collectAsState()
    HomeScreen(
        onSalute,
        onDonation,
        viewModel::exitAccountLogin,
        uiState,
    )
}

@Composable
internal fun HomeScreen(
    onSalute: () -> Unit,
    onDonation: () -> Unit,
    exitLogin: () -> Unit,
    uiState: HomeUiState,
) {
    when (uiState) {
        HomeUiState.Empty,
        HomeUiState.Loading -> Unit

        is HomeUiState.Success -> HomeContent(
            onSalute,
            onDonation,
            exitLogin,
            uiState.homeBanner,
            uiState.updateNotice,
        )
    }
}
