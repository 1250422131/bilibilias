package com.imcys.bilibilias.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.AsBottomBar
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.components.FullScreenScaffold
import com.imcys.bilibilias.home.ui.viewmodel.FragmentHomeViewModel

@Composable
fun HomeScreen(
    onNavigateToTool: () -> Unit,
    onNavigateToDownload: () -> Unit,
    onNavigateToUser: () -> Unit
) {
    val homeViewModel: FragmentHomeViewModel = hiltViewModel()
    FullScreenScaffold(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        topBar = {
            Text(
                stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Banner()
            UpdateContent()
            Salute()
            // todo 捐款有特殊处理
            Donate()
            Feedback()
            Logout()
        }
    }
}
