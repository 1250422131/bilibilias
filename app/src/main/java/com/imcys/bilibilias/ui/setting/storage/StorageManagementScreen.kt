package com.imcys.bilibilias.ui.setting.storage

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import com.imcys.bilibilias.ui.utils.rememberWidthSizeClass
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.AnimatedStorageRing
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Serializable
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
        StorageManagementContent(Modifier.padding(it), onToDownloadList)
    }
}

@Composable
fun StorageManagementContent(
    modifier: Modifier = Modifier,
    onToDownloadList: () -> Unit
) {
    val context = LocalContext.current
    val vm = koinViewModel<StorageManagementViewModel>()
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadStorageInfo(context)
    }


    when (val state = uiState) {
        is StorageManagementViewModel.StorageManagementUIState.Error -> {}
        StorageManagementViewModel.StorageManagementUIState.Loading -> {
            StorageManagementLoadingScreen()
        }

        is StorageManagementViewModel.StorageManagementUIState.Success -> {
            StorageManagementSuccessScreen(
                modifier,
                state.storageInfoData,
                state.hasDownloadSAFPermission,
                onCleanCache = {
                    vm.cleanAppCache(context)
                },
                onToDownloadList = onToDownloadList,
                onSaveDownloadUri = {
                    vm.saveDownloadUri(context, it)
                }
            )
        }
    }
}

@Composable
fun StorageManagementSuccessScreen(
    modifier: Modifier = Modifier,
    data: StorageInfoData,
    hasDownloadSAFPermission: Boolean,
    onCleanCache: () -> Unit,
    onToDownloadList: () -> Unit,
    onSaveDownloadUri: (uri: Uri) -> Unit,
) {
    val windowWidthSizeClass = rememberWidthSizeClass()
    val context = LocalContext.current
    val downloadLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { uri ->
            if (uri != null) {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    onSaveDownloadUri(uri)
                } catch (_: Exception) {
                }

            }
        }

    Column(
        modifier
            .verticalScroll(rememberScrollState())
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
                    .fillMaxWidth(
                        if (windowWidthSizeClass == WindowWidthSizeClass.Compact) 0.6f else 0.4f
                    )
                    .aspectRatio(1f)
            )

        }

        if (!hasDownloadSAFPermission) {
            ASWarringTip {
                Row {
                    Text(
                        "应用存储权限未完全获取，可能导致存储数据不准确，点击授权后重新计算。",
                        Modifier.weight(1f)
                    )
                    ASIconButton(onClick = {
                        val downloadsDir =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        val targetDir = File(downloadsDir, "BILIBILIAS")
                        if (!targetDir.exists()) {
                            runCatching { targetDir.mkdirs() }
                        }
                        val relativePath =
                            targetDir.absolutePath.substringAfter("/storage/emulated/0/")
                        val downloadUri = DocumentsContract.buildDocumentUri(
                            "com.android.externalstorage.documents",
                            "primary:$relativePath"
                        )
                        downloadLauncher.launch(downloadUri)
                    }) {
                        Icon(Icons.Outlined.NorthEast, contentDescription = "去授权")
                    }
                }
            }
        }

        StorageContent(
            title = "音视频文件",
            dataNumStr = StorageUtil.formatSize(data.downloadBytes),
            description = "已下载的音视频文件大小",
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    val targetDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "BILIBILIAS")
                        .apply {
                        if (!exists()) mkdirs()
                    }
                    val relativePath =
                        targetDir.absolutePath.substringAfter("/storage/emulated/0/")
                    val downloadUri = DocumentsContract.buildDocumentUri(
                        "com.android.externalstorage.documents",
                        "primary:$relativePath"
                    )
                    setDataAndType(
                        downloadUri,
                        "vnd.android.document/directory"
                    )
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "未找到文件管理器", Toast.LENGTH_SHORT).show()
                }
            },
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
                    alwaysDisplay = false
                )
            }
        },
    ) {
        content.invoke(it)
    }


}
