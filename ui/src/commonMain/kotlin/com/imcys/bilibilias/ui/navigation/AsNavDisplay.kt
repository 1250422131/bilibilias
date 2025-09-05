package com.imcys.bilibilias.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.imcys.bilibilias.core.navigation.AsBackStack
import com.imcys.bilibilias.core.navigation.AsNavKey
import com.imcys.bilibilias.core.navigation.rememberNavBackStack

data object Search : NavKey
data object Cache : NavKey
data object Login : NavKey
data object Player : NavKey
data object Settings : NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AsNavDisplay(
    asBackStack: AsBackStack,
    entryProviderBuilders: Set<EntryProviderBuilder<AsNavKey>.() -> Unit>,
    modifier: Modifier = Modifier,
) {
//    val listDetailStrategy = rememberListDetailSceneStrategy<AsNavKey>()

    NavDisplay(
        backStack = asBackStack.backStack,
//        sceneStrategy = listDetailStrategy,
        onBack = { count -> asBackStack.popLast(count) },
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
        modifier = modifier,
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