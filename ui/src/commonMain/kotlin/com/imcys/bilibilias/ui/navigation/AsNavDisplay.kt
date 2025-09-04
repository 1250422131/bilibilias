package com.imcys.bilibilias.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator

data object Search : NavKey
data object Cache : NavKey
data object Login : NavKey
data object Player : NavKey
data object Settings : NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NiaNavDisplay(
    niaBackStack: AsBackStack,
    entryProviderBuilders: Set<EntryProviderBuilder<AsNavKey>.() -> Unit>,
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<AsNavKey>()

    NavDisplay(
        backStack = niaBackStack.backStack,
        sceneStrategy = listDetailStrategy,
        onBack = { count -> niaBackStack.popLast(count) },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
//            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entryProviderBuilders.forEach { builder ->
                builder()
            }
        },
    )
}

@Composable
fun Nav() {
    val backStack = rememberNavBackStack(Search, Cache, Login, Player, Settings)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            // Then add the view model store decorator
//            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Search> {}
            entry<Cache> {}
            entry<Login> {}
            entry<Player> {}
            entry<Settings> {}
        }
    )
}