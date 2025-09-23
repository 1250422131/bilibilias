package com.imcys.bilibilias.ui.tools.frame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object FrameExtractorRoute : NavKey


@Composable
fun FrameExtractorScreen(
    frameExtractorRoute: FrameExtractorRoute, onToBack: () -> Unit
) {
    val vm = koinViewModel<FrameExtractorViewModel>()
    FrameExtractorScaffold(onToBack = onToBack) { paddingValues ->
        FrameExtractorContent(vm, paddingValues)
    }
}

@Composable
private fun FrameExtractorContent(
    vm: FrameExtractorViewModel,
    paddingValues: PaddingValues
) {
    Column(
        Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FrameExtractorScaffold(
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
                        Text(text = "逐帧提取")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        AsBackIconButton(onClick = {
                            onToBack.invoke()
                        })
                    },
                    actions = {
                        ASIconButton(onClick = {
                        }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                contentDescription = "操作"
                            )
                        }
                    }
                )
            }
        },
    ) {
        content.invoke(it)
    }


}
