package com.imcys.bilibilias.common.base.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun FullScreenScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val isUseDarkModeIcons = shouldUseDarkIcons()
    SideEffect {
        transparentSystemBars(systemUiController, isUseDarkModeIcons)
    }
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        content = content
    )
}

/**
 * 根据 bgColor 亮度判断 systemBar 中 icon 是否使用 暗色模式
 * @param bgColor Color systemBar 背景颜色
 * @return Boolean true 需要使用 暗色模式 Icon ; false 亮色模式
 */
@Composable
fun shouldUseDarkIcons(bgColor: Color = Color.Transparent): Boolean {
    // 透明时使用跟随系统主题
    return if (bgColor == Color.Transparent) {
        !isSystemInDarkTheme()
    } else {
        //颜色亮度
        return bgColor.luminance() >= 0.5
    }
}

fun transparentSystemBars(systemUiController: SystemUiController, useDarkIcons: Boolean) {
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = useDarkIcons,
        isNavigationBarContrastEnforced = false,
    )
}
