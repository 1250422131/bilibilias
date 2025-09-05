package com.imcys.bilibilias.ui.navigation

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