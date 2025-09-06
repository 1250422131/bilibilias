package com.imcys.bilibilias.navigation

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
@Composable
fun AsNavDisplay(
    asBackStack: AsBackStack,
    entryProviderBuilders: EntryProviderBuilder<AsNavKey>.() -> Unit,
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
            entryProviderBuilders()
        },
        modifier = modifier,
    )
}