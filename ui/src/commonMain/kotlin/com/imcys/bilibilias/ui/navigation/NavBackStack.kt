package com.imcys.bilibilias.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration

@Composable
inline fun <reified T : NavKey> rememberNavBackStack(
    vararg elements: T,
    configuration: SavedStateConfiguration = SavedStateConfiguration.DEFAULT,
): NavBackStack {
    return rememberSerializable(
        configuration = configuration,
        serializer = NavBackStackSerializer<NavKey>(configuration = configuration),
    ) {
        elements.toList().toMutableStateList()
    }
}

/** A List of objects that extend the [NavKey] marker class. */
typealias NavBackStack = SnapshotStateList<NavKey>