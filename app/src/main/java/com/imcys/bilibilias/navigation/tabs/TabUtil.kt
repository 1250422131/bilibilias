package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator

abstract class ScreenX: Screen {

    @Composable
    abstract fun Content(modifier: Modifier)

    @Composable
    final override fun Content() = Content(Modifier)
}
