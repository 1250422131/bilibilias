package com.imcys.bilibilias.core.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

class AsBackStackViewModel(
    savedStateHandle: SavedStateHandle,
    val asBackStack: AsBackStack,
    serializersModules: SerializersModule,
) : ViewModel() {

    private val config = SavedStateConfiguration { serializersModule = serializersModules }

    @VisibleForTesting
    internal var backStackMap by savedStateHandle.saved(
        serializer = getMapSerializer<AsNavKey>(),
        configuration = config,
    ) {
        linkedMapOf()
    }

    init {
        if (backStackMap.isNotEmpty()) {
            // Restore backstack from saved state handle if not emtpy
            @Suppress("UNCHECKED_CAST")
            asBackStack.restore(
                backStackMap as LinkedHashMap<AsNavKey, MutableList<AsNavKey>>,
            )
        }

        // Start observing changes to the backStack and save backStack whenever it updates
        viewModelScope.launch {
            snapshotFlow {
                asBackStack.backStack.toList()
                backStackMap = asBackStack.backStackMap
            }.collect()
        }
    }
}

private inline fun <reified T : AsNavKey> getMapSerializer() =
    MapSerializer(serializer<T>(), serializer<List<T>>())