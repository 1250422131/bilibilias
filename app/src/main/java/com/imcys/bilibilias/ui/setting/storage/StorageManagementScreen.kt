package com.imcys.bilibilias.ui.setting.storage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.common.utils.StorageInfoData
import com.imcys.bilibilias.common.utils.StorageUtil
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.weight.AnimatedStorageRing
import org.koin.androidx.compose.koinViewModel

data object StorageManagementRoute : NavKey

@Composable
fun StorageManagementScreen(
    route: StorageManagementRoute,
    onToBack: () -> Unit,
    onToDownloadList: () -> Unit,
) {
    StorageManagementScaffold(
        onToBack = onToBack,
    ) {
        StorageManagementContent(it, onToDownloadList)
    }
}

@Composable
fun StorageManagementContent(paddingValues: PaddingValues, onToDownloadList: () -> Unit) {
    val content = LocalContext.current
    val vm = koinViewModel<StorageManagementViewModel>()
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadStorageInfo(content)
    }


    when (val state = uiState) {
        is StorageManagementViewModel.StorageManagementUIState.Error -> {}
        StorageManagementViewModel.StorageManagementUIState.Loading -> {
            StorageManagementLoadingScreen()
        }

        is StorageManagementViewModel.StorageManagementUIState.Success -> {
            StorageManagementSuccessScreen(
                paddingValues, state.storageInfoData,
                onCleanCache = {
                    vm.cleanAppCache(content)
                },
                onToDownloadList
            )
        }
    }
}

@Composable
fun StorageManagementSuccessScreen(
    paddingValues: PaddingValues,
    data: StorageInfoData,
    onCleanCache: () -> Unit,
    onToDownloadList: () -> Unit
) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(vertical = 10.dp)
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedStorageRing(
                data, modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f)
            )

        }

        StorageContent(
            title = "缓存视频",
            dataNumStr = "${StorageUtil.formatSize(data.downloadBytes)}",
            description = "已下载的视频文件大小",
            onClick = onToDownloadList,
        )


        StorageContent(
            title = "临时文件",
            dataNumStr = "${StorageUtil.formatSize(data.cacheTotalBytes)}",
            description = "临时文件，可放心清理",
            buttonText = "清理",
            buttonColor = MaterialTheme.colorScheme.primary,
            onClick = onCleanCache
        )

        StorageContent(
            title = "核心文件",
            dataNumStr = "${StorageUtil.formatSize(data.appBytes - data.cacheTotalBytes)}",
            description = "运行时必要文件，不可清除。",
            showButton = false,
            buttonColor = MaterialTheme.colorScheme.primary,
        )


    }
}

@Preview
@Composable
fun StorageContent(
    title: String = "",
    dataNumStr: String = "",
    description: String = "",
    showButton: Boolean = true,
    buttonText: String = "管理",
    buttonColor: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit = {}
) {
    Surface(
        Modifier
            .fillMaxWidth(),
        shape = CardDefaults.shape
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    dataNumStr,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    description,
                    fontSize = 11.sp,
                )
            }
            if (showButton) {
                Surface(
                    onClick = onClick,
                    shape = RoundedCornerShape(6.dp),
                    border = if (buttonColor == MaterialTheme.colorScheme.surface)
                        CardDefaults.outlinedCardBorder() else null,
                    modifier = Modifier.padding(0.dp),
                    color = buttonColor
                ) {
                    Text(
                        buttonText,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }


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
