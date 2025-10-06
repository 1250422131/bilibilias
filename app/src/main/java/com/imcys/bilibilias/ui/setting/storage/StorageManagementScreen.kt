package com.imcys.bilibilias.ui.setting.storage

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.common.utils.StorageInfoData
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.AnimatedStorageRing
import org.koin.androidx.compose.koinViewModel

data object StorageManagementRoute : NavKey

@Composable
fun StorageManagementScreen(
    route: StorageManagementRoute,
    onToBack: () -> Unit
) {
    StorageManagementScaffold(
        onToBack = onToBack,
    ) {
        StorageManagementContent(it)
    }
}

@Composable
fun StorageManagementContent(paddingValues: PaddingValues) {
    val content = LocalContext.current
    val vm = koinViewModel<StorageManagementViewModel>()
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadStorageInfo(content)
    }


    AnimatedContent(targetState = uiState) {
        when (it) {
            is StorageManagementViewModel.StorageManagementUIState.Error -> {}
            StorageManagementViewModel.StorageManagementUIState.Loading -> {
                StorageManagementLoadingScreen()
            }

            is StorageManagementViewModel.StorageManagementUIState.Success -> {
                StorageManagementSuccessScreen(paddingValues, it.storageInfoData)
            }
        }
    }
}

@Composable
fun StorageManagementSuccessScreen(paddingValues: PaddingValues, data: StorageInfoData) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(vertical = 10.dp)
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedStorageRing(data, modifier = Modifier.fillMaxWidth(0.6f).aspectRatio(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun StorageManagementLoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 加载中
        CircularWavyProgressIndicator()
        Text(text = "加载中...")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StorageManagementScaffold(
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text(text = "存储管理")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        AsBackIconButton(onClick = {
                            onToBack.invoke()
                        })
                    },
                )
            }
        },
    ) {
        content.invoke(it)
    }


}
