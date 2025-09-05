package com.imcys.bilibilias.navigation

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.imcys.bilibilias.core.navigation.AsBackStack
import com.imcys.bilibilias.core.navigation.AsNavKey
import com.imcys.bilibilias.ui.navigation.SearchRoute
import com.imcys.bilibilias.ui.search.SearchScreen
import org.koin.dsl.module

val AsEntryProviderModule = module {
    single {
        AsBackStack(TopLevelDestination.SEARCH.key)
    }
    single {
        fun provideSearchEntryProviderBuilder(
            backStack: AsBackStack,
        ): EntryProviderBuilder<AsNavKey>.() -> Unit = {
            entry<SearchRoute> { key ->
                SearchScreen(
                    navigationToLogin = TODO(),
                    navigationToPlayer = TODO(),
                    navigationToSettings = TODO(),
                    searchViewModel = TODO()
                )
            }
        }
    }
}