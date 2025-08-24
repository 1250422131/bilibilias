package com.imcys.bilibilias.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
) {
    SEARCH(
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        iconText = "搜索",
    ),
    CACHE(
        selectedIcon = Icons.Filled.FileDownload,
        unselectedIcon = Icons.Outlined.FileDownload,
        iconText = "缓存",
    ),
}