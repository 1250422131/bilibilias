package com.imcys.bilibilias.ui.navigation

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