package com.imcys.bilibilias.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.imcys.bilibilias.ui.home.navigation.HomeRoute

/**
 * BILIBILAIS导航显示组件
 * 计划迁移到Compose Navigation 3.0
 * 暂未使用
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BILIBILAISNavDisplay() {
    val backStack = remember { mutableStateListOf<Any>(HomeRoute()) }
    SharedTransitionLayout {
        NavDisplay(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            transitionSpec = {
                ContentTransform(
                    // 正向导航：新页面进入 - 只是淡入
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    // 正向导航：原页面退出 - 放大并保持可见
                    scaleOut(
                        targetScale = 1.1F,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    )
                )
            },
            popTransitionSpec = {
                ContentTransform(
                    // 返回导航：上一个页面进入 - 从放大状态恢复
                    scaleIn(
                        initialScale = 1.1F,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    // 返回导航：当前页面退出 - 淡出+放大
                    scaleOut(
                        targetScale = 1.1F,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    )
                )
            },
            entryProvider = { key ->
                when (key) {
                    else -> NavEntry(Unit) {
                        Text("Unknown route")
                    }
                }
            }
        )
    }

}