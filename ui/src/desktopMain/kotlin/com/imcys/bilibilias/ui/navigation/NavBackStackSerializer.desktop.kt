package com.imcys.bilibilias.ui.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

@Suppress("FunctionName") // Factory function.
actual inline fun <reified T : Any> NavBackStackSerializer(
    configuration: SavedStateConfiguration
): KSerializer<SnapshotStateList<T>> {
    // Non-Android targets always use the provided `serializersModule`. You MUST also
    // pass the same configuration (or at least its serializersModule) to your
    // encode/decode calls. This is because polymorphic dispatch looks up subtypes
    // in the Encoder/Decoder’s module, not in the KSerializer instance itself.
    return SnapshotStateListSerializer(
        elementSerializer = configuration.serializersModule.serializer<T>()
    )
}