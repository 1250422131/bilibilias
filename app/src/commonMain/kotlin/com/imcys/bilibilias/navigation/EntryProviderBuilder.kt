package com.imcys.bilibilias.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.imcys.bilibilias.core.navigation.AsNavKey
import com.imcys.bilibilias.ui.cache.CacheScreen
import com.imcys.bilibilias.ui.search.SearchScreen

val entryProviderBuilders: EntryProviderBuilder<AsNavKey>.() -> Unit = {
    entry<SearchRoute> {
        SearchScreen(
            navigationToLogin = {},
            navigationToPlayer = {},
            navigationToSettings = { }
        )
    }
    entry<CacheRoute> {
        CacheScreen()
    }
}

