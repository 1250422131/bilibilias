package com.imcys.bilibilias.ui

import androidx.compose.runtime.Stable
import com.imcys.bilibilias.navigation.TopLevelDestination

@Stable
class AsAppState {
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries
}