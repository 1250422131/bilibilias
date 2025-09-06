package com.imcys.bilibilias.navigation

import com.imcys.bilibilias.core.navigation.AsNavKey
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoute : AsNavKey {
    override val isTopLevel: Boolean
        get() = true
}

@Serializable
data object CacheRoute : AsNavKey {
    override val isTopLevel: Boolean
        get() = true
}

@Serializable
data object LoginRoute : AsNavKey {
    override val isTopLevel: Boolean
        get() = false
}

@Serializable
data object SettingRoute : AsNavKey {
    override val isTopLevel: Boolean
        get() = false
}

internal val PolymorphicModuleBuilders = listOf(SearchRoute, CacheRoute, LoginRoute, SettingRoute)