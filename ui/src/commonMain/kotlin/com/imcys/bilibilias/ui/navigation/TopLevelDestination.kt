package com.imcys.bilibilias.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cached
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val iconText: String,
) {
    SEARCH(
        selectedIcon = Icons.Rounded.Search,
        iconText = "搜索",
    ),
    CACHE(
        selectedIcon = Icons.Rounded.Cached,
        iconText = "缓存",
    ),
}