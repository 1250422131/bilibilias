package com.imcys.bilibilias.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
) {
    SEARCH(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconText = "搜索",
    ),
    CACHE(
        selectedIcon = Icons.Filled.FileDownload,
        unselectedIcon = Icons.Outlined.FileDownload,
        iconText = "缓存",
    ),
}