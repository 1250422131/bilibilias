package com.imcys.bilibilias.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.ui.graphics.vector.ImageVector
import com.imcys.bilibilias.core.navigation.AsNavKey
import com.imcys.bilibilias.ui.navigation.CacheRoute
import com.imcys.bilibilias.ui.navigation.SearchRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val route: KClass<*>,
    val key: AsNavKey,
) {
    SEARCH(
        selectedIcon = Icons.Filled.Explore,
        unselectedIcon = Icons.Outlined.Explore,
        iconText = "搜索",
        route = SearchRoute::class,
        key = SearchRoute
    ),
    CACHE(
        selectedIcon = Icons.Filled.FileDownload,
        unselectedIcon = Icons.Outlined.FileDownload,
        iconText = "缓存",
        route = CacheRoute::class,
        key = CacheRoute
    ),
}

internal val TopLevelDestinations = TopLevelDestination.entries.associateBy { dest -> dest.key }