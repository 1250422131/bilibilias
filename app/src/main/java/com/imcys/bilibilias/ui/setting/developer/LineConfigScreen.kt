package com.imcys.bilibilias.ui.setting.developer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.utils.rememberWidthSizeClass
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTextButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import com.imcys.bilibilias.ui.weight.TipSettingsItem
import com.imcys.bilibilias.ui.weight.shimmer.shimmer
import com.imcys.bilibilias.ui.weight.tip.ASInfoTip
import com.imcys.bilibilias.ui.weight.tip.ASWarringTip
import com.imcys.bilibilias.weight.maybeNestedScroll
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
data object LineConfigRoute : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineConfigScreen(lineConfigRoute: LineConfigRoute, onToBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LineConfigScaffold(scrollBehavior = scrollBehavior, onToBack = onToBack) { paddingValues ->
        LineConfigContent(
            modifier = Modifier
                .padding(paddingValues)
                .maybeNestedScroll(scrollBehavior)
        )
    }
}

@Composable
fun LineConfigContent(modifier: Modifier) {
    val vm = koinViewModel<LineConfigViewModel>()
    val uiState by vm.uiState.collectAsState()
    val biliLineHostListState by vm.biliLineHostListState.collectAsState()
    val widthSizeClass = rememberWidthSizeClass()

    LaunchedEffect(Unit) {
        vm.loadLineConfig()
    }

    val columns = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1
        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> 2
        else -> 1
    }
    LazyVerticalGrid(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(columns)
    ) {

        item(
            span = { GridItemSpan(columns) }
        ) {
            ASInfoTip {
                Text(
                    """
                线路配置可在部分情况下重定向缓存资源的地址路径，从而加快缓存，但并不是任何时候都有用。
            """.trimIndent()
                )
            }
        }


        item(span = { GridItemSpan(columns) }) {
            AnimatedVisibility(visible = uiState.currentLineHost.isNotEmpty()) {
                ASWarringTip(
                    modifier = Modifier.animateItem()
                ) {
                    Text(
                        """
                            如果你发现使用非自动线路后出现视频无法播放等问题，请切换回自动线路，这可能是内置的线路已经被废弃。
                        """.trimIndent()
                    )
                }
            }
        }

        item(span = { GridItemSpan(columns) }) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ASIconButton(onClick = {
                    vm.startSpeedTest()
                }) {
                    Icon(Icons.Outlined.Speed, contentDescription = "图标")
                }
            }
        }

        biliLineHostListState.forEach {
            item {
                LineHostCard(uiState, it, vm)
            }
        }

        item {
            TipSettingsItem(
                modifier =  Modifier.animateItem(),
                text =
                """
                    这些加速由B站支持的CDN提供，全程不经过BILIBILIAS服务器代理。
                """.trimIndent()
            )
        }
    }
}

@Composable
private fun LineHostCard(
    uiState: LineConfigUIState,
    item: BILILineHostItem,
    vm: LineConfigViewModel
) {
    val colorState by animateColorAsState(
        targetValue = if (uiState.currentLineHost == item.host)
            MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface
    )
    Surface(
        shape = CardDefaults.shape,
        // 选中颜色
        color = colorState,
        onClick = {
            vm.updateLineHost(item.host)
        }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.name, modifier = Modifier.weight(1f))

            ASTextButton(
                enabled = !item.checkSpeeding,
                modifier = Modifier
                    .shimmer(item.checkSpeeding)
                    .then(if (item.host.isNotEmpty()) Modifier else Modifier.alpha(0f)),
                onClick = {
                    vm.startSpeedTest(item)
                }) {
                if (item.speed != null) {
                    Text("${item.speed}")
                } else {
                    Text("检测")
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LineConfigScaffold(
    scrollBehavior: TopAppBarScrollBehavior,
    onToBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            ASTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                scrollBehavior = scrollBehavior,
                style = BILIBILIASTopAppBarStyle.Large,
                title = {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text("Beta")
                            }
                        }
                    ) {
                        Text(text = "线路配置")
                    }
                },
                navigationIcon = {
                    AsBackIconButton(onClick = {
                        onToBack.invoke()
                    })
                },
                alwaysDisplay = false
            )
        },
    ) {
        content(it)
    }

}