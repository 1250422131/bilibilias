package com.imcys.bilibilias.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.imcys.bilibilias.core.navigation.AsBackStack
import com.imcys.bilibilias.core.navigation.AsNavKey

@Suppress("VisibleForTests")
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