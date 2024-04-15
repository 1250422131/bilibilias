package com.imcys.bilibilias.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(modifier: Modifier, onSalute: () -> Unit, onDonation: () -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.homeUiState.collectAsState()
    HomeScreen(
        onSalute,
        onDonation,
        viewModel::exitAccountLogin,
        uiState,
        modifier
    )
}

@Composable
internal fun HomeScreen(
    onSalute: () -> Unit,
    onDonation: () -> Unit,
    exitLogin: () -> Unit,
    uiState: HomeUiState,
    modifier: Modifier
) {
    when (uiState) {
        HomeUiState.Empty,
        HomeUiState.Loading -> Unit

        is HomeUiState.Success -> HomeContent(
            onSalute,
            onDonation,
            exitLogin,
            uiState.banner,
            uiState.updateNotice,
            modifier,
        )
    }
}
