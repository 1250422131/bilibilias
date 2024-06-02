package com.imcys.bilibilias.feature.common

import androidx.annotation.MainThread
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.savedstate.SavedStateRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val VALUES = "values"
private const val KEYS = "keys"

class SavedStateHandle @Inject constructor() {
    private val regular = mutableMapOf<String, Any?>()
    private val flows = mutableMapOf<String, MutableStateFlow<Any?>>()

    @MainThread
    fun <T> getStateFlow(key: String, initialValue: T): StateFlow<T> {
        @Suppress("UNCHECKED_CAST")
        // If a flow exists we should just return it, and since it is a StateFlow and a value must
        // always be set, we know a value must already be available
        return flows.getOrPut(key) {
            // If there is not a value associated with the key, add the initial value, otherwise,
            // use the one we already have.
            if (!regular.containsKey(key)) {
                regular[key] = initialValue
            }
            MutableStateFlow(regular[key]).apply { flows[key] = this }
        }.asStateFlow() as StateFlow<T>
    }

    @MainThread
    operator fun <T> set(key: String, value: T?) {
        regular[key] = value
        flows[key]?.value = value
    }

    @MainThread
    fun <T> remove(key: String): T? {
        @Suppress("UNCHECKED_CAST")
        val latestValue = regular.remove(key) as T?
        flows.remove(key)
        return latestValue
    }
}
