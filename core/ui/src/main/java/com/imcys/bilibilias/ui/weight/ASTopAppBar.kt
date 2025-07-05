package com.imcys.bilibilias.ui.weight

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class BILIBILIASTopAppBarStyle {
    Small, Large, CenterAligned
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ASTopAppBar(
    style: BILIBILIASTopAppBarStyle = BILIBILIASTopAppBarStyle.Small,
    title: @Composable () -> Unit,
    contentPadding: @Composable () -> PaddingValues = { PaddingValues() },
    navigationIcon: @Composable () -> Unit = { },
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {

    val mColors = colors ?: when (style) {
        BILIBILIASTopAppBarStyle.Small -> TopAppBarDefaults.topAppBarColors()
        BILIBILIASTopAppBarStyle.Large -> TopAppBarDefaults.topAppBarColors()
        BILIBILIASTopAppBarStyle.CenterAligned -> TopAppBarDefaults.topAppBarColors()
    }

    val topBarModifier = Modifier.padding(contentPadding())
    when (style) {
        BILIBILIASTopAppBarStyle.Small -> {
            TopAppBar(
                title = title,
                modifier = topBarModifier,
                navigationIcon = navigationIcon,
                actions = actions,
                windowInsets = windowInsets,
                colors = mColors,
                scrollBehavior = scrollBehavior
            )
        }

        BILIBILIASTopAppBarStyle.Large -> {
            LargeTopAppBar(
                modifier = topBarModifier,
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                windowInsets = windowInsets,
                colors = mColors,
                scrollBehavior = scrollBehavior
            )
        }

        BILIBILIASTopAppBarStyle.CenterAligned -> {
            CenterAlignedTopAppBar(
                modifier = topBarModifier,
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
                windowInsets = windowInsets,
                colors = mColors,
                scrollBehavior = scrollBehavior
            )
        }
    }
}