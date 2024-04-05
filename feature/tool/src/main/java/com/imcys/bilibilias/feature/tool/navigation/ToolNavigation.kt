package com.imcys.bilibilias.feature.tool.navigation

import androidx.compose.runtime.Composable
import com.imcys.bilibilias.feature.tool.ToolRoute
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun NavigationToTool() {
    ToolRoute()
}

@Composable
fun ToolFragmentScreen() {
    ToolRoute()
}