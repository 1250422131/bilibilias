package com.imcys.bilibilias.weight

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.imcys.bilibilias.ui.utils.rememberWidthSizeClass

@Composable
fun ASAnimatedContent(
    modifier: Modifier = Modifier,
    compactContent: @Composable () -> Unit,
    mediumContent: @Composable () -> Unit = compactContent,
    expandedContent: @Composable () -> Unit = mediumContent
) {
    val windowWidthSize = rememberWidthSizeClass()
    AnimatedContent(
        targetState = windowWidthSize,
        modifier = modifier
    ) { size ->
        when (size) {
            WindowWidthSizeClass.Compact -> compactContent()
            WindowWidthSizeClass.Medium -> mediumContent()
            WindowWidthSizeClass.Expanded -> expandedContent()
            else -> compactContent()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Modifier.maybeNestedScroll(
    scrollBehavior: TopAppBarScrollBehavior?,
    alwaysDisplay: Boolean = false
): Modifier {
    val windowWidthSizeClass = rememberWidthSizeClass()
    val isTopBarVisible = windowWidthSizeClass == WindowWidthSizeClass.Compact || alwaysDisplay
    return if (isTopBarVisible && scrollBehavior != null) {
        nestedScroll(scrollBehavior.nestedScrollConnection)
    } else { this }
}